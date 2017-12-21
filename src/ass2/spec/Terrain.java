package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    public static final float X_OFFSET = 3;
	public static final float Z_OFFSET = 3;
	private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private List<Other> myOthers;
    private List<Pond> myPonds;
    private float[] mySunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        myOthers = new ArrayList<Other>();
        myPonds = new ArrayList<Pond>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }
    
    public List<Other> others() {
        return myOthers;
    }
    
    public List<Pond> ponds() {
        return myPonds;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
    	 double altitude = 0;
       
         double x1 = Math.floor(x);
         double x2 = Math.ceil(x);
         double z1 = Math.floor(z);
         double z2 = Math.ceil(z);
         
         double y1 = getGridAltitude((int)x1,(int)z1);
 		 double y2 = getGridAltitude((int)x1,(int)z2);
 		 double ya1 = (z - z2) / (z1 - z2) * (y1 - y2) + y2;
 		 if(Double.isNaN(ya1)){
 			 ya1 = y2;
 		 }

 		 double y3 = getGridAltitude((int)x2,(int)z1);
 		 double y4 = getGridAltitude((int)x2,(int)z2);
 		 double ya2 = (z - z2) / (z1 - z2) * (y3 - y4) + y4;
 		if(Double.isNaN(ya2)){
			 ya2 = y4;
		 }

 		if((x1-x2)==0){
 			return ya2;
 		}
 		 altitude = (x - x2) / (x1 - x2) * (ya1 - ya2) + ya2;
 		 //System.out.println(altitude);
         //if(altitude == null) altitude = 0;
         return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }
    
    public void addOther(double x1, double z1, double x2, double z2, int action) {
        Other other = new Other(x1, z1, x2, z2, action);
        myOthers.add(other);
    }
    
    public void addPond(double x, double z, double size) {
    	double y = altitude(x, z);
        Pond pond = new Pond(x,y,z,size);
        myPonds.add(pond);
    }

}