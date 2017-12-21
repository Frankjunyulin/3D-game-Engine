package ass2.spec;

import java.util.Random;

class Particle {
	float x, y, z,drop;  // position
	boolean active;
	private Random rand;
	
	// Constructor
	public Particle() {			
		init();
		
	}

	public void update() {
		if (y <= 0){
			if (active){
				init();
			}else{
				active = !active;
			}
		}else{
			y -= drop;
		}
		
	}

	public void init() {
		//terrain.size().width*100
		rand = new Random();
		x = ((float)(rand.nextInt(100)))/100-3;
		y = rand.nextFloat()+4;
		z = ((float)(rand.nextInt(100)))/100-3;
		drop = rand.nextFloat();
	}
}