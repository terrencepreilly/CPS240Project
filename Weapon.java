import java.awt.image.BufferedImage;

/*
 * The Weapon interface creates a contract for all weapons. Weapons must have a method to get damage, and getImage.
 * May create a more refined interface later, but for now this works and is all that is necessary.
 */

public interface Weapon {
	public int getDamage();
	public BufferedImage getImage();
}
