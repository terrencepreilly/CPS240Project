
public class FrameRate {
	
	private String frameRate;
	private long lastTime;
	private long delta;
	private int frameCount;
	
	public void initialize() {
		lastTime = System.currentTimeMillis();
		frameRate = "FPS 0";
	}
	public void calculate() {
		long current = System.currentTimeMillis(); //current time
		delta += current - lastTime; //set delta (total number of milliseconds)
		lastTime = current; //now current is lastTime
		frameCount++; //increment frameCount
				
		if(delta > 1000) {
			delta -= 1000; //subtract 1 second from total delta time, this will save leftover milliseconds
			frameRate = String.format("FPS %s", frameCount);
			frameCount = 0; //reset framerate counter
		}
	}
	
	//accessor to get frameRate value
	public String getFrameRate(){
		return frameRate;
	}
}
