/*
 * Peak.java
 *
 * Created on April 27, 2007, 2:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mslib;

import java.util.Comparator;


public class Peak {
    /** The x value of the Peak initialized with -1. */
    private double  x = -1D;
    /** The y value of the Peak initialized with -1. */
    private double  y = -1D;
    
    /** Creates a new instance of Peak */
    public Peak() {
        set(-1D,-1D);
    }

    /**
     * Instantiates using x and y values in an existing Peak.
     * @param p An existing Peak if null uses default values.
     */
    public Peak(final Peak p) {
        if(p == null)
            set(-1D,-1D);
        else
            set(p.getX(),p.getY());
    }

    /**
     * Expects the String inp to contain two values, first the x value and then
     * the y value separated by one of the following character: " ,;/\t".
     * @param inp A string representing one x and y value pair. Uses default values
     * if inp is null or not supported.
     */
    public Peak(final String inp) {
        try {
            String[] sa = inp.split("[ ,;/\t]",2);
            set(Double.parseDouble(sa[0]),Double.parseDouble(sa[1]));
        } catch(Exception e) {
            set(-1D,-1D);
        }
    }

    /**
     * Instantiates using the given x and y value.
     * @param x A double value representing the x part.
     * @param y A double value representing the y part.
     */
    public Peak(final double x, final double y) {
        set(x,y);
    }

    /**
     * Creates a deep copy of Peak by copying the underlying values.
     * @param p The Peak to be cloned.
     * @return Returns a new Peak with the values of p or standard values if p is null.
     */
    private static Peak clone(Peak p) {
        Peak ret = new Peak();
        ret.set(p);
        return(ret);
    }

    @Override
    public Peak clone() {
        return(clone(this));
    }

    /**
     * Sets the x part of Peak.
     * @param x A double value representing the x part of Peak.
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Sets the y part of Peak.
     * @param y A double value representing the y part of Peak.
     */
    public void setY(final double y) {
        this.y = y;
    }

    /**
     * Adds y to the y part of this Peak.
     * @param y A double value representing the y part to be added to the y part of this Peak.
     */
    public void addY(final double y) {
        this.y += y;
    }

    public void add(final Peak p) {
        if(p != null)
            addY(p.getY());
    }

    /**
     * Sets both x and y at the same time by calling the underlying methods:<br/>
     * @param x A double value representing the x part of Peak.
     * @param y A double value representing the y part of Peak.
     */
    public final void set(final double x, final double y) {
        setX(x);
        setY(y);
    }

    /**
     * Sets both x and y at the same time by copying the values of p.
     * If p is null default values will be used.
     * @param p The Peak which's values should be used.
     */
    public void set(final Peak p) {
        if(p == null)
            set(-1D,-1D);
        else
            set(p.x,p.y);
    }

    /**
     * The x value of the Peak.
     * @return Returns the x part of the Peak.
     */
    public double getX() {
        return(x);
    }

    /**
     * The y value of the Peak.
     * @return Returns the y part of the Peak.
     */
    public double getY() {
        return(y);
    }

    /**
     * Makes a deep copy of this Peak and then returns it.
     * @return Returns a clone of this Peak.
     */
    public Peak get() {
        Peak p = this.clone();
        return(p);
    }

    /**
     * Tests whether two peaks are equal. To be equal both x and y part need to
     * be within 0.00001.
     * @param other The Peak to be compared with.
     * @return Returns true when equal and false otherwise.
     */
    public boolean equals(final Peak other) {
        if(other == null)
            return(false);
        if (Math.abs(this.x - other.x) > 0.00001)
            return false;
        if (Math.abs(this.y - other.y) > 0.00001)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Peak other = (Peak) obj;
        return(this.equals(other));
    }

    @Override
    public String toString() {
        return("" + getX() + "," + getY());
    }

    /**
     * Can be used to sort a spectrum with high abundance Peaks followed by lower abundant ones.
     */
    public static final Comparator<Peak> byYdesc = new Comparator<Peak>() {
        public int compare(final Peak l, final Peak r) {
            if(r.getY() > l.getY())
                return 1;
            else {
                if(r.getY() < l.getY())
                    return -1;
                else
                    return 0;
            }
        }
    };

    /**
     * Can be used to sort a spectrum with low abundance Peaks followed by increasingly abundant ones.
     */
    public static final Comparator<Peak> byYasc = new Comparator<Peak>() {
        public int compare(final Peak l, final Peak r) {
            if(r.getY() > l.getY())
                return -1;
            else {
                if(r.getY() < l.getY())
                    return 1;
                else
                    return 0;
            }
        }
    };

    /**
     * Can be used to sort a spectrum with high mz Peaks followed by lower mz ones.
     */
    public static final Comparator<Peak> byXdesc = new Comparator<Peak>() {
        public int compare(final Peak l, final Peak r) {
            if(r.getX() > l.getX())
                return 1;
            else {
                if(r.getX() < l.getX())
                    return -1;
                else
                    return 0;
            }
        }
    };
    
    public static final Comparator<Peak> byStartMZ = new Comparator<Peak>() {
        public int compare(Peak l, Peak r) {
            return (int) ((l.getX() - r.getX()) * 10000);
        }
    };
    /**
     * Can be used to sort a spectrum naturally (low mz precedes high mz).
     */
    public static final Comparator<Peak> byXasc = new Comparator<Peak>() {
        public int compare(final Peak l, final Peak r) {
            if(r.getX() > l.getX())
                return -1;
            else {
                if(r.getX() < l.getX())
                    return 1;
                else
                    return 0;
            }
        }
    };
}

