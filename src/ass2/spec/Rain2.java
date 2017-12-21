package ass2.spec;


import java.util.Random;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Rain2{
	private static final int MAX_PARTICLES = 50000; // max number of particles
	private Particle[] particles = new Particle[MAX_PARTICLES];
	// Global speed for all the particles
	private static float z = -40.0f; //zOffset
	private static float y = 5.0f;   //yOffset
	MyTexture myTexture;
	double tranx, trany;
	
	private Terrain terrain;
	private boolean on;
	public Rain2(double tranx, double trany,Terrain terrain, MyTexture myTexture){
		// Initialize the particles
		this.myTexture = myTexture;
		this.terrain = terrain;
		this.on = false;
		for (int i = 0; i < MAX_PARTICLES; i++) {
			particles[i] = new Particle(this);
		}
	}
	public void display(GL2 gl) {
		gl.glPushMatrix();
		 //gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	     
		gl.glActiveTexture(gl.GL_TEXTURE1);
		//myTexture = new MyTexture(gl, "star.bmp", "bmp", false);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glDepthMask( false );
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		// Render the particles
		for (int i = 0; i < MAX_PARTICLES; i++) {
			if (particles[i].active){
				// Draw the particle using our RGB values, fade the particle based on it's life

				gl.glColor4f(0.5f,0.75f, 0.75f, 0.3f);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId()); 
				gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
				float[] color = {0.5f,0.75f,0.75f,0.3f};

				gl.glBegin(GL2.GL_TRIANGLE_STRIP); // build quad from a triangle strip

				double px = particles[i].x;
				double py = particles[i].y;
				double pz = particles[i].z;
				//double[] ps1 = {px + 0.0125f, py + 0.0125f, pz};
				//double[] ps2 = {px - 0.0125f, py + 0.0125f, pz};
				//double[] ps3 = {px + 0.0125f, py - 0.0125f, pz};
				//double[] ps4 = {px - 0.0125f, py - 0.0125f, pz};
				double[] rainPos = {px,py,pz};
				double[] eye = {tranx+1.5,terrain.altitude(tranx, trany)+0.5,trany+1.5};
				double[] upVec = {0d,1d,0d};
				double[] dc = MathUtil.sub(rainPos,eye);
				double[] ca = MathUtil.normalise( MathUtil.crossProduct(dc, upVec));
				double[] cb = MathUtil.normalise(MathUtil.crossProduct(dc, ca));
				double[] p1 = MathUtil.sub(rainPos, MathUtil.doScale(MathUtil.add(ca,cb),0.01d));
				double[] p2 = MathUtil.add(rainPos, MathUtil.doScale(MathUtil.sub(ca,cb),0.01d));
				double[] p3 = MathUtil.add(rainPos, MathUtil.doScale(MathUtil.add(ca,cb),0.01d));
				double[] p4 = MathUtil.sub(rainPos, MathUtil.doScale(MathUtil.sub(ca,cb),0.01d));
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE1,1, 1);
	            gl.glVertex3d(p1[0],p1[1],p1[2]);
	            gl.glMultiTexCoord2d(GL2.GL_TEXTURE1,0, 1);
	            gl.glVertex3d(p2[0],p2[1],p2[2]);
	            gl.glMultiTexCoord2d(GL2.GL_TEXTURE1,1, 0);
	            gl.glVertex3d(p4[0],p4[1],p4[2]);
	            gl.glMultiTexCoord2d(GL2.GL_TEXTURE1,0, 0);
	            gl.glVertex3d(p3[0],p3[1],p3[2]);
	            gl.glEnd();
	            particles[i].update(this);
			}
		}
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glPopMatrix();
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glDisable(GL2.GL_BLEND);
		gl.glDepthMask( true );
	}

	// Particle (inner class)
	class Particle {
		float x, y, z,drop;  // position
		boolean active;
		private Random rand = new Random();

		// Constructor
		public Particle(Rain2 rain2) {			
			init();
			active = rain2.on;
		}

		public void update(Rain2 rain2) {
			if (y <= 0){
				if (rain2.on){
					init();
				}else{
					active = !active;
				}
			}else{
				y -= drop;
			}
			
		}

		public void init() {
			x = ((float)(rand.nextInt(terrain.size().width*100)))/100-terrain.X_OFFSET;
			y = rand.nextFloat()+4;
			z = ((float)(rand.nextInt(terrain.size().height*100)))/100-terrain.Z_OFFSET;
			drop = rand.nextFloat();
		}
	}
	
	public void rainChange() {
		on = !on;
		for (int i = 0; i < MAX_PARTICLES; i++) {
			if (!particles[i].active){
				particles[i] = new Particle(this);
			}
		}
	}

}
