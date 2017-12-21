package ass2.spec;

import com.jogamp.opengl.util.texture.Texture;

public class texture {
	private Texture terrain;
	  private Texture treeTrunk;
	  private Texture treeLeaves;
	  private Texture road;
	  private Texture avatar;

	  
	  public Texture getTerrain() {
	    return terrain;
	  }
	  
	  public void setTerrain(Texture terrain) {
	    this.terrain = terrain;
	  }
	  
	  public Texture getTreeTrunk() {
	    return treeTrunk;
	  }
	  
	  public void setTreeTrunk(Texture treeTrunk) {
	    this.treeTrunk = treeTrunk;
	  }
	  
	  public Texture getTreeLeaves() {
	    return treeLeaves;
	  }
	  
	  public void setTreeLeaves(Texture treeLeaves) {
	    this.treeLeaves = treeLeaves;
	  }
	  
	  public Texture getRoad() {
	    return road;
	  }
	  
	  public void setRoad(Texture road) {
	    this.road = road;
	  }
	  
	  public Texture getAvatar() {
	    return avatar;
	  }
	  
	  public void setAvatar(Texture avatar) {
	    this.avatar = avatar;
	  }
	  
}
