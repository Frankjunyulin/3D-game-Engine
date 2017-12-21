package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

	



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener,MouseMotionListener{

    private Terrain myTerrain;
    private Rain2 rain;
    private double s = 1;
    private double tranx = 0.05;
    private double trany = 0.05;
    private double npcx = 1.7;
    private double npcy = 4.5;
    private double npcr = 0;
    private double avaRotate = 0;
    private Boolean down = false;
    private double cameraX;
    private double cameraY;
    private double rot = 0;
    private double lastRot = 0;
    private double r = 0;
    private MyTexture[] myTextures;	
    boolean night;
    boolean avatarMode;
    boolean rainMode;
    Particle[] particles;
    int MAX_PARTICLES = 50000;

    private static float gravityY = -0.0008f; // gravity

    // Global speed for all the particles
    private static float speedYGlobal = 0.1f;
    private static float z = -40.0f; //zOffset
    private static float y = -5.0f;   //yOffset
    private float wiggle = 0;
    private static boolean enabledBurst = false;
    //ParticleSystem parSys;
    ////
    private String textureFileName1 = "src/ass2/spec/grass.bmp";
    private String textureFileName2 = "src/ass2/spec/tree_trunk.jpg";
    private String textureFileName3 = "src/ass2/spec/tree_leaves.jpg";
    private String textureFileName4 = "src/ass2/spec/asphalt.png";
    private String textureFileName5 = "src/ass2/spec/crack2.jpg";
    private String textureFileName6 = "src/ass2/spec/water_.bmp";
    private String textureFileName7 = "src/ass2/spec/star.bmp";
    private String textureExt1 = "bmp";
    private String textureExt2 = "jpg";
    private String textureExt3 = "jpg";
    private String textureExt4 = "png";
    private String textureExt5 = "jpg";
    private String textureExt6 = "bmp";
    private String textureExt7 = "bmp";
    ////
    
    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        night = false;
        avatarMode = false;
        rainMode = false;
        
        
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);  
          
          Game s = new Game(myTerrain);
          
          // add a GL Event listener to handle rendering
          panel.addGLEventListener(s);
          panel.addKeyListener(s);
          panel.addMouseMotionListener(s);
          panel.setFocusable(true); 
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();

    	//Set Black Background
		///gl.glClearColor(0f, 0f, 0f, 1);
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	//gl.glColor3f(1,1,1); 
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);
    	
    	setCamera(gl);
    	
    	//setLighting(gl);
        if(night)
        	setTorch(gl);
        else
        	setLighting(gl);
        
  	
       	//gl.glScaled(0.1,0.1,0.1);
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	//System.out.println(s);
    	gl.glPushMatrix();     	
  	
       	drawTerrain(gl);
    	
       	drawTree(gl);
       	//drawRoad(gl);
       	drawRoad2(gl);
       	//drawRain(gl);
       	if(avatarMode){
       		drawAvatar(gl);
       	}
       	drawNPC(gl);
       	
       
       	if(rainMode){
       		//drawRain(gl);
       		rain.display(gl);
       		//parSys.display(gl);
       		
       	}
       	drawPond(gl);
       	gl.glPopMatrix();
		
	}
	
	private void setCamera(GL2 gl) {		
		// TODO Auto-generated method stub
        GLU glu = new GLU();
        double cameraZ = 2.0;
        if(avatarMode){
        	cameraZ = 3.0;
        }    
        // Position the camera for viewing.
        glu.gluLookAt(0, 0, cameraZ, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
		
		gl.glRotated(180,1,0,0);
		gl.glTranslated(0, 0, -2.0);
		gl.glRotated(rot,0,1,0);
		gl.glTranslated(0, 0, 2.0);
		gl.glRotated(-45,0,1,0);
		//double al = myTerrain.altitude(s, s);
		//gl.glTranslated(-s-1.225,-al-0.1,-s-1.225);
		//System.out.println("s: "+s+"\nal: "+al+"r: "+rot);
		double al = myTerrain.altitude(tranx, trany);
		gl.glTranslated(-tranx-1.5,-al-0.5,-trany-1.5);
		//System.out.println("tranx: "+tranx+"trany: "+trany+"\nal: "+al+"r: "+rot);
		//gl.glScaled(s, s, s);

				
		/*old code	
        GLU glu = new GLU();
        // Position the camera for viewing.
        glu.gluLookAt(-1.0, -1, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
		
		gl.glRotated(180,1,0,0);
		gl.glRotated(-20,0,1,0);
		gl.glRotated(r,0,1,0);
		gl.glScaled(s, s, s);
		 */		
	}
	
	public void setLighting(GL2 gl){
		gl.glPushMatrix();
		gl.glDisable(GL2.GL_LIGHT2);
		 //gl.glEnable(GL2.GL_LIGHT1);
		if(!night)
			gl.glClearColor(0f, 90f, 200f, 1.0f); 
		//System.out.println(night);
		
		gl.glEnable(GL2.GL_LIGHT1);
	    float[] Ambient = {1.0f, 1.0f, 1.0f, 1.0f};
	    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, Ambient, 0);
	    float[] sunlightVec = myTerrain.getSunlight();
	    float[] SunlightVectorE = new float[4];
	    
	    SunlightVectorE[0] = sunlightVec[0];
	    SunlightVectorE[1] = sunlightVec[1];
	    SunlightVectorE[2] = sunlightVec[2];
	    SunlightVectorE[3] = 0; 
	    
	    float[] diffuse = new float[]{1.0f, 1.0f, 1.0f, 1.0f}; //diffuse all light
	    
	    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, SunlightVectorE, 0);
	    //gl.glEnable(GL2.GL_LIGHT1);
	
	
		gl.glPopMatrix();
	}
	
	private void setTorch(GL2 gl) {
		gl.glPushMatrix();
		    
	   //Disable sun in night mode
		gl.glDisable(GL2.GL_LIGHT1);
		    
	    //Enable torch light
	    gl.glEnable(GL2.GL_LIGHT2);
		  
	    //Background colour
	    gl.glClearColor(0.0f, 0.09408f, 0.2820f, 1.0f); //Night sky, storm petrel
		    
	    //Global Ambient light
	    float[] globalAmb = {0.04f, 0.04f, 0.04f, 1.0f}; //very low for night mode
	    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globalAmb, 0);
		    
	    // Light property vectors.
	    float lightAmb[] = {0.04f, 0.04f, 0.04f, 1.0f};
	    float lightDifAndSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};
		  
	    // Light properties.
	    gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, lightDifAndSpec, 0);
	    gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, lightDifAndSpec, 0);
	    gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, lightAmb, 0);
	    
	    
	    //Set torch position to camera position
	    
	    float[] torchPos = {(float) tranx, (float) ((float)myTerrain.altitude(tranx, trany))+0.5f, (float) trany,  1.0f};
	    gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, torchPos, 0);
		    
		    //Set torch direction (facing outwards from avatar)
	    float[] direction = {(float)Math.cos(Math.toRadians(rot+45)), 0.0f, (float)Math.sin(Math.toRadians(rot+45))};
	    gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, direction, 0); //direction vector
		  
	   //Set cut off and attenuation
	    gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, 45.0f); //cutoff angle
	    gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_EXPONENT, 0.0f); //attenuation
		    
	    gl.glPopMatrix();
	}
	
	

	public void drawTerrain(GL2 gl){  
		gl.glPushMatrix();
	    double[] v1 = new double[3];
		v1[0] = 1;
		v1[2] = -1;
		double[] v2 = new double[3];
       	
        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        
		//gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
        ///////////
	    float[] ambient = {0.22f, 0.26f, 0.22f, 1.0f};
	    float[] diffuse = {0.24f, 0.62f, 0.32f, 1.0f};
	    float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};
	  
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
               
        ////////////
        double[] v1Texture = {-1, -1};
        double[] v2Texture = {1, -1};
        double[] v3Texture = {-1, 1};        
        double[] v4Texture = {-1, 1};
        double[] v5Texture = {1, -1};
        double[] v6Texture = {1, 1};
	    ///////
        //gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glEnable(GL2.GL_TEXTURE_2D);
	    gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
	    
    		//draw terrain
    		for(int i = 0; i< myTerrain.size().getWidth()-1;i++){
    			for(int j = 0; j< myTerrain.size().getHeight()-1;j++){
    				   				
    	    		double[] Paltitude = new double[]{ myTerrain.getGridAltitude(i, j),myTerrain.getGridAltitude(i, j+1),
    	    				myTerrain.getGridAltitude(i+1, j),	myTerrain.getGridAltitude(i+1, j+1),};
    		
    	    		gl.glBegin(GL.GL_TRIANGLES);{		    		

    	    			v1[1] = Paltitude[2] - Paltitude[1];		    		
    	    			v2[0] = 0;
    	    			v2[2] = -1;
    	    			v2[1] = Paltitude[0] - Paltitude[1];
		    		
    	    			
    	    			
    	    			
    	    			double[] normal = crossProduct(v1,v2);
   			
    	    			gl.glNormal3d(normal[0], normal[1], normal[2]);
    	    			gl.glTexCoord2dv(v1Texture, 0);
    	    			gl.glVertex3d(i,Paltitude[0],j); // P0
    	    			gl.glTexCoord2dv(v2Texture, 0);
    	    			gl.glVertex3d(i,Paltitude[1],j+1);// P1
    	    			gl.glTexCoord2dv(v3Texture, 0);
        	    		gl.glVertex3d(i+1,Paltitude[2],j); // P2

    		    		normal = crossProduct(v2,v1);
    		    		//gl.glNormal3d(n1[0], n1[1], n1[2]);
        	    		
    		    		gl.glTexCoord2dv(v4Texture, 0);
        	    		gl.glVertex3d(i,Paltitude[1],j+1);// P1
        	    		gl.glTexCoord2dv(v5Texture, 0);
        	    		gl.glVertex3d(i+1,Paltitude[3],j+1); // P3
        	    		gl.glTexCoord2dv(v6Texture, 0);
        	    		gl.glVertex3d(i+1,Paltitude[2],j); // P2
        	    		        	    		
    	    		}gl.glEnd();
    			}
    			 
    		}
    		//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    		gl.glPopMatrix();
    		gl.glDisable(GL2.GL_TEXTURE_2D);
	}
	
	public void drawTree(GL2 gl){
		//MyTexture trunkTextures =new MyTexture(gl,textureFileName2,textureExt2,true);
		//Tree tree = myTerrain.trees().get(0);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		   //gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());   
		GLU glu = new GLU(); 
		//System.out.println("YYYY");
		List<Tree> treeList = myTerrain.trees();
		for(Tree tree: treeList)
		{
			gl.glPushMatrix();
			{float[] ambient = {0.22f, 0.24f, 0.24f, 1.0f};
		      float[] diffuse = {0.32f, 0.1f, 0.0f, 1.0f};
		      float[] specular = {0.52f, 0.52f, 0.52f, 1.0f};
		  
		      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		  
		      //Get texture

		      if(myTextures[1]!=null){
		    	  gl.glEnable(GL2.GL_TEXTURE_2D);
		    	  gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
		    	  
		      }
		    	  //Texture treeTrunk = myTextures[1];
		      
				//System.out.println(tree.getPosition()[0]+ " " + tree.getPosition()[2]);
				//System.out.println("Altitude: " + myTerrain.altitude(tree.getPosition()[0], tree.getPosition()[2]));
				//System.out.println("TreePos[1]: " + tree.getPosition()[1]);
				gl.glTranslated(tree.getPosition()[0], tree.getPosition()[1] - 0.000009, tree.getPosition()[2]);
				gl.glRotated(-90.0, 1, 0, 0);
				GLUquadric gluQuadratic = glu.gluNewQuadric();
				glu.gluQuadricTexture(gluQuadratic, true);
				glu.gluQuadricNormals(gluQuadratic, GLU.GLU_SMOOTH);
				glu.gluCylinder(gluQuadratic, 0.025f, 0.025f, 0.4f, 25, 25);
			}
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glPopMatrix();
	  
			gl.glPushMatrix();
			{
				
				 if(myTextures[2]!=null){
			    	  gl.glEnable(GL2.GL_TEXTURE_2D);
			    	  gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[2].getTextureId());
			    	  
			      }
				
				  float[] ambient = {0.32f, 0.44f, 0.32f, 1.0f};
			      float[] diffuse = {0.0f, 0.54f, 0.0f, 0.54f};
			      float[] specular = {0.52f, 0.52f, 0.52f, 0.74f};
			  
			      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
			      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
			
			
				gl.glTranslated(tree.getPosition()[0], tree.getPosition()[1] + (0.4f - 0.00009), tree.getPosition()[2]);
				GLUquadric gluQuadratic = glu.gluNewQuadric();
				glu.gluQuadricTexture(gluQuadratic, true);
				glu.gluQuadricNormals(gluQuadratic, GLU.GLU_SMOOTH);
				glu.gluSphere(gluQuadratic, 0.15f, 40, 40);
			}
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glPopMatrix();
		}	    
	    //
		//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}
	
	public void drawRoad2(GL2 gl) {
		float[] ambient = {0.2f, 0.24f, 0.24f, 1.0f};
	      float[] diffuse = {0.32f, 0.1f, 0.0f, 1.0f};
	      float[] specular = {0.52f, 0.52f, 0.52f, 1.0f};
	  
	      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
	      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
	      gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
	      
		List<Road> roads = myTerrain.roads();
        for(Road road : roads) {
			 if(myTextures[3]!=null){
		    	  gl.glEnable(GL2.GL_TEXTURE_2D);
		    	  gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[3].getTextureId());
		    	  //System.out.println("caocaocaocaocaocoao");;
		      }
            road.drawRoad(gl,myTerrain);
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }
	}
	
	
	
	

	public void drawAvatar(GL2 gl){
		gl.glPushMatrix();
		gl.glTranslated(tranx, myTerrain.altitude(tranx, trany)+0.25, trany);
		gl.glRotated(-rot, 0, 1, 0);
		gl.glRotated(-50, 0, 1, 0);
		GLU glu = new GLU(); 
		//gl.glColor3f(1.0f, 1.0f, 1.0f);
		float[] ambient2 = {255.3f, 255.0f, 255.24f, 1.0f};
	    float[] diffuse2 = {255.32f, 255.1f, 255.0f, 1.0f};
	    float[] specular2 = {255.52f, 255.52f, 255.52f, 1.0f};
	  
	     gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient2, 0);
	     gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse2, 0);
	     gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular2, 0);;	
		
		GLUT avatar = new GLUT();
		gl.glRotated(avaRotate, 0, 0, 1);
		
	      if(myTextures[4]!=null){
	    	  gl.glEnable(GL2.GL_TEXTURE_2D);
	    	  gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[4].getTextureId());
	    	  
	      }
		GLUquadric gluQuadratic = glu.gluNewQuadric();
		glu.gluQuadricTexture(gluQuadratic, true);
		glu.gluQuadricNormals(gluQuadratic, GLU.GLU_FLAT);
		glu.gluSphere(gluQuadratic, 0.25f, 40, 40);
		
        gl.glFrontFace(GL2.GL_CW);
        //avatar.glutSolidTeapot(0.4);
        
        gl.glFrontFace(GL2.GL_CCW);
		
        gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glPopMatrix();
		
	}
	
	public void drawNPC(GL2 gl){
		/*gl.glPushMatrix();
		gl.glTranslated(npcx, myTerrain.altitude(npcx,npcz)+0.28, npcz);
		gl.glRotated(npcr, 0, 0, 1);
		if(down){
			npcx-=0.05;
			npcr+=20;
		}else{
			npcx+=0.05;
			npcr=0;
		}
		if(npcx>3.7){
			down=true;
		}else if(npcx<1.7){
			down=false;
		}
		
		GLUT avatar = new GLUT();
		
        gl.glFrontFace(GL2.GL_CW);
        avatar.glutSolidTeapot(0.4);
        gl.glFrontFace(GL2.GL_CCW);
		
		
		gl.glPopMatrix();*/

		List<Other> otherList = myTerrain.others();
		for(Other other: otherList){
			gl.glPushMatrix();
			
		    float[] ambient = {0.22f, 0.26f, 0.22f, 1.0f};
		    float[] diffuse = {0.24f, 0.62f, 0.32f, 1.0f};
		    float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};
		  
		    if(night) {

		    }
		    
		    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		    
			double x = other.getArr().get(other.getIndex())[0];
			double z = other.getArr().get(other.getIndex())[1];
			gl.glTranslated(x, myTerrain.altitude(x,z)+0.28, z);
			//System.out.println("x: "+x+" z: "+z);
			double rot = other.getRot();
			gl.glRotated(rot, 0, 0, 1);
			if((other.isForward())&&other.getArr().size()>1){
				//x-=0.01;
				//other.setCurrPos(new double[]{x,other.getCurrPosition()[1]});
				other.setIndex(other.getIndex()+1);
				//System.out.println("backward: "+other.getIndex());
				if(other.getAction()==2||other.getAction()==3){
					rot-=5.4;
					other.setRot(rot);
				}else{
					other.setRot(0);
				}
			}else if((!other.isForward())&&other.getArr().size()>1){
				/*npcx+=0.05;
				npcr=0;*/
				//x+=0.01;
				//other.setCurrPos(new double[]{x,other.getCurrPosition()[1]});
				other.setIndex(other.getIndex()-1);
				//System.out.println("forward: "+other.getIndex());
				if(other.getAction()==2||other.getAction()==4){
					rot+=5.4;
					other.setRot(rot);
				}else{
					other.setRot(0);
				}
			}
			/*if(x>other.getPosition()[2]){
				//down=true;
				other.setForward(false);
			}else if(x<other.getPosition()[0]){
				//down=false;
				other.setForward(true);
			}*/
			if(other.getIndex()==0){
				other.setForward(true);
			}else if(other.getIndex()==other.getArr().size()-1){
				other.setForward(false);
			}
			
			
			GLUT avatar = new GLUT();
			//drawRain(gl);
	        gl.glFrontFace(GL2.GL_CW);
	        avatar.glutSolidTeapot(0.4);
	        gl.glFrontFace(GL2.GL_CCW);
			
			
			gl.glPopMatrix();
		}
		
		
	}
	
	public void drawPond(GL2 gl){
		List<Pond> ponds = myTerrain.ponds();
		double above = 0.01;
		for(Pond pond:ponds){
			//gl.glPushMatrix();
			double x = pond.getPosition()[0];
			double z = pond.getPosition()[2];
			double y = myTerrain.altitude(x,z)+above;
			gl.glTranslated(x, 0, z);
			
			/*GLUT avatar = new GLUT();
			
	        gl.glFrontFace(GL2.GL_CW);
	        avatar.glutSolidTeapot(0.4);
	        gl.glFrontFace(GL2.GL_CCW);*/
			//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
			
			if(myTextures[5]!=null){
				gl.glColor3f(1, 1, 1);
				float f = 0.9f;
				float[] ambient = {f, f, f, 1.0f};
			    float[] diffuse = {f, f, f, 1.0f};
			    float[] specular = {f, f, f, 1.0f};
			  
			    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
			    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
				gl.glEnable(GL2.GL_TEXTURE_2D);
			    gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[5].getTextureId());
			}
			gl.glBegin(GL2.GL_POLYGON); 
			int i = 0;
			double r = pond.getSize()/2;
			int slice = 30;
			if(pond.getCounter()==60){
	        	pond.setCounter(0);
	        }
	        double[] tt = new double[2];
	        if(pond.getCounter()>45){
	        	tt[0] = 1;
	        	tt[1] = 0;
	        	pond.setCounter(pond.getCounter()+1);
	        }else if(pond.getCounter()>30){
	        	tt[0] = 1;
	        	tt[1] = 1;
	        	pond.setCounter(pond.getCounter()+1);
	        }else if(pond.getCounter()>15){
	        	tt[0] = 0;
	        	tt[1] = 1;
	        	pond.setCounter(pond.getCounter()+1);
	        }else if(pond.getCounter()>=0){
	        	tt[0] = 0;
	        	tt[1] = 0;
	        	pond.setCounter(pond.getCounter()+1);
	        }
		    for (; i < slice; i++) {  
		        gl.glVertex3d(r*Math.sin(2*Math.PI/slice*i), y, r*Math.cos(2*Math.PI/slice*i));
		        //gl.glTexCoord2d(r+r*Math.sin(2*Math.PI/slice*i),r+r*Math.cos(2*Math.PI/slice*i));
		        gl.glTexCoord2d(tt[0],tt[1]);
		        //gl.glTexCoord2d(0,0);
		    }  
		    
		    gl.glEnd();
		    gl.glTranslated(-x, 0, -z);
		    gl.glDisable(GL2.GL_TEXTURE_2D);
			//gl.glPopMatrix();
		}
	}
	
	public void drawRain(GL2 gl){

		gl.glPushMatrix();
		 //gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	     
		//gl.glActiveTexture(GL2.GL_TEXTURE7);
		//myTexture = new MyTexture(gl, "rain.bmp", "bmp", false);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glDepthMask( false );
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);		
		

		// Render the particles
	     for (int i = 0; i < MAX_PARTICLES; i++) {
	         if (particles[i].active) {
	            // Draw the particle using our RGB values
	        	
	            gl.glColor4f(particles[i].r, particles[i].g, particles[i].b, particles[i].life);
	         
	         
	            
	            gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId()); 
	        
	            
	            gl.glBegin(GL2.GL_QUADS);

	            float px = (float) (particles[i].offset*Math.cos(particles[i].angle))
	                      + (float) Math.sin(particles[i].y/5 + wiggle)*3;
	            float py = particles[i].y + y;
	            float pz = (float) (particles[i].offset*Math.sin(particles[i].angle)) + z
	                      + + (float) Math.cos(particles[i].y/5 + wiggle)*3;;

	            gl.glTexCoord2d(1, 1);
	            gl.glVertex3f(px + 0.5f, py + 0.5f, pz); // Top Right
	            gl.glTexCoord2d(0, 1);
	            gl.glVertex3f(px - 0.5f, py + 0.5f, pz); // Top Left
	            gl.glTexCoord2d(0, 0);
	            gl.glVertex3f(px - 0.5f, py - 0.5f, pz); // Bottom Left
	            gl.glTexCoord2d(1, 0);
	            gl.glVertex3f(px + 0.5f, py - 0.5f, pz); // Bottom Right
	            gl.glEnd();

	            // Move the particle
	            particles[i].y += particles[i].speedY;
	            particles[i].angle += particles[i].angularSpeed;
	            particles[i].offset += particles[i].offsetSpeed;
	            
	            wiggle += 0.00003;
	            
	            if (particles[i].y >= 20) {
	                particles[i].y = 0;
	                particles[i].offset = 0;
	            }
	            
	            // Apply the gravity force on y-axis
//	            particles[i].speedY += gravityY;
	            
	            // Slowly kill it
	            //particles[i].life -= 0.002;
	            
	            if (enabledBurst) {
	               particles[i].burst();
	            }
	         }
	      }
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glPopMatrix();
		//gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glDisable(GL2.GL_BLEND);
		gl.glDepthMask( true );
	}
	
	
	class Particle {
	      boolean active; // always active in this program
	      float life;     // how alive it is
	      float r, g, b;  // color
	      float y;  // position
	      float angle;
	      float offset;
	      float speedY; // speed in the direction
	      float angularSpeed;
	      float offsetSpeed;

	      private final float[][] colors = {    // rainbow of 12 colors
	            { 1.0f, 0.5f, 0f }, { 1.0f, 0.75f, 0 }, { 1.0f, 1.0f, 0f },
	            { 0.5f, 0.5f, 0f },
	            { 0.75f, 0.5f, 0f } };

	      private Random rand = new Random();

	      // Constructor
	      public Particle() {
	         active = true;
	         burst();
	      }

	      public void burst() {
	         // Set the initial position
	         y = rand.nextFloat()*20;
	         
	         // Generate a random speed and direction in polar coordinate, then resolve
	         // them into x and y.
	         angle = (float)Math.toRadians(rand.nextInt(360));

	         speedY = speedYGlobal;
	         angularSpeed = (float) ((float) (1.0/360)*(2*Math.PI));
	         
	         offset = y/3;
	         offsetSpeed = speedY / 3;

	         int colorIndex = rand.nextInt(colors.length);
	         // Pick a random color
	         r = colors[colorIndex][0];
	         g = colors[colorIndex][1];
	         b = colors[colorIndex][2];
	         
	         // Initially it's fully alive
	         life = 1.0f;
	      }
	   }

	
	
	
	private void glTexParameteri(int glTexture2d, int glTextureWrapS, int glRepeat) {
		// TODO Auto-generated method stub
		
	}

	private double[] crossProduct(double[] v1, double[] v2) {
		// TODO Auto-generated method stub
		double[] reVec = new double[3];
		reVec[0] = (v2[2]*v1[1] - v1[2]*v2[1]);
		reVec[1] = (v2[0]*v1[2] - v1[0]*v2[2]);
		reVec[2] = (v2[1]*v1[0] - v1[1]*v2[0]);
		return reVec;
	}
		

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		//Rain rain = new Rain();
		//parSys = new ParticleSystem();
		
		GL2 gl = drawable.getGL().getGL2();
	    //gl.glClearColor(0f, 0f, 0f, 1);
	    gl.glEnable(GL2.GL_DEPTH_TEST);
	    gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
	    gl.glEnable(GL2.GL_LIGHTING);
	   //Turn on texture
	    
        //gl.glEnable(GL2.GL_TEXTURE_2D);	    
	    //Load or create textures
	    myTextures = new MyTexture[7];
	    //myTextures2 = new MyTexture[1];
	    myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);
	    //Set Texture
	    	gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); //use trilinear filtering with MIN filter
    	
         //trunkTextures = new MyTexture[1];
        myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,true);
         //myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,true);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); //use trilinear filtering with MIN filter
    	//myTextures[1] = new MyTexture(gl,textureFileName2,textureExt2,true);
    	
         
        myTextures[2] = new MyTexture(gl,textureFileName3,textureExt3,true);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); //use trilinear filtering with MIN filter
         
         
        myTextures[3] = new MyTexture(gl,textureFileName4,textureExt4,true);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
         
    	//gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
        myTextures[4] = new MyTexture(gl,textureFileName5,textureExt5,true);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); 
        
        myTextures[5] = new MyTexture(gl,textureFileName6,textureExt6,true);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); 

        myTextures[6] = new MyTexture(gl,textureFileName7,textureExt7,true);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); 
        
		particles = new Particle[50000];
		
	      for (int i = 0; i < MAX_PARTICLES; i++) {
	          particles[i] = new Particle();
	       }
	      
	      rain = new Rain2(tranx,trany,myTerrain,myTextures[6]);
        
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		double aspectRadio = ((double)width)/((double)height);
	   	 GL2 gl = drawable.getGL().getGL2();
	     gl.glMatrixMode(GL2.GL_PROJECTION);
	     gl.glLoadIdentity();  
	     GLU glu = new GLU();
	    
	     glu.gluPerspective(300, aspectRadio, 0.1, 100);
	     //glu.gluPerspective(300, aspectRadio, 0.1, 100);
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("ccc");
		
		double dist,newRot,x1,y1,deg,side,x2,y2;
		if (e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_DOWN) {
			dist = 0.05;
			 newRot=rot-lastRot+45;
			 x1 = Math.cos(lastRot/180*Math.PI)*dist;
			 y1 = Math.sin(lastRot/180*Math.PI)*dist;
			 deg = (180-(180-newRot))/2+lastRot;
			 side = Math.sqrt(dist*dist*2-2*dist*dist*Math.cos((180-newRot)/180*Math.PI));
			 x2 = Math.cos(deg/180*Math.PI)*side;
			 y2 = Math.sin(deg/180*Math.PI)*side;
			 if(e.getKeyCode()==KeyEvent.VK_UP){
				tranx += (x2 - x1); 
				trany += (y2 - y1);
				avaRotate -= 10;
			 }else if(e.getKeyCode()==KeyEvent.VK_DOWN){
				tranx -= (x2 - x1);
				trany -= (y2 - y1);
				avaRotate += 10;
			 }
			 if(tranx<0){
				 tranx = 0.01;
			 }
			 if(tranx>myTerrain.size().width-1){
				 tranx=myTerrain.size().width-1.01;
			 }
			 if(trany<0){
				 trany = 0.01;
			 }
			 if(trany>myTerrain.size().height-1){
				 trany=myTerrain.size().height-1.01;
			 }
			 lastRot = rot;
			 //System.out.println("tranx: "+tranx+"trany: "+trany);
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			rot+=2;
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			rot-=2;
		}
	
		
		switch (e.getKeyCode()) {
		/*
		 case KeyEvent.VK_UP:
			 s+= 0.05;
			 break;
		 case KeyEvent.VK_DOWN:		 
			 s-= 0.05;
			 break;
		 case KeyEvent.VK_RIGHT:
			 r+=2;
			 break;
		 case KeyEvent.VK_LEFT:
			 r-=2;
			 break;
		*/
		 case KeyEvent.VK_N:
			 night = !night;
			 break;
		 case KeyEvent.VK_T:
			 avatarMode = !avatarMode;
			 break;
		 case KeyEvent.VK_R:
			 rainMode = !rainMode;
			 rain.rainChange();
			 break;
		 default:
			 break;
		 }		
		
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}