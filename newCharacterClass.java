package javagames.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 * This class is like the character class, but it can store the characterImage AND the graphics image.
 * Basically this class can draw itself when passed the correct graphics object being used to render the screen.
 * This could make it easy to do calls to draw characters on the screen. Requiring that we only have to set the new location
 * when it is changed! Just an idea for now, but this can simplify some method calls for now.
 */
 
 /*
  * The Vector2f class simply takes two double arguments for the x and y coordinates of something. It has 2 methods:
  * getX() {return x} and getY() {return y}, and a constructor which takes x and y. Pretty simple.
  */
public class newCharacterClass {

	private Graphics g = null;
	private BufferedImage characterImage;
	private int health = 0;
	private Vector2f location;
	
	//FULL-ARG CONSTRUTOR
	public drawCharacter(BufferedImage characterImage, Vector2f location, Graphics g){
		this.characterImage = characterImage;
		this.location = location;
		this.g = g;
	}
	//SINGLE ARG CONSTRUCTORS
	public drawCharacter(BufferedImage characterImage){
		this(characterImage, null, null);
	}
	public drawCharacter(Vector2f location){
		this(null, location, null);
	}
	public drawCharacter(Graphics g){
		this(null, null, g);
	}
	
	//SET METHODS
	public void setGraphics(Graphics g){
		this.g = g;
	}
	public void setLocation(float x, float y){
		this.location = new Vector2f(x, y);
	}
	public void setVLocation(Vector2f location){
		this.location = location;
	}
	public void setImage(BufferedImage characterImage){
		this.characterImage = characterImage;
	}
	public void setHealth(int health){
		this.health = health;
	}
	//GET METHODS
	public Graphics getGraphics(){
		return g;
	}
	public Vector2f getVLocation(){
		return location;
	}
	public BufferedImage getImage(){
		return characterImage;
	}
	public int getHealth(){
		return health;
	}
	//No need to get individual coorindate, the x and y values in Vector2f are public, and it has get methods anyway
	//just use like this: double xCoord = characterName.getVLocation().getX(); if you really need a single coordinate
	
	//DRAWING METHODS WITH X/Y coordinates and vector2f coordinates
	public void draw(double x, double y) {
		g.drawImage(characterImage, (int)x, (int)y, null);
	}
	public void draw(Vector2f location){
		this.draw(location.x, location.y);
	}
	//DRAW AT ALREADY set LOCATION
	//this could be helpful for idle animations, it requires not having to repass the location for every draw.
	public void draw(){
			this.draw(location);
	}
	//ROTATING METHOD
	public void setRotate(double angle){
		//eventually have a method in here to rotate the IMAGE, eventually.
	}
}
