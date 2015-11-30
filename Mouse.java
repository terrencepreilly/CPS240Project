import java.awt.Canvas;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

public class Mouse implements MouseListener, MouseMotionListener {

    //number of buttons
    private static final int BUTTON_COUNT = 3;
    private int direction; //to represent degree calculation of mouse position
    //array to tell when a button is down, down = true, not down = false
    private boolean[] mouse;
    private Vector mousePos, currentPos;
    private Canvas canvas; //to be used to manipulate mouse
    private Robot robot; //Robot class to "reset/move" mouse

    /**
     * Constructor, create new mouse at Position 0,0, 
     */
    public Mouse(Canvas c){
        canvas = c;
        mousePos = new Vector(canvas.getWidth() / 2, canvas.getHeight() / 2); //set as center of canvas/screen
        currentPos = new Vector(canvas.getWidth(), canvas.getHeight() / 2); //set as center of canvas/screen
        mouse = new boolean[BUTTON_COUNT];
        
        try{
        	robot = new Robot();
        } catch(Exception ex){
        	//do nothing
        }
    }
    
    public Vector getPosition(){
        return mousePos;
    }
    public int getDirection(){
        return direction % 360;
    }
    
    public synchronized void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * will update currentPos whenever mouse is moved.
     * This involves a variety of things. One of them being getting/updating the direction faced.
     * @param e, the mouseEvent triggered by moving the mouse
     */
    public synchronized void mouseMoved(MouseEvent e) {
        mousePos = new Vector(e.getPoint().x, e.getPoint().y);
        
        //calculate the angle of this new vector from 0,0
        //turn coords into proper locations
        float properXCoord, properYCoord;
        properXCoord = mousePos.x - (canvas.getWidth() / 2);
        properYCoord = (canvas.getHeight() / 2) - mousePos.y;
        
        //now calculate angle between these new coords and 0,0
        if(properXCoord > 0 && properYCoord >= 0){
        	//in FIRST quadrant
        	direction = (int) Math.toDegrees(Math.atan(properYCoord / properXCoord));
        }
        else if(properXCoord <= 0 && properYCoord > 0){
        	//in SECOND quadrant
        	if(properXCoord == 0){
        		direction = 90;// "facing up"
        	} else{
        		direction = (int) Math.toDegrees((180 + Math.atan(properYCoord / properXCoord)));
        	}
        } else if(properXCoord < 0 && properYCoord <= 0){
        	//in THIRD quadrant
        	if(properYCoord == 0){
        		direction = 180;//"facing right"
        	} else {
        		direction = (int) Math.toDegrees((180 + Math.atan(properYCoord / properXCoord)));
        	}
        } else{
        	//otherwise in FOURTH quadrant
        	if(properXCoord == 0){
        		direction = 270; //facing down
        	} else {
        		direction = (int) Math.toDegrees(360 + Math.atan(properYCoord / properXCoord));
        	}
        }        
        centerMouse();
    }

    private void centerMouse(){
    	if(robot != null && canvas.isShowing()){
    		if(mousePos.x > canvas.getWidth() / 2 + 400 || mousePos.x < canvas.getWidth() / 2 - 400
    				|| mousePos.y > canvas.getHeight() / 2 + 400 || mousePos.y < canvas.getHeight() / 2 - 400){	
    			Point center = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
    			robot.mouseMove((int)center.x, (int)center.y);
    		}
    	}
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //don't need this method, but must inplement from MouseListener
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton() - 1;
        if(button >= 0 && button < mouse.length){
            mouse[button] = true; //mouse button has been pressed
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton() - 1;
        if(button >= 0 && button < mouse.length){
            mouse[button] = false; //mouse button has been released
        }
    }

    /**
     * @return the array of all the buttons currently activated.
     */
    public boolean[] buttonsPressed(){
        boolean[] temp = new boolean[BUTTON_COUNT];
        for(int i = 0; i < mouse.length; i++){
            temp[i] = mouse[i]; //fill temp
            mouse[i] = false; //erase mouse back to all false
        }
        return temp; //return temp array which had all of mouse original values
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }
}
