package ass2.spec;

public class Pond {

    private double[] myPos;
    private double size;
    private int counter;
    
    public Pond(double x, double y, double z, double size) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        this.size = size;
        counter = 0;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public double getSize(){
    	return size;
    }

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
    
    
}
