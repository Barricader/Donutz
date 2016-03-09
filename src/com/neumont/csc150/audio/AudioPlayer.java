package com.neumont.csc150.audio;

import java.io.File;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;

public class AudioPlayer {
	
	private Clip clip;
	private boolean playing;
	
	/**
	 * Sets the song put in the parameters
	 * */
	public AudioPlayer(String s) {
		playing = false;
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        
	        clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == Type.STOP && playing) {
						play();
					}
				}
	        });
	    } catch(Exception ex) {
	        System.out.println("Error code 404: Girlfriend not found.");
	        ex.printStackTrace();
	    }
	}
	/**
	 * Plays the song stored in AudioPlayer(String s)
	 * */
	public void play(){
		if(clip == null){
			return;
		}
		stop();
		clip.start();
		clip.loop(1000);
		playing = true;
	}

	/**
	 * Stops the song stored in AudioPlayer(String s)
	 * */
	public void stop(){
		if(clip.isRunning()) {
			playing = false;
			clip.stop();
		}
		
	}
	/**
	 * Uses stop() to stop the song and closes it releasing its resources
	 * */
	public void close(){
		stop();
		clip.close();
	}
}
