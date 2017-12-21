package ass2.spec;

public class Beast {
	private double[] myPos;
	
	 public Beast(double x, double y, double z) {
	        myPos = new double[3];
	        myPos[0] = x;
	        myPos[1] = y;
	        myPos[2] = z;
	    }

	public double[] getMyPos() {
		return myPos;
	}

	public void setMyPos(double[] myPos) {
		this.myPos = myPos;
	}
	
	
}
