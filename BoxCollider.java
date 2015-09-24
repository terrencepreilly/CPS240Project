package manipulation.util;

import java.awt.image.BufferedImage;

import actors.util.Actor;
import actors.util.Scenic;
import actors.util.Character;

/*
 * The BoxCollider class will hold a BufferedImage of the representing character/scenic object
 * Based on the image it will make a box collider of appropriate length.
 * When the location needs to be moved it will be moved with set location
 * A scenic object should not need it's location moved, but the method will
 * allow it to be for the initial setting, which will be done with the constructor
 * 
 * It will store an array of vectors representing the points of the box collider
 * It will also use the image passed in the constructor to store the necessary
 * width and height of the collider values (height and width of the original image
 */

public class BoxCollider {

	private Vector[] points;
	private int height;
	private int width;
	
	public BoxCollider(BufferedImage image, boolean isCharacter){
		//make new boxcollider based on image
		//scenic objects only need 2 points
		width = image.getWidth();
		height = image.getHeight();
		
		//if it's NOT a character then it's a Scenic, only need to create 2 points
		if(!isCharacter){
			points = new Vector[2];
			points[0] = new Vector(0, 0); //top left point
			points[1] = new Vector(width, height);
		}
		//characters need 4 points for collision detetion
		else{
			points = new Vector[4];
			points[0] = new Vector(0, 0); //top left
			points[1] = new Vector(width, height); //bottom right
			points[2] = new Vector(width, 0); //top right
			points[3] = new Vector(0, height); //bottom left
		}
	}
	
	//mutators
	public void setLocation(Vector location){
		if(points.length == 2){
			points[0] = new Vector(location.x, location.y);
			points[1] = new Vector(location.x + width, location.y + height);
		}
		else{
			points[0] = new Vector(location.x, location.y); //top left
			points[1] = new Vector(location.x + width, location.y + height);//bottom right
			points[2] = new Vector(location.x + width, location.y);//top right
			points[3] = new Vector(location.x, location.y + height);//bottom left
		}
	}
	//accessors
	public Vector[] getPoints(){
		return points;
	}
	//static method
	//will return true IF the inner collider has any point inside the outer box collider
	public static boolean detectCollision(Actor outer, Actor inner){
			Vector[] outerPoints = outer.getBoxCollider().getPoints();
			Vector[] innerPoints = inner.getBoxCollider().getPoints();
			
			//test for a Character INSIDE a Scenic, only need limited points
			if(outer instanceof Scenic){
				//loop four times, four each point in inner boxCollider
				for(int i = 0; i < innerPoints.length; i++){
					//if ANY innerpoint is within the boundaries of the outer boxCollider
					//then there has been a collision, return true
					if(innerPoints[i].x < outerPoints[1].x && innerPoints[i].x > outerPoints[0].x &&
							innerPoints[i].y < outerPoints[1].y && innerPoints[i].y > outerPoints[0].y){
						return true;
					}
				}
			}
			//test for a Character INSIDE a CHARACTER, need to check EVERY point for outer, against every point for inner
			//top left[0], bottom right[1], top right[2], bottom left[3]
			if(outer instanceof Character){
								
				//loop through for each point of inner
				for(int i = 0; i < innerPoints.length; i++){
					//check if ANY point of the inner is inside the outer character
					if(innerPoints[i].x - 1 < outerPoints[1].x && innerPoints[i].x + 1 > outerPoints[0].x &&
							innerPoints[i].y - 1 < outerPoints[1].y && innerPoints[i].y + 1 > outerPoints[0].y )
						return true;
					}
			}
			return false;
	}	
	public String toString(){
		return "BoxCollider " + points[0] + points[1];
	}
}
