
public class SynchronizedIDCounter {
	private int prevID;
	public SynchronizedIDCounter() { prevID = 0; }

	public synchronized int next() {
		prevID++;
		return prevID;
	}
}
