/**
 * A utility class which calculates the frame rate for the game.
 */
class FrameRate {
	
	private String frameRate;
	private long lastTime;
	private long delta;
	private int frameCount;
	
	/**
	 * Initialize FrameRate by setting the last time to the current time.
	 */
	public void initialize() {
		lastTime = System.currentTimeMillis();
		frameRate = "FPS 0";
	}

	/**
	 * Calculate the frame rate by setting delta (the difference of this
	 * time and the last time calculate() was called) and incrementing
	 * the frameCount.
	 */
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
	
	/**
	 * Get the current frame rate.
	 * @return A String representation of the current frame rate.
	 */
	public String getFrameRate(){
		return frameRate;
	}
}
