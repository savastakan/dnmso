package mslib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author jallmer
 */
public class MassSpectrum 
        extends Spectrum {
    /** Defines a name for MS (survey) spectra. */
    public static final int MSLevel = 1;
    /** Defines a name for fragmentation spectra. */
    public static final int MSMSLevel = 2;
    /** Defines a name for MS3 fragmentation spectra. */
    public static final int MS3Level = 3;
    /** Defines a name for MS4 fragmentation spectra. */
    public static final int MS4Level = 4;
    /** Defines a name for MS5 fragmentation spectra. */
    public static final int MS5Level = 5;
    /** Defines a name for MS6 fragmentation spectra. */
    public static final int MS6Level = 6;
    /** Defines a name for MS7 fragmentation spectra. */
    public static final int MS7Level = 7;
    /** Defines a name for MSn fragmentation spectra. */
    public static final int MSNLevel = 8;
    /** The name for negative polarity.*/
    public static final int negativeMode = 0;
    /** The name for positive polarity.*/
    public static final int positiveMode = 1;

    /** The intensity of the precursor ion from the previous MS level. */
    protected double    precursorIntensity = 0D;
    /** The mass of the precursor which was identified in the MS spectrum, collected, and fragmented to yield this MS/MS spectrum (not set: 0). */
    protected double    precursorMass = 0D;
    /** The total intensity of all ions in the current MS/MS spectrum (not set: -1)*/
    protected double    totIonCurrent = -1;
    /** The charge of the precursor which was identified in the MS spectrum, collected, and fragmented to yield this MS/MS spectrum (not set: 1). */
    protected int       precursorCharge=1;
    /** Which number in a sequential number of scans is this MS/MS spectrum (if part of a collection, not set: -1) */
    protected int       scanNumber = -1;
   /** Which number in a sequential number of scans is the last MS/MS spectrum of a series of merged scans. */
    protected int       endScanNumber = -1;
    protected String    scanInfo = "";
    /** Which MS level was used for this scan? (Default: MSMSLevel).*/
    protected int       msLevel = 1;
    /** Polarity that was used to make this measurment (negative or positive). Note: only positive mode supported at the moment)*/
    protected int       polarity = 1;
    /** The collision energy used for the fragmentation process (default: 0eV). */
    protected int       collisionEnergy = 0;
    /** The lowest m/z measured for this scan (Default: 200). */
    protected double    lowMZ = 200D;
    /** The highest m/z measured for this scan (Default: 2000). */
    protected double    highMZ = 2000D;
    /** The {@link Peak} with the strongest intensity in the current MassSpectrum (Default: null). */
    protected Peak      basePeak = null;
    /** The number of cycles that were averaged to make this MassSpectrum (Default: 0). */
    protected int       cycleNumber = 0;
    /** The filling time of the trap to acquire this MassSpectrum (Default: 0ms). */
    protected int       fillingTime = 0;
    /** The retention time when the first spectrum was measured. */
    double startRetentionTime = 0;
    /** The retention time when the last spectrum was measured. */
    double endRetentionTime = 0;

    /** Creates a new instance of MassSpectrum */
    public MassSpectrum() {
        
    }

    @Override
    public ArrayList<Peak> getSpectrum() {
        return(super.getSpectrum());
    }
    public boolean setPrecursorMass(final double mass) {
        if(mass > 0.0) {
            precursorMass = mass;
            return(true);
        }
        return(false);
    }
    public boolean setCharge(final int charge) {
        if((charge != 0) && (Math.abs(charge) < 10)) {
            this.precursorCharge =  charge;
            return(true);
        }
        return(false);
    }
    public int getCharge() {
        return(precursorCharge);
    }
    public double getTIC() {
        if(totIonCurrent == -1)
            totIonCurrent = getSum();
        return(totIonCurrent);
    }
    public void setTIC(final double tic) {
        totIonCurrent = tic;
    }
    public double getPrecursorMass() {
        return(precursorMass);
    }

    @Override
    public MassSpectrum clone() {
        MassSpectrum ms = new MassSpectrum();
        ms.precursorMass = precursorMass;
        ms.precursorCharge = precursorCharge;
        ms.spectrum = new ArrayList<Peak>();
        for(Peak sd : spectrum)
            ms.spectrum.add(sd.clone());
        ms.source = source;
        ms.name = name;
        return(ms);
    }

    /** TODO: Deprecated, remove */
    public void parseString(String msmsData) {
        String data[] = msmsData.split("[;]");
        String tmp[] = data[0].split("[,\t]",2);
        precursorMass = Double.parseDouble(tmp[0].trim());
        precursorCharge = Integer.parseInt(tmp[1].trim());
        Peak sd = new Peak();
        for(int i=1; i<data.length; i++) {
            tmp = data[i].split("[,\t]",2);
            sd = new Peak(Double.parseDouble(tmp[0].trim()),Double.parseDouble(tmp[1].trim()));
            if(sd.getX() > 0 && sd.getY() > 0)
                spectrum.add(sd);
        }
    }

    public String getAmsString() {
        if(spectrum.size() <= 0)
            return("");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);
        DecimalFormat df1 = new DecimalFormat();
        df1.setMaximumFractionDigits(1);
        df1.setGroupingUsed(false);
        df1.setMinimumFractionDigits(1); 
        StringBuilder ret = new StringBuilder();
        ret.append(precursorMass);
        //ret.append(df.format(precursorMass));
        ret.append(",");
        ret.append(precursorCharge);
        ret.append(";");
        for (Iterator<Peak> ot = spectrum.iterator (); ot.hasNext (); ) {
            Peak o = ot.next();
            ret.append(df.format(o.getX()));
            ret.append(",");
            ret.append(df1.format(o.getY()));
            ret.append(";");
        }
        df = null;
        return(ret.toString());
    }
    
    @Override
    public String toString() {
        return(getName());
    }
    
    @Override
    public String getID() {
        if(scanNumber != -1)
            return "" + scanNumber;
        return getName();
    }
    
    public void setAmsSpectrum(String amsSpec) {
        clear();
        String[] rows = amsSpec.split("[;]");
        String[] vals = null;
        String row = rows[0];
        vals = row.split("[,]");
        setPrecursorMass(Double.parseDouble(vals[0]));
        setCharge(Integer.parseInt(vals[1]));
        for(int i=1; i<rows.length; i++) {
            row = rows[i];
            vals = row.split("[,]");
            if(vals != null && vals.length >= 2)
                add(new Peak(Double.parseDouble(vals[0]),Double.parseDouble(vals[1])));
                //add(new Peak(Double.parseDouble(vals[0]+"."+vals[1]),Double.parseDouble(vals[2])));
        }
        row = null;
        vals = null;
        rows = null;
        setTIC(this.getSum());
    }

    int[] getIntArray(int max, HashMap<Integer,Peak> spec) {
        int[] peaks = new int[max];
        int i=0;
        for(Peak sd : spectrum) {
            spec.put((int)sd.getX(), sd);
            peaks[(int)sd.getX()] += (int)sd.getY();
            i++;
        }
        return(peaks);
    }

    public int[] getIntArray(int max) {
        int[] peaks = new int[max+1];
        int i=0;
        for(Peak sd : spectrum) {
            peaks[(int)sd.getX()] += (int)sd.getY();
            i++;
        }
        return(peaks);  
    }

    public double[] getDoubleArray(int max) {
        double[] peaks = new double[max+1];
        int i=0;
        for(Peak sd : spectrum) {
            peaks[(int)sd.getX()] += sd.getY();
            i++;
        }
        return(peaks);  
    }

    public int[] getIntArray() {
        DoublePoint p = getMax(0);
        return(getIntArray((int)p.getX()));
    }

    public double[] getDoubleArray() {
        DoublePoint p = getMax(0);
        return(getDoubleArray((int)p.getX()));
    }



    public void setSpectrum(int[] peaks) {
        spectrum.clear();
        for(int x=0; x<peaks.length; x++) {
            if(peaks[x] > 0) {
                Peak sd = new Peak(x,peaks[x]);
                spectrum.add(sd);
            }
        }
    }

    @Override
    public MassSpectrum getCentroid() {
        MassSpectrum ms = this.clone();
        ms.spectrum = (ArrayList<Peak>) getCentroidList().clone();
        return(ms);
    }

    @Override
    public void setSpectrum(ArrayList<Peak> spectrum) {
        this.spectrum = spectrum;
        getTIC();
    }

    public String getSource() {
        return(source);
    }

    public void addAll(Collection<Peak> peaks) {
        for(Peak p : peaks)
            add(p);
    }


    /**
     *
     * Getter and Setters
     *
     */

    
    public Peak getBasePeak() {
        return basePeak;
    }

    public void setBasePeak(Peak basePeak) {
        this.basePeak = basePeak;
    }
    public int getCollisionEnergy() {
        return collisionEnergy;
    }

    public void setCollisionEnergy(int collisionEnergy) {
        this.collisionEnergy = collisionEnergy;
    }


    public double getHighMZ() {
        return highMZ;
    }

    public void setHighMZ(double highMZ) {
        this.highMZ = highMZ;
    }

    public double getLowMZ() {
        return lowMZ;
    }

    public void setLowMZ(double lowMZ) {
        this.lowMZ = lowMZ;
    }

    public int getMsLevel() {
        return msLevel;
    }

    /**
     * Must be one of the defined levels in {@link MassSpectrum}.
     * @param msLevel Sets the MS level defaults to MSMSlevel
     */
    public void setMsLevel(int msLevel) {
        if(msLevel < 0 || msLevel > 8)
            this.msLevel = MSMSLevel;
        else
            this.msLevel = msLevel;
    }

    public int getPolarity() {
        return polarity;
    }

    /**
     * Must be one of the polarities defined in {@link MassSpectrum}.
     * @param polarity The polarity (defaults to positiveMode).
     */
    public void setPolarity(int polarity) {
        if(polarity < 0 || polarity > 1)
            this.polarity = MassSpectrum.positiveMode;
        else
            this.polarity = polarity;
    }

    public int getPrecursorCharge() {
        return precursorCharge;
    }

    public void setPrecursorCharge(int precursorCharge) {
        this.precursorCharge = precursorCharge;
    }

    public int getScanNumber() {
        return scanNumber;
    }


    public void setScanNumber(int scanNumber) {
        this.scanNumber = scanNumber;
        this.endScanNumber = scanNumber;
    }
    
    /**
     * Assumes that multiple scans have been merged and therefore uses start 
     * scan number as the first scan and end scan number as the last.
     * In case the number given as endScanNumber is less than startScanNumber
     * endScanNumber is set equal to startScanNumber.
     * @param startScanNumber The first scan number of the merged scans.
     * @param endScanNumber The last scan number of the merged scans.
     */
    public void setScanNumber(int startScanNumber, int endScanNumber) {
        this.scanNumber = startScanNumber;
        if(endScanNumber > scanNumber)
            this.endScanNumber = endScanNumber;
        else
            this.endScanNumber = startScanNumber;
    }
    
    public double getTotIonCurrent() {
        return totIonCurrent;
    }

    public void setTotIonCurrent(double totIonCurrent) {
        this.totIonCurrent = totIonCurrent;
    }

    public double getPrecursorIntensity() {
        return precursorIntensity;
    }

    public void setPrecursorIntensity(double precursorIntensity) {
        this.precursorIntensity = precursorIntensity;
    }

    public Peak getPrecursorPeak() {
        return(new Peak(precursorMass,precursorIntensity));
    }

    public void setPrecursorPeak(Peak precursor) {
        precursorMass = precursor.getX();
        precursorIntensity = precursor.getY();
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public int getFillingTime() {
        return fillingTime;
    }

    public void setFillingTime(int fillingTime) {
        this.fillingTime = fillingTime;
    }

    /**
     * Removes a {@link Peak} from the spectrum.
     * @param peak The {@link Peak} to be removed.
     */
    public void remove(Peak peak) {
        spectrum.remove(peak);
    }

    public int getStartScanNumber() {
        return(scanNumber);
    }
    

    public int getEndScanNumber() {
        return(endScanNumber);
    }    
    
    public void setRT(double startRTinSec, double endRTinSec) {
        startRetentionTime = startRTinSec;
        endRetentionTime = endRTinSec;
    }
    
    public double getStartRT() {
        return(startRetentionTime);
    }
    
    public double getEndRT() {
        return(endRetentionTime);
    }

    public String getScanInfo() {
        return scanInfo;
    }

    public void setScanInfo(String scanInfo) {
        this.scanInfo = scanInfo;
    }

    public MassSpectrum subtract(MassSpectrum bufferMS, double tolerance) {
        MassSpectrum res = new MassSpectrum();
        res.setPrecursorCharge(precursorCharge);
        res.setCharge(precursorCharge);
        for(Peak p : bufferMS) {
            ArrayList<Peak> matches = getWindow(p.getX()-tolerance, p.getX()+tolerance);
            Peak np = new Peak();
            if(!matches.isEmpty()) {
                for(int i=0; i<matches.size(); i++) {
                    np.addY(matches.get(i).getY());
                }
                double mz = 0;
                for(int i=0; i<matches.size(); i++) {
                    mz += matches.get(i).getX() * matches.get(i).getY()/np.getY();
                }
                np.setX(mz);
                np.setY(np.getY() - p.getY());
                res.add(np);
            }
        }
        return(res);
    }

     public static final Comparator<MassSpectrum> byScanID = new Comparator<MassSpectrum>() {
        public int compare(final MassSpectrum l, final MassSpectrum r) {
            if(l.equals(r)){
                return(0);
            }else{
            double ret = (l.getScanNumber() - r.getScanNumber());
            return((int)ret);
            }
        }
    };
}
