package mslib;

/**
 * A DoublePoint is very similar to a {@link Peak} but in order not to mistake
 * some arbitrary combination of x and y values with a {@link Peak}, the DoublePoint
 * needed to be introduced.
 * @author jens
 */
public class DoublePoint {
    private double  x=0,
                    y=0;
    /** Creates a new instance of DoublePoint */
    public DoublePoint() {
        
    }
    
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
}
