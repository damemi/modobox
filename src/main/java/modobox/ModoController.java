package modobox;

import modobox.repository.*;
import modobox.domain.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

@RestController
public class ModoController {

    Process p;
    SongRepo songRepo;
    List<Song> playlist;
    List<Song> history;
    Song nowPlaying;
    String[] filetypes = {".mp3",".wav"};

    boolean playingList;

    @Autowired
    ModoController(SongRepo songRepo) {
	this.songRepo = songRepo;
    }
    public ModoController() {}

    private void indexDir(String path) {
	File current = new File(path);
	File[] files = current.listFiles();
	for(int i = 0; i < files.length; i++) {
	    File file = files[i];
	    if(file.isDirectory()) {
		indexDir(file.getPath());
	    } else {		
		for(int j=0; j<filetypes.length; j++) {
		    if(file.getName().contains(filetypes[j])) {
			Song s = new Song(file.getName(), file.getPath());
			Song check = songRepo.findByPath(file.getPath());
			if(check == null) {
			    songRepo.insert(s);
			}
			break;
		    }
		}		
	    }
	}
    }

    private void getPlaylist() {
	List<Song> playlist = songRepo.findAll();
	for(int i=0; i<playlist.size(); i++) {
	    int random = (int) (Math.random() * playlist.size());
	    Song a = playlist.get(i);
	    Song b = playlist.get(random);
	    playlist.set(random,a);
	    playlist.set(i, b);
	}
    }

    public void startPlaylist() {
    }

    private void playSong(Song song) {
	if(p != null) {
	    p.destroy();
	}

	try {
	    ProcessBuilder pb = new ProcessBuilder("play", song.getPath()).inheritIO();
	    p = pb.start();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	if(playlist.size() > 0) {
	    Song next = playlist.get(0);
	    playlist.remove(0);
	    playSong(next); //Woah there buddy do we really want to recurse here?
	}
    }

    @RequestMapping("/off")
    public String off() {
	if(p != null) {
	    p.destroy();
	}
        return "Turning off. <a href=\"/on\">Turn on</a>";
    }

    @RequestMapping("/play/{id}")
    public String play(@PathVariable String id) {
	Song song = songRepo.findOne(id);

	/*
	try {
	    if(p != null) {
		p.destroy();
	    }
	    ProcessBuilder pb = new ProcessBuilder("play", song.getPath()).inheritIO();
	    p = pb.start();
	} catch(IOException e) {
	    e.printStackTrace();
	}
	*/
	
	try {
	final URL resource = new File(song.getPath()).toURI().toURL();
	final Media media = new Media(resource.toString());
	
	final MediaPlayer mediaPlayer = new MediaPlayer(media);
	mediaPlayer.play();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	}

	return "Playing "+id+"<br /><a href=\"/\">Go back</a>";
    }

    @RequestMapping("/")
    public String home() {
	String ret = "";

	File homeDir = new File("/home/pi");
	this.indexDir("/home/mike/Music");

	List<Song> songs = songRepo.findAll();
	for(int i = 0; i < songs.size(); i++) {
	    Song song = songs.get(i);
	    ret = ret + "<a href=\"/play/"+song.getSongId()+"\">"+song.getName()+"</a><br />";
	}

	getPlaylist();
	startPlaylist();

	//this.playlingList = true;
	//while(playlingList) {
	//}

	return ret;
    }

    
}
