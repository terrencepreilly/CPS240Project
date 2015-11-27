/*
 * Marcus Fields
 * Audio Test program
 * 
 * The purpose of this is to just get audio working in our project with a small GUI sample. 
 * 
 * This uses JavaFX.
 * 
 * The audio files are located in the src/AudioFiles directory of a project.
 * 
 * Sounds are from freesound.org. They are not final. If you guys find some more audio that you like 
 * better, then you can replace them with your own in the AudioFiles directory. Or just tell me and
 * I can do it/upload them to the github repository
 * 
 * Later, if you guys wish, when I get the actual project to work on my computer, I can incorporate some
 * of this into the actual gameplay.
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import java.io.*;

public class AudioTest extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		
		//Creating File objects from our audio directory
		final String fleSoundSlashSword1 = new File("AudioFiles/sndSlashSword1.wav").toURI().toString();
		final String fleSoundZombieGroan1 = new File("AudioFiles/sndZombieGroan1.wav").toURI().toString();
		final String fleMusicAmbience = new File("AudioFiles/sndBGMAmbience.wav").toURI().toString();
				
		//Create AudioClip objects from our Files
		AudioClip varSoundSlashSword1 = new AudioClip(fleSoundSlashSword1);
		AudioClip varSoundZombieGroan1 = new AudioClip(fleSoundZombieGroan1);
		AudioClip varMusicAmbience = new AudioClip(fleMusicAmbience);
	
		//Create Button GUI objects for our AudioClips
		Button varButtonPlaySlashSound = new Button("Slash");
		Button varButtonPlayZombieGroanSound = new Button("Zombie Groan");
		Button varButtonLoopAmbienceMusic = new Button("Ambience BGM");
	
		//Event handling for each of our buttons
		varButtonPlaySlashSound.setOnAction(e -> {
			if (varSoundSlashSword1.isPlaying() == true) {
				varSoundSlashSword1.stop();
			}
			varSoundSlashSword1.play();
		});
		varButtonPlayZombieGroanSound.setOnAction(e -> {
			if (varSoundZombieGroan1.isPlaying() == true) {
				varSoundZombieGroan1.stop();
			}
			varSoundZombieGroan1.play();
		});
		varMusicAmbience.setCycleCount(AudioClip.INDEFINITE);
		varButtonLoopAmbienceMusic.setOnAction(e -> {
			if (varMusicAmbience.isPlaying() == true) {
				varMusicAmbience.stop();
			}
			varMusicAmbience.play();
		});


		//Layouts
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		BorderPane varPane = new BorderPane();
		varPane.setCenter(varButtonLoopAmbienceMusic);
		varPane.setBottom(hBox);
		varPane.setLeft(varButtonPlaySlashSound);
		varPane.setTop(varButtonPlayZombieGroanSound);

		//Create a scene for our stage
		Scene varScene = new Scene(varPane,480,(int) (480*0.5625));
		primaryStage.setTitle("Audio GUI test");
		primaryStage.setScene(varScene);
		primaryStage.show(); 
 	}
}
