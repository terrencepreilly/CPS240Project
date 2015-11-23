
class ServerOutputHandler extends OutputHandler {

	//TODO Don't pass the locations back to the client you got it from.
	public void run() {
                System.out.println("OUTPUTHANDLER:\trun\tbegin");
                try {
                        while (true) {
                                GameDelta gd = gamestate.getUpdate();
                                if (gd != null) {
                                        System.out.println("OUTPUTHANDLER:\trun\tsending" + gd);
                                        out.writeObject(gd);
                                        out.flush();
                                }

                                // make room for other processes
                                Thread.sleep(10L);
                        }
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }
                System.out.println("OUTPUTHANDLER:\trun\tfinish");

	}
}
