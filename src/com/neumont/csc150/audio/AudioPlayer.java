package com.neumont.csc150.audio;

import java.io.File;

import javax.sound.sampled.*;

public class AudioPlayer {
	
	private Clip clip;
	
	/**
	 * Sets the song put in the parameters
	 * */
	public AudioPlayer(String s) {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
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
	}

	/**
	 * Stops the song stored in AudioPlayer(String s)
	 * */
	public void stop(){
		if(clip.isRunning()){
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
