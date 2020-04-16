
package mslib;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


/**
 *
 * @author jallmer
 */
public class Spectrum implements Iterable<Peak> {

    /**
     * For retrieval of {@link Peak}s from the spectrum this can be used to
     * indicate to look for the closest one on m/z basis, only.
     */
    public static final int closestMZ = 0;
    /**
     * For retrieval of {@link Peak}s from the spectrum this can be used to
     * indicate to look for the one with the highest abundance in the window.
     */
    public static final int highestAbundance = 1;

    public static final int weightedAbuMZ = 2;
    public static final int BYXASC = 0;
    public static final int BYXDESC = 1;
    public static final int BYYASC = 2;
    public static final int BYYDESC = 3;
    protected String name = "",
            source = "";
    protected ArrayList<Peak> spectrum = null;
    protected HashMap<Double, Integer> mzind = new HashMap<Double, Integer>();
    /** An annotation can be any information in regard to the {@link Spectrum}. */
    HashMap<String,String> annotations = new HashMap<String,String>();
    private String fileName;

    private String link;


    public HashMap<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(HashMap<String, String> annotations) {
        this.annotations = annotations;
    }

    /**
     * Creates a new instance of Spectrum
     */
    public Spectrum() {
        spectrum = new ArrayList();
    }

    public Spectrum(Spectrum template) {
        name = new String(template.name);
        source = new String(template.source);
        spectrum = new ArrayList<Peak>(template.spectrum);
    }

    public void add(final Peak toAdd) {
        if (toAdd == null) {
            return;
        }
        if (spectrum == null) {
            spectrum = new ArrayList();
        }
        double diff = 0;
        boolean found = false;
        for (Iterator it = spectrum.iterator(); it.hasNext();) {
            Peak s = (Peak) it.next();
            diff = Math.abs(toAdd.getX() - s.getX());
            if (diff <= 0.01) {
                found = true;
                s.setY(s.getY() + toAdd.getY());
                break;
            }
        }
        if (!found) {
            spectrum.add(toAdd);
            mzind.put(toAdd.getX(), spectrum.size() - 1);
        }
    }

    public Peak front() {
        if ((spectrum == null) || (spectrum.size() < 1)) {
            return (null);
        }
        Peak sd = (Peak) spectrum.get(0);
        return (sd);
    }

    public Peak end() {
        if ((spectrum == null) || (spectrum.size() < 1)) {
            return (null);
        }
        Peak sd = (Peak) spectrum.get(spectrum.size() - 1);
        return (sd);
    }

    public ArrayList<Peak> getSpectrum() {
        return (spectrum);
    }

    /**
     * Sorts the spectrum by the methods described in {@link Peak}.<br/> It can
     * be sorted by MZ(X) or Intensity(Y). <br/> It can be sorted ascending or
     * descending.<br/> If the specified mode is unknown sorting will be by MZ
     * in ascending mode.
     *
     * @param mode The mode as specified in {@link Peak}.mode.
     */
    public void sort(final int mode) {
        switch (mode) {
            case Spectrum.BYXASC:
                Collections.sort(spectrum, Peak.byXasc);
                break;
            case Spectrum.BYXDESC:
                Collections.sort(spectrum, Peak.byXdesc);
                break;
            case Spectrum.BYYASC:
                Collections.sort(spectrum, Peak.byYasc);
                break;
            case Spectrum.BYYDESC:
                Collections.sort(spectrum, Peak.byYdesc);
                break;
            default:
                Collections.sort(spectrum, Peak.byXasc);
        }
    }

    /**
     * Sorts the spectrum by the methods described in {@link Peak}.<br/> It can
     * be sorted by MZ(X) or Intensity(Y). <br/> It can be sorted ascending or
     * descending.<br/> If the specified mode is unknown sorting will be by MZ
     * in ascending mode.
     *
     * @param mode The mode as specified in {@link Peak}.mode.
     */
    public void sort(final Comparator<Peak> mode) {
        try {
            Collections.sort(spectrum, mode);
        } catch (Exception e) {
            Collections.sort(spectrum, Peak.byXasc);
        }
    }

    /**
     * Sorts the spectrum by increasing MZ(X) value.
     */
    public void sort() {
        sort(Peak.byXasc);
    }

    public boolean setName(String in) {
        if (in != null && !in.isEmpty()) {
            name = in;
            return (true);
        }
        return (false);
    }

    public String getName() {
        if (name == null) {
            return ("");
        }
        return (name);
    }

    public int getIndex(Peak in) {
        Integer i = mzind.get(in.getX());
        if (i == null) {
            return (-1);
        }
        return (i);
    }

    public String getID() {
        try {
            String str[] = getName().toLowerCase().split("[.]");
            String ret = str[0];
            for (int i = 1; i < str.length - 3; i++) {
                ret += "." + str[i];
            }
            return (ret);
        } catch(Exception e) {
            return "-1";
        }
    }

    public boolean loadPoints(BufferedReader br) {
        String line = "";
        Peak sd = new Peak();
        try {
            while ((line = br.readLine()) != null) {
                sd = new Peak(line);
                spectrum.add(sd);
                mzind.put(sd.getX(), spectrum.size() - 1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return (true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Peak sd = new Peak();
        for (int i = 0; i < spectrum.size(); i++) {
            sd = spectrum.get(i);
            sb.append(sd.getX());
            sb.append(' ');
        }
        if (sb.length() > 2) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return (sb.toString());
    }

    public void normalizeIntensity(double max) {
        double curMax = getMaxIntensity();
        double ratio = max / curMax;
        double y = 0;
        for (int i = 0; i < spectrum.size(); i++) {
            y = spectrum.get(i).getY();
            spectrum.get(i).setY(y * ratio);
        }
    }

    public int[] normalize(int xmin, int xmax, int bin, int tarInt) {
        int size = xmax - xmin;
        if (size <= 0) {
            return (null);
        }
        int ia[] = new int[size];
        double maxIntensity = getMaxIntensity();
        double ratio = tarInt / maxIntensity;
        int val = 0;
        for (int i = 0; i < spectrum.size(); i++) {
            val = (int) spectrum.get(i).getX();
            if (val > size) {
                break;
            }
            double t = spectrum.get(i).getY();
            double te = t * ratio;
            int tes = (int) te;
            ia[val] = (int) (spectrum.get(i).getY() * ratio + 1);
        }
        return (ia);
    }

    private double getMaxIntensity() {
        double max = 0;
        for (int i = 0; i < spectrum.size(); i++) {
            if (spectrum.get(i).getY() > max) {
                max = spectrum.get(i).getY();
            }
        }
        return (max);
    }

    /**
     *
     * @return Retunrs the number of {@link Peak}s in the spectrum.
     */
    public int length() {
        if (spectrum != null) {
            return (spectrum.size());
        }
        return (0);
    }

    public void makeLength(int len) {
        sort(Spectrum.BYYASC);
        int i = 0;
        while (spectrum.size() > len) {
            spectrum.remove(0);
        }
        sort(Spectrum.BYXASC);
    }

    public void centerPeaks(double window) {
        double start = 0,
                end = window,
                intensity = 0,
                sum = 0,
                center = 0;
        ArrayList<Peak> tmp = new ArrayList();
        ArrayList<Peak> local = new ArrayList();
        Iterator it = spectrum.iterator();
        Peak s = new Peak();
        while (it.hasNext()) {
            Peak sd = (Peak) it.next();
            local.add(sd);
            if (sd.getX() > end) {
                if (local.size() > 1) {
                    sum = 0;
                    intensity = 0;
                    for (int i = 0; i < local.size(); i++) {
                        s = local.get(i);
                        sum += s.getX();
                        intensity += s.getY();
                    }
                    center = sum / local.size();
                    tmp.add(new Peak(center, intensity));
                } else {
                    tmp.add(new Peak((Peak) local.get(0)));
                }
                local.clear();
                start = sd.getX();
                end = start + window;
            }
        }
        spectrum.clear();
        mzind.clear();
        spectrum.addAll(tmp);
        int i = 0;
        for (Peak p : tmp) {
            mzind.put(p.getX(), i++);
        }
    }

    /**
     *
     * @return Returns the minimum x and the minimum y value as one
     * {@link DoublePoint}.
     */
    public DoublePoint getMin() {
        DoublePoint dp = new DoublePoint(-1, -1);
        if (spectrum.size() <= 0) {
            return dp;
        }
        dp.setX(((Peak) spectrum.get(0)).getX());
        sort(Spectrum.BYYASC);
        dp.setY(((Peak) spectrum.get(0)).getY());
        sort(Spectrum.BYXASC);
        return (dp);
    }

    /**
     *
     * @param pos The position to retrieve the y value.
     * @return Returns the maximum x and the y value at pos as one
     * {@link DoublePoint}.
     */
    public DoublePoint getMax(int pos) {
        DoublePoint dp = new DoublePoint(-1, -1);
        if (spectrum.isEmpty()) {
            return (dp);
        }
        dp.setX(((Peak) spectrum.get(spectrum.size() - 1)).getX());
        sort(Spectrum.BYYDESC);
        dp.setY(((Peak) spectrum.get(pos)).getY());
        sort(Spectrum.BYXASC);
        return (dp);
    }

    /**
     *
     * @return Returns the maximum x and the maximum y value as one
     * {@link DoublePoint}.
     */
    public DoublePoint getMax() {
        DoublePoint dp = new DoublePoint(-1, -1);
        if (spectrum.size() <= 0) {
            return (dp);
        }
        dp.setX(((Peak) spectrum.get(spectrum.size() - 1)).getX());
        sort(Spectrum.BYYDESC);
        dp.setY(((Peak) spectrum.get(0)).getY());
        sort(Spectrum.BYXASC);
        return (dp);
    }

    public void clear() {
        spectrum.clear();
        spectrum = new ArrayList<Peak>();
        mzind.clear();
        mzind = new HashMap<Double, Integer>();
    }

    public void cleave(double min, double max) {
        int i = 0;
        try {
            while (true) {
                if (spectrum.get(i).getX() < min || spectrum.get(i).getX() > max) {
                    mzind.remove(spectrum.get(i).getX());
                    spectrum.remove(i);
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public int[] toArray(int size) {
        int ret[] = new int[size + 1];
        Peak sd = new Peak();
        int x = 0,
                y = 0;
        for (int i = 0; i < spectrum.size(); i++) {
            sd = spectrum.get(i);
            x = (int) Math.round(sd.getX()) * 2;
            y = (int) Math.round(sd.getY());
            if (x < ret.length) {
                ret[x] = y;
            }
        }
        return (ret);
    }

    public double[] toDoubleArray() {
        double ret[] = new double[spectrum.size()];
        Peak sd = new Peak();
        for (int i = 0; i < spectrum.size(); i++) {
            sd = spectrum.get(i);
            ret[i] = sd.getX();
        }
        return (ret);
    }

    public double[] toDoubleArray(int size) {
        double ret[] = new double[size];
        Peak sd = new Peak();
        for (int i = 0; i < spectrum.size(); i++) {
            if (i >= size) {
                break;
            }
            sd = spectrum.get(i);
            ret[i] = sd.getX();
        }
        return (ret);
    }

    public double getSum() {
        double sum = 0;
        Peak s = new Peak();
        for (Iterator it = spectrum.iterator(); it.hasNext();) {
            s = (Peak) it.next();
            sum += s.getY();
        }
        return (sum);
    }

    public double getMean() {
        double mean = getSum();
        mean /= (double) spectrum.size();
        return (mean);
    }

    public ArrayList<Peak> getWindow(int percent) {
        //calculate startmz, endmz according to percent given
        int startPercent = (100 - percent) / 2;
        startPercent = (startPercent > 20 && startPercent < 80) ? startPercent : 40;
        int length = spectrum.size();
        int windowlength = Math.round((length * percent) / 100);
        int startpoint = Math.round((length * startPercent) / 100);
        int endpoint = startpoint + windowlength;
        double startmz = spectrum.get(startpoint).getX();
        double endmz = spectrum.get(endpoint).getX();
        return (getWindow(startmz, endmz));
    }

    /**
     * Retrieves the {@link Peak} at the given index.
     *
     * @param index Index of the desired {@link Peak}.
     * @return Returns the {@link Peak} at the desired index or null if no such
     * {@link Peak} exists.
     */
    public Peak getPeak(int index) {
        if (spectrum == null) {
            return (null);
        }
        if (index >= spectrum.size() || index < 0) {
            return (null);
        }
        return (spectrum.get(index));
    }

    public Peak getClosestPeak(double x, double precision) {
        Peak closest = null;
        double diff = 0,
                closestDiff = 10;
        for (Peak sd : spectrum) {
            diff = Math.abs(sd.getX() - x);
            if (diff < precision && diff < closestDiff) {
                closestDiff = diff;
                closest = sd;
            }
        }
        return (closest);
    }

    /**
     * This method looks for the closest {@link Peak} within +/- precision
     *
     * @param mz the m/z for which the closest {@link Peak} should be returned.
     * @param precision the precision in absolute Dalton
     * @return Returns the closest {@link Peak}'s index within +/- precision, or
     * null if there is none.
     */
    public int getClosestPeakIndex(double mz, double precision) {
        Peak ci = getClosestPeak(mz, precision);
        if (ci == null) {
            return (-1);
        }
        Integer i = mzind.get(ci.getX());
        if (i != null) {
            return (i);
        }
        return (-1);
    }

    /**
     * This method looks for the closest {@link Peak} within +/- 0.5 Dalton
     * precision.
     *
     * @param mz the m/z for which the closest {@link Peak} should be returned.
     * @return Returns the closest {@link Peak}'s index within +/- precision, or
     * null if there is none.
     */
    public int getClosestPeakIndex(double mz) {
        Peak p = getClosestPeak(mz, 0.5);
        Integer i = mzind.get(p.getX());
        if (i != null) {
            return (i);
        }
        return (-1);
    }


    public int getClosestPeakIndex(double mz, int which, double precision) {
        switch (which) {
            default:
            case closestMZ:
                return (getClosestPeakIndex(mz, precision));
            case highestAbundance:
                return (getClosestPeakIndexAbundance(mz, precision));
            case weightedAbuMZ:
                return (getClosestPeakIndexWeighted(mz, precision));
        }
    }


    public Peak getClosestPeak(double mz, int which, double precision) {
        return (getPeak(getClosestPeakIndex(mz, which, precision)));
    }


    public int getClosestPeakIndexAbundance(double mz, double precision) {
        int[] matches = getMatchingPeakIndices(mz, precision);
        double max = 0,
                dist = 10;
        int selInd = -1;
        if (matches != null && matches.length > 0) {
            for (int match : matches) {
                if (spectrum.get(match).getY() > max && Math.abs(mz - spectrum.get(match).getX()) < dist) {
                    max = spectrum.get(match).getY();
                    dist = Math.abs(mz - spectrum.get(match).getX());
                    selInd = match;
                }
            }
        }
        return (selInd);
    }

    /**
     * Retrieves the {@link Peak} with the highest abundance that is closest to
     * the desired mz from the window presented by mz as the center +/-
     * precision. Selects the {@link Peak} where mz abundance/distance from mz
     * is largest.
     *
     * @param mz the mass to charge ratio to be the center of the window from
     * which the desired {@link Peak} should be selected.
     * @param precision The precision in absolute Dalton
     * @return Returns the {@link Peak} where mz abundance/distance_from_mz is
     * largest or -1 if no {@link Peak} exists.
     */
    private int getClosestPeakIndexWeighted(double mz, double precision) {
        int[] matches = getMatchingPeakIndices(mz, precision);
        double max = 0,
                dist = 10,
                val = 0;
        int selInd = -1;
        Peak tmp = new Peak(1, 1);
        if (matches != null && matches.length > 0) {
            for (int match : matches) {
                tmp = spectrum.get(match);
                dist = Math.abs(tmp.getX() - mz);
                val = tmp.getY() / dist;
                if (val > max) {
                    max = tmp.getY();
                    selInd = match;
                }
            }
        }
        return (selInd);
    }

    /**
     * Retrieves all indices of {@link Peak}s which are within
     * fragmentIonPrecision of the selected mz value.
     *
     * @param mz Mass to charge ratio of the target area in the
     * {@link Spectrum}.
     * @param fragmentIonPrecison The mass tolerance in absolute Dalton.
     * @return Returns the indices in the {@link ArrayList} containing the
     * {@link Peak}s or null if there are is no such index.
     */
    public int[] getMatchingPeakIndices(double mz, double fragmentIonPrecison) {
        ArrayList<Integer> cps = new ArrayList<Integer>();
        double diff = -100;
        for (int i = 0; i < spectrum.size(); i++) {
            Peak sd = spectrum.get(i);
            diff = Math.abs(sd.getX() - mz);
            if (diff < fragmentIonPrecison) {
                cps.add(i);
            }
        }
        if (cps.size() > 0) {
            int[] ret = new int[cps.size()];
            for (int i = 0; i < cps.size(); i++) {
                ret[i] = cps.get(i);
            }
            return (ret);
        }
        return (null);
    }

    /**
     * Retrieves all peak indices within given fragment tolerance for given
     * direction. 0 for left and 1 for right direction
     *
     * @param mz
     * @param fragmentIonPrecison
     * @param direction
     * @return peak indices array
     */
    public int[] getMatchingPeakIndicesInDirection(double mz, double fragmentIonPrecison, int direction) {
        ArrayList<Integer> cps = new ArrayList<Integer>();
        double diff = -100;

        switch (direction) {
            case 1:
                for (int i = 0; i < spectrum.size(); i++) {
                    Peak sd = spectrum.get(i);
                    diff = sd.getX() - mz;
                    if (diff > 0 && diff < fragmentIonPrecison) {
                        cps.add(i);
                    }
                }
                break;
            case 0:
                for (int i = 0; i < spectrum.size(); i++) {
                    Peak sd = spectrum.get(i);
                    diff = sd.getX() - mz;
                    if (diff < 0 && Math.abs(diff) < fragmentIonPrecison) {
                        cps.add(i);
                    }
                    break;
                }
        }
        if (cps.size() > 0) {
            int[] ret = new int[cps.size()];
            for (int i = 0; i < cps.size(); i++) {
                ret[i] = cps.get(i);
            }
            return (ret);
        }
        return (null);
    }

    public void padWithZero() {
        sort();
        Peak sd = spectrum.get(0);
        int f = (int) sd.getX();
        f--;
        Peak first = new Peak(f, 0);
        sd = spectrum.get(spectrum.size() - 1);
        int l = (int) sd.getX();
        l++;
        Peak last = new Peak(l, 0);
        ArrayList<Peak> al = new ArrayList<Peak>();
        al.add(first);
        for (Peak s : spectrum) {
            al.add(s);
        }
        al.add(last);
        spectrum = al;
    }

    /**
     * Retrieves the {@link Peak}s from the window of the spectrum starting and
     * including startMZ and ending with but not including endMZ.
     *
     * @param startMZ The value from where to start extracting {@link Peak}s
     * from the {@link Spectrum} (inclusive).
     * @param endMZ The value where to stop extracting {@link Peak}s from the
     * {@link Spectrum} (exclusive).
     * @return returns a list of {@link Peak}s which can be empty.
     */
    public ArrayList<Peak> getWindow(double startMZ, double endMZ) {
        ArrayList<Peak> window = new ArrayList<Peak>();
        for (int i = 0; i < spectrum.size(); i++) {
            Peak p = spectrum.get(i);
            if (p.getX() > endMZ) {
                return (window);
            }
            if (p.getX() >= startMZ) {
                window.add(p);
            }
        }
        return (window);
    }

    /**
     * 
     * @param mz The mass from where to search upstream (towards higher masses).
     * @return Returns the next upstream {@link Peak} or null if there is none.
     */
    public Peak getUpstreamPeak(double mz) {
        for(Peak p: spectrum) {
            if(p.getX() >= mz)
                return p;
        }
        return null;
    }


    /**
     * This running average is based on the indeces of the peaks It could be
     * contemplated to base it on a width in Dalton.
     *
     * @param width
     */
    public void runningAvg(int width) {
        double ai = 0;
        int w = width / 2;
        for (int i = w; i < spectrum.size() - w; i++) {
            ai = 0;
            for (int p = i - w; p <= i + w; p++) {
                ai += spectrum.get(p).getY();
            }
            ai /= (double) width;
            spectrum.get(i).setY(ai);
        }
    }

    /**
     * Currently a peak is accepted if previous and following peaks have lower
     * abundance. A function that looks at the slope of several peaks and then
     * accepts peaks if the slope increases on the left and decreases on the
     * right could be good A function fitting parts of the spectrum to gaussian
     * would be also good.
     *
     * @return
     */
    public Spectrum getCentroid() {
        Spectrum s = new Spectrum(this);
        s.spectrum.clear();
        s.spectrum = (ArrayList<Peak>) getCentroidList().clone();
        return (s);
    }

    public ArrayList<Peak> getCentroidList() {
        double ab = 0;
        ArrayList<Peak> ret = new ArrayList<Peak>();
        for (int i = 1; i < spectrum.size() - 1; i++) {
            ab = spectrum.get(i).getY();
            if ((spectrum.get(i - 1).getY() < ab) && (ab > spectrum.get(i + 1).getY())) {
                ret.add(spectrum.get(i).clone());
            }
        }
        return (ret);
    }

    public void setSpectrum(ArrayList<Peak> spectrum) {
        this.spectrum = spectrum;
    }

    public Iterator<Peak> iterator() {
        return (spectrum.iterator());
    }
    /**
     * Allows the annotation of a {@link Spectrum} with a name value pair.
     * @param name The name of the additional information
     * @param value The value associated with the name.
     * @return returns true if successful and false otherwise (e.g.: if the name existed already).
     */
    public boolean setAnnotation(final String name, final String value) {
        if(annotations.containsKey(name))
            return false;
        annotations.put(name, value);
        return true;
    }
    
    /**
     * 
     * @param name the name of the annotation to retrieve the value for.
     * @return Returns the value or null if no such annotation exists.
     */
    public String getAnnotation(final String name) {
        return annotations.get(name);
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;

    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}

