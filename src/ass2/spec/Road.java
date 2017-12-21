package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        setMyPoints(new ArrayList<Double>());
        getMyPoints().add(x0);
        getMyPoints().add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        setMyPoints(new ArrayList<Double>());
        for (int i = 0; i < spine.length; i++) {
            getMyPoints().add(spine[i]);
        }
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


	public void drawRoad(GL2 gl, Terrain myTerrain) {
        //gl.glLineWidth(30);
        
        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINES);
        

        //gl.glBindTexture(GL2.GL_TEXTURE_2D, myRoadTexture.getTextureId());
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
        gl.glBegin(GL2.GL_QUAD_STRIP); {
            
            double[] prev_pt = point(0);
            double[] pt;
            int count = 0;

            
            double y = myTerrain.altitude(this.controlPoint(0)[0], this.controlPoint(0)[1]);
            //System.out.println((myPoints.size()+2)/8);
            
            for(double i = 0.01; i < (myPoints.size()+2)/8; i += 0.01) {
                
                pt = point(i);
                double[][] normals = getNormal(prev_pt, pt);
                
                normals[0] = normalize(normals[0]);
                normals[1] = normalize(normals[1]);
        
                double[] roadPointA = getPoint(normals[0], pt);
                double[] roadPointB = getPoint(normals[1], pt);
                
                gl.glTexCoord2d(i, 0);
                gl.glVertex3d(roadPointB[0], y+0.01, roadPointB[1]);
                
                gl.glTexCoord2d(i, 1);
                gl.glVertex3d(roadPointA[0], y+0.01, roadPointA[1]);
       
                count++;
                prev_pt = pt;
             
            }
            
            
            /*
            for(double i = 0.99; i >= 0.01; i -= 0.01) {
                
                pt = point(i);
                double[][] normals = normal(pt, prev_pt);
                
                normals[1] = normalize(normals[1]);
                normals[0] = normalize(normals[0]);
                
        
                double[] roadPointA = getRoadPoint(normals[0], pt);
                double[] roadPointB = getRoadPoint(normals[1], pt);
                
                gl.glTexCoord2d(i, 0);
                gl.glVertex3d(roadPointB[1], y-0.01, roadPointB[0]);
                
                gl.glTexCoord2d(i, 1);
                gl.glVertex3d(roadPointA[1], y-0.01, roadPointA[0]);
                                 
                //count++;
                pt = prev_pt ;
                
            }
            */
            
        } gl.glEnd();
        
    }
    
    public double[][] getNormal(double[] a1, double[] a2) {
        
        double[][] normals = new double[2][2];
        double dx = a2[0] - a1[0];
        double dz = a2[1] - a1[1];
        
        normals[0][0] = -dz;
        normals[0][1] = dx;
        normals[1][0] = dz;
        normals[1][1] = -dx;
        
        return normals;
    }
    
    
    
    // Get Points of road width
    public double[] getPoint(double[] vec, double[] pt) {
        //System.out.println(pt[0]+""+vector[0]+""+pt[1]+""+vector[1]);
        return new double[] {pt[0] + vec[0]*myWidth/2, pt[1] + vec[1]*myWidth/2};
    }
    
    	public double[] normalize(double[] vec) {
        
        double magnitude = Math.sqrt(Math.pow(vec[0], 2) + Math.pow(vec[1], 2));
        
        double[] normalized = new double[2];
        
	    normalized[0] = vec[0] / magnitude;
	    normalized[1] = vec[1] / magnitude;
        
	    //System.out.println(normalized[0]+""+normalized[1]);
	    
        return normalized;
    }

}
