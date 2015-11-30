import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuApp extends JFrame implements Runnable {
	
	private volatile boolean running;
	private BufferedImage menuScreen, startButtonIcon;
	
	public void run() {
		
		running = true;
		try{
			menuScreen = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\menuScreen.jpg"));
			startButtonIcon = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\techButtonStart.png"));
		} catch(IOException e){
			System.out.println("INCORRECT location for menu image");
		}
		
		
		
		JPanel p = new JPanel();
		JButton startButton = new JButton();
		startButton.setIcon(new ImageIcon(startButtonIcon));	
		startButton.setBounds(100, 100, 100, 100);
		startButton.repaint();
		JLabel menu = new JLabel(new ImageIcon(menuScreen));
		menu.setBounds(0,0, 1290, 900);
		menu.repaint();
		
		p.add(menu);
		p.add(startButton);
		menu.repaint();
		add(p);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
