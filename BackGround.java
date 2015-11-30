import java.awt.image.BufferedImage;

public class BackGround extends Actor{

	private BoxCollider boundary = null;
	
	public BackGround(BufferedImage image, Vector location){
		super(image, location);
	}
	
	protected void setBoxCollider(BufferedImage image){
		boundary = new BoxCollider(image);
	}
	public BoxCollider getBoxCollider(){
		return boundary;
	}
}
