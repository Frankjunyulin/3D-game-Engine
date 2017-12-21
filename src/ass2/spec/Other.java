package ass2.spec;

import java.util.ArrayList;

public class Other {

    private double[] myPos;
    //1: No rotation both way
    //2: Rotation both ways
    //3: Rotate forward
    //4: Rotate back
    private int action; 
    private double rot;
    private boolean forward;
    private ArrayList<double[]> arr;
    private int index;
    
    public Other(double x1, double z1, double x2, double z2, int action) {
    	rot = 0;
        myPos = new double[6];
        myPos[0] = x1;
        myPos[1] = z1;
        myPos[2] = x2;
        myPos[3] = z2;
        index = 0;
        arr = buildArr();
        this.action = action;
        forward = true;
    }
    
    private ArrayList<double[]> buildArr(){
    	ArrayList<double[]> arr = new ArrayList<double[]>();
    	double x = myPos[2]-myPos[0];
    	double z = myPos[3]-myPos[1];
    	double k = z/x;
    	double xx = (0.01)/(Math.sqrt(k*k+1));
    	double zz = Math.abs(k*xx);
    	double xi = myPos[0];
    	double zi = myPos[1];
    	double distance = Math.sqrt(x*x+z*z);
    	double i = 0;
    	while(i<distance){
    		//System.out.println(xi+" "+zi);
    		arr.add(new double[]{xi,zi});
    		if(myPos[0]<myPos[2]){
    			xi+=xx;
    		}else{
    			xi-=xx;
    		}
    		if(Double.isNaN(zz)){
    			if(myPos[1]<myPos[3]){
        			zi+=0.01;
        			//System.out.println("zi nan ++: "+zi);
        		}else if(myPos[1]>myPos[3]){
        			zi-=0.01;
        			//System.out.println("zi nan --: "+zi);
        		}
    		}else{
    			if(myPos[1]<myPos[3]){
        			zi+=zz;
        			//System.out.println("zi++: "+zi);
        		}else if(myPos[1]>myPos[3]){
        			zi-=zz;
        			//System.out.println("zi--: "+zi);
        		}
    		}
    		/*xi+=xx;
    		zi+=zz;*/
    		i+=0.01;
    	}
    	if(myPos[0]==myPos[2]&&myPos[1]==myPos[3]){
    		arr.add(new double[]{myPos[0],myPos[1]});
    		//System.out.println(myPos[0]+""+myPos[1]);
    	}
    	return arr;
    }
    
    public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<double[]> getArr(){
    	return arr;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public int getAction(){
    	return action;
    }
    
    public double getRot(){
    	return rot;
    }

	public void setRot(double rot) {
		this.rot = rot;
	}

	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}
    
	
}