/*
 * Marcus Fields
 * AudioTest update
 * This time, it uses Java swing.
 * 
 * The audio files are located in the src/AudioFiles directory of a project.
 * 
 * Just a heads-up: there's this weird compatibility thing with Java swing's audio capabilities; it will only work
 * with certain .wav files --or else it'll throw a LineUnavailableException-- which is incredibly irritating. I got 
 * most of it work, though. There's probably some work-around for this that I overlooked;till studying the documentation.
 */

import javax.swing.JFrame;
import javax.swing.*;
import javax.sound.sampled.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;


public class AudioTest extends JFrame{
	//Sound set-up
	private final AudioInputStream aisItemGet;
	private final Clip clpItemGet;
	
		private final AudioInputStream aisZombieGroan1;
		private final Clip clpZombieGroan1;
	
		private final AudioInputStream aisBGMAmbience;
		private final Clip clpBGMAmbience;
	
	//Frame settings
	private static final long serialVersionUID = -1L;
	private final int WIDTH = 480;
	private final int HEIGHT = (int) (WIDTH*0.5625);
	public final Dimension RESOLUTION = new Dimension(WIDTH,HEIGHT);
	private JFrame frame = new JFrame();
	private JPanel varPanelMain = new JPanel();
	
	//Registering the E-H
	private JButton varButtonExit = new JButton("Exit application");
	private JButton varButtonPlayZombieGroan = new JButton("Zombie groaning");
	private JButton varButtonPlayItemGet = new JButton("Item get");
	private JButton varButtonLoopBGMAmbience = new JButton("Ambience music");


	public AudioTest() throws UnsupportedAudioFileException,IOException,LineUnavailableException {
		//GUI settings
		setMinimumSize(RESOLUTION);
		setPreferredSize(RESOLUTION);
		setMaximumSize(RESOLUTION);
		
		//GUI frame
		varPanelMain.add(varButtonExit);	
		varPanelMain.add(varButtonPlayZombieGroan);
		varPanelMain.add(varButtonPlayItemGet);
		varPanelMain.add(varButtonLoopBGMAmbience);
		frame.add(varPanelMain);
		frame.setTitle("Marktatious Graphics");
		frame.setSize(WIDTH,HEIGHT);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creating clips and AIS for each sound/BGM
		clpItemGet = AudioSystem.getClip();
		aisItemGet = AudioSystem.getAudioInputStream(new File("AudioFiles/sndItemGet.wav"));
		
		clpZombieGroan1 = AudioSystem.getClip();
		aisZombieGroan1 = AudioSystem.getAudioInputStream(new File("AudioFiles/sndZombieGroan1.wav")); //Fresound.org
		
		clpBGMAmbience = AudioSystem.getClip();
		aisBGMAmbience = AudioSystem.getAudioInputStream(new File("AudioFiles/sndBGMAmbience.wav")); //Freesound.org
		
		
		//E-H system
		varButtonExit.addActionListener(new ButtonEvent("Exit"));
		varButtonPlayZombieGroan.addActionListener(new ButtonEvent("PlayZombieGroanSound"));
		varButtonPlayItemGet.addActionListener(new ButtonEvent("PlayItemGetSound"));
		varButtonLoopBGMAmbience.addActionListener(new ButtonEvent("LoopBGMAmbience"));
		
		
	}						
	
	class ButtonEvent implements ActionListener {
		//Register each button with a String command to do something.
		//There's probably a way to do this better, but doesn't really matter in this case ATM
		String varCommand;
		public ButtonEvent(String varCommand) {
			this.varCommand = varCommand;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			//Exit the program
			if (varCommand.equals("Exit")) {
				frame.dispose();
				System.exit(1);
			}
			//Play an item sound (could be for some kind of powerup or something; mainly a placeholder)
			else if (varCommand.equals("PlayItemGetSound")) {
				PlayClip(clpItemGet,aisItemGet);
			} 
			//Play a zombie groan sample
			else if (varCommand.equals("PlayZombieGroanSound")) {
				PlayClip(clpZombieGroan1,aisZombieGroan1);
			} 
			//Loop the background ambience
			else if (varCommand.equals("LoopBGMAmbience")) {
				LoopClip(clpBGMAmbience,aisBGMAmbience);
			}
			//Debugging
			else {
				System.out.println("Error: button doesn't have a command!");
			}
		}
		//Essentially, this method block determines whether a clip is already playing. And if so, stop it and then play the sound
		public void PlayClip(Clip sound,AudioInputStream ais) {
			try {
				if (sound.isOpen() == true) {
					if(sound.isRunning() || sound.isActive()) {
						sound.stop();
						sound.flush();
					}
				}
				else {
					sound.open(ais);
				}
				sound.setFramePosition(0);
				sound.start();
			}
			catch (LineUnavailableException exception) {
				System.out.println("Line unavailable!");
			}
			catch (IOException exception) {
				System.out.println("IO unavailable!");
			}
		}
		//Same only loop it instead
		public void LoopClip(Clip sound,AudioInputStream ais) {
			try {
				
				if (sound.isOpen() == true) {
					if(sound.isRunning() || sound.isActive()) {
						sound.stop();
						sound.flush();
					}
				}
				else {
					sound.open(ais);
				}
				sound.setFramePosition(0);
				sound.start();
				sound.loop(Clip.LOOP_CONTINUOUSLY);
				
			}
			catch (LineUnavailableException exception) {
				System.out.println("Line unavailable!");
			}
			catch (IOException exception) {
				System.out.println("IO unavailable!");
			}
		}
	}

	public static void main(String[] args) throws LineUnavailableException,IOException,UnsupportedAudioFileException {
		//Simply run the program
		AudioTest varScene = new AudioTest();
	}

	

}


