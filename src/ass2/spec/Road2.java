package ass2.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road2 {

    private List<Double> myPoints;
    private double myWidth;
    private int size;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road2(double width, double x0, double y0) {
        myWidth = width;
        setMyPoints(new ArrayList<Double>());
        getMyPoints().add(x0);
        getMyPoints().add(y0);
        size = getMyPoints().size()/2;
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road2(double width, double[] spine) {
        myWidth = width;
        setMyPoints(new ArrayList<Double>());
        for (int i = 0; i < spine.length; i++) {
            getMyPoints().add(spine[i]);
        }
        size = getMyPoints().size()/2;
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        getMyPoints().add(x1);
        getMyPoints().add(y1);
        getMyPoints().add(x2);
        getMyPoints().add(y2);
        getMyPoints().add(x3);
        getMyPoints().add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return getMyPoints().size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = getMyPoints().get(i*2);
        p[1] = getMyPoints().get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = getMyPoints().get(i++);
        double y0 = getMyPoints().get(i++);
        double x1 = getMyPoints().get(i++);
        double y1 = getMyPoints().get(i++);
        double x2 = getMyPoints().get(i++);
        double y2 = getMyPoints().get(i++);
        double x3 = getMyPoints().get(i++);
        double y3 = getMyPoints().get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

	public List<Double> getMyPoints() {
		return myPoints;
	}

	public void setMyPoints(List<Double> myPoints) {
		this.myPoints = myPoints;
	}
	
	public int getSize(){
		return size;
	}


}