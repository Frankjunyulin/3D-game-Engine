package ass2.spec;

public class MathUtil {
    /**
     * Normalise an angle to the range [-180, 180)
     * 
     * @param angle 
     * @return
     */
    static public double normaliseAngle(double angle) {
        return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }

    /**
     * Clamp a value to the given range
     * 
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Multiply two matrices
     * 
     * @param p A 3x3 matrix
     * @param q A 3x3 matrix
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     * 
     * @param m A 3x3 matrix
     * @param v A 3x1 vector
     * @return
     */
    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[3];

        for (int i = 0; i < 3; i++) {
            u[i] = 0;
            for (int j = 0; j < 3; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }



    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * TODO: A 2D translation matrix for the given offset vector
     * 
     * @param pos
     * @return
     */
    public static double[][] translationMatrix(double[] v) {

    	//double[][] s = new double[3][3];
    	double[][] matrix = new double[3][3];
    	
    	for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
            	if(i==j){
            		matrix[i][j] = 1;
            	}else if(j==2 && i!=j){
            		matrix[i][j] = v[i];
            	}
            	else{
            		matrix[i][j] = 0;
            	}
                
            }
        }
        return matrix;
    }

    /**
     * TODO: A 2D rotation matrix for the given angle
     * 
     * @param angle in degrees
     * @return
     */
    public static double[][] rotationMatrix(double angle) {
    	double[][] matrix = new double[3][3];
    	double radius = (angle*2*(Math.PI))/360;
    	matrix[0][0] = Math.cos(radius);
    	matrix[0][1] = -Math.sin(radius);
    	matrix[0][2] = 0;
    	matrix[1][0] = Math.sin(radius);
    	matrix[1][1] = Math.cos(radius);
    	matrix[1][2] = 0;
    	matrix[2][0] = 0;
    	matrix[2][1] = 0;
    	matrix[2][2] = 1;
    	
        return matrix;
    }

    /**
     * TODO: A 2D scale matrix that scales both axes by the same factor
     * 
     * @param scale
     * @return
     */
    public static double[][] scaleMatrix(double scale) {
    	double[][] matrix = new double[3][3];
    	for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	if(i == j){
            		if(i != 2){
            			matrix[i][j] = scale;
            		}
            		else{
            			matrix[i][j] = 1;
            		}
            	}else{         	
            		matrix[i][j] = 0;
            	}
            }
        }

        return matrix;
    }
    
    public static double [] getUnitVector(double[] u) {
        double unitVector[] = new double[4];
        
        double magnitude = Math.sqrt(u[0] * u[0] + u[1] * u[1] + u[2] * u[2]);
        unitVector[0] = u[0] / magnitude;
        unitVector[1] = u[1] / magnitude;
        unitVector[2] = u[2] / magnitude;
        unitVector[3] = 1;
        
        return unitVector;
    }

	static double[] crossProduct(double[] v1, double[] v2) {
		// TODO Auto-generated method stub
		double[] reVec = new double[3];
		reVec[0] = (v2[2]*v1[1] - v1[2]*v2[1]);
		reVec[1] = (v2[0]*v1[2] - v1[0]*v2[2]);
		reVec[2] = (v2[1]*v1[0] - v1[1]*v2[0]);
		return reVec;
	}
	
	public static float[] add(float[]v1, float[]v2){
		float[] result = new float[3];
		result[0] = v1[0] + v2[0];
		result[1] = v1[1] + v2[1];
		result[2] = v1[2] + v2[2];
		return result;	
	}
	
	public static double[] add(double[]v1, double[]v2){
		double[] result = new double[3];
		result[0] = v1[0] + v2[0];
		result[1] = v1[1] + v2[1];
		result[2] = v1[2] + v2[2];
		return result;	
	}
	
	
	
	public static double[] sub(double[] rainPos, double[] d){
		double[] result = new double[3];
		result[0] = rainPos[0] - d[0];
		result[1] = rainPos[1] - d[1];
		result[2] = rainPos[2] - d[2];
		return result;		
	}
	
	
	public static float[] sub(float[]v1, float[]v2){
		float[] result = new float[3];
		result[0] = v1[0] - v2[0];
		result[1] = v1[1] - v2[1];
		result[2] = v1[2] - v2[2];
		return result;
    
}

	public static double[] normalise(double[] vec) {
		// TODO Auto-generated method stub
		double length = (double) Math.sqrt(vec[0]*vec[0] + vec[1]*vec[1] + vec[2]*vec[2]);
		double val[] = {vec[0]/length,vec[1]/length,vec[2]/length};
		return val;
	}
	
	
	public static double[] doScale(double[]v1, double scale){
		double[] val = new double[3];
		val[0] = v1[0] * scale;
		val[1] = v1[1] * scale;
		val[2] = v1[2] * scale;
		return val;	
	}
	
}
