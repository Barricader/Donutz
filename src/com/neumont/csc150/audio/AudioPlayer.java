package com.neumont.csc150.audio;

import java.io.File;

import javax.sound.sampled.*;

public class AudioPlayer {
	
	private Clip clip;
	
	public AudioPlayer(String s) {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	
	public void play(){
		if(clip == null){
			return;
		}
		stop();
		clip.start();
	}
	public void stop(){
		if(clip.isRunning()){
			clip.stop();
		}
	}
	public void close(){
		stop();
		clip.close();
	}
}
