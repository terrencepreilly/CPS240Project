import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Animation implements GameConstants {
	private HashMap<String, BufferedImage> playerImages;
	private HashMap<Integer, Boolean> firstImage;

	public Animation() {
		playerImages = new HashMap<>();

		try {
			playerImages.put("F1", ImageIO.read(new File(IMGFLD + "playerFrwd.png")));
			playerImages.put("F2", ImageIO.read(new File(IMGFLD + "playerFrwd2.png")));
			playerImages.put("S1", ImageIO.read(new File(IMGFLD + "playerSide.png")));
			playerImages.put("S2", ImageIO.read(new File(IMGFLD + "playerSide2.png")));
			playerImages.put("D1", ImageIO.read(new File(IMGFLD + "playerBack.png")));
			playerImages.put("D2", ImageIO.read(new File(IMGFLD + "playerBack2.png")));
			//playerImages.put("AT", ImageIO.read(new File(IMGFLD + "plFrAtk.png")));
//			playerImages.put("DD", ImageIO.read(new File(IMGFLD + "playDead.png")));

			playerImages.put("F1z", ImageIO.read(new File(IMGFLD + "zombieFrwd.png")));
			playerImages.put("F2z", ImageIO.read(new File(IMGFLD + "zombieFrwd2.png")));
			playerImages.put("S1z", ImageIO.read(new File(IMGFLD + "zombieSide.png")));
			playerImages.put("S2z", ImageIO.read(new File(IMGFLD + "zombieSide2.png")));
			playerImages.put("D1z", ImageIO.read(new File(IMGFLD + "zombieBack.png")));
			playerImages.put("D2z", ImageIO.read(new File(IMGFLD + "zombieBack2.png")));
//			playerImages.put("ATz", ImageIO.read(new File(IMGFLD + "zombAtk.png")));
//			playerImages.put("DDz", ImageIO.read(new File(IMGFLD + "zombDead.png")));
		} catch (IOException ioe) { ioe.printStackTrace(); }

		firstImage = new HashMap<>();
	}

	public int getDirection(Character c) {
		int cdir = c.getDirection();
		if (cdir >= 315 || cdir < 45)
			return RIGHT; 
		else if (cdir >= 45 && cdir < 135)
			return DOWN;
		else if (cdir >= 135 && cdir < 225)
			return LEFT;
		else
			return UP;
	}

	public BufferedImage horizontalFlip(BufferedImage img) {
		int width = img.getWidth();
	        int height = img.getHeight();
	        BufferedImage flippedImage = new BufferedImage(width, height, img.getType());
	        Graphics g = flippedImage.createGraphics();
	        g.drawImage(img, 0, 0, width, height, width, 0, 0, height, null);
	        g.dispose();
	        return flippedImage; 
	}

	public void addCharacter(Character c) {
		firstImage.put( c.getUniqueID(), true);
	}

	public BufferedImage getImage(Character c) {
		int dir = getDirection(c);
		String imgname = "";

		if (dir == DOWN)
			imgname = "D";
		else if (dir == UP)
			imgname = "F";
		else 
			imgname = "S";

		imgname += firstImage.get(c.getUniqueID()) ? "1" : "2";
		imgname += c.getType() == ENEMY ? "z" : "";


		if (c.type == PLAYER && System.currentTimeMillis() - c.getBlit() > ANIMATION_UPDATE_SPEED) {
			firstImage.put(c.getUniqueID(), ! firstImage.get(c.getUniqueID()));
			c.setBlit();
		}
		else if (c.type == ENEMY && System.currentTimeMillis() - c.getBlit() > ENEMY_ANIMATION_UPDATE_SPEED) {
			firstImage.put(c.getUniqueID(), ! firstImage.get(c.getUniqueID()));
                        c.setBlit();
		}

		if (dir == LEFT) 
			return horizontalFlip( playerImages.get(imgname) );


		return playerImages.get( imgname );
	}
}
