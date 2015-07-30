package modobox;

import modobox.domain.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class ModoPlayer extends Thread {

    private List<Song> playlist;
    private Process p;
    public ModoPlayer(List<Song> playlist) { 
	this.playlist = playlist;
    }

    public List<Song> getPlaylist() {
	return this.playlist;
    }

    @Override
    public void run() {
	for(int i = 0; i < this.playlist.size(); i++) {
	    Song song = this.playlist.get(i);
	    try {
		ProcessBuilder pb = new ProcessBuilder("play", song.getPath()).inheritIO();
		p = pb.start();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	    try { 
		p.waitFor();
	    } catch (InterruptedException ie) { // tried playing new song
		p.destroy();
		break;
	    }
	}
    }

}