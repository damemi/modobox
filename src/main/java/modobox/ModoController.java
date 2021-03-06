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
    String[] filetypes = {".mp3",".wav"};
    ModoPlayer modoPlayer;
    MediaPlayer currentPlayer;

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
	this.playlist = songRepo.findAll();
	for(int i=0; i<this.playlist.size(); i++) {
	    int random = (int) (Math.random() * this.playlist.size());
	    Song a = this.playlist.get(i);
	    Song b = this.playlist.get(random);
	    this.playlist.set(random,a);
	    this.playlist.set(i, b);
	}
    }

    public void startPlaylist() {
	this.modoPlayer = new ModoPlayer(this.playlist);
	modoPlayer.start();
    }

    @RequestMapping("/stop")
    public String stop() {
	modoPlayer.interrupt();
        return "Turning off. <a href=\"/\">Turn on</a>";
    }

    private void stopAndPop() {
	if(this.modoPlayer != null) {
	    this.modoPlayer.interrupt();
	    this.playlist = this.modoPlayer.getPlaylist();
	    this.playlist.remove(0);
	}
    }

    @RequestMapping("/next")
    public String next() {
	stopAndPop();

	this.modoPlayer = new ModoPlayer(this.playlist);
	modoPlayer.start();

	return "Success";
    }

    @RequestMapping("/play/{id}")
    public String play(@PathVariable String id) {
	Song song = songRepo.findOne(id);
	stopAndPop();

	this.playlist.add(0,song);
	this.modoPlayer = new ModoPlayer(this.playlist);
	modoPlayer.start();

	return "Playing "+id+"<br /><a href=\"/\">Go back</a>";
    }

    @RequestMapping("/listSongs")
    public String listSongs() {
	String ret = "";
	List<Song> songs = songRepo.findAll();
	for(int i = 0; i < songs.size(); i++) {
	    Song song = songs.get(i);
	    ret = ret + "<a href=\"/play/"+song.getSongId()+"\">"+song.getName()+"</a><br />";
	}
	return ret;
    }

    @RequestMapping("/")
    public String home() {
	String ret = "";

	this.indexDir("/home/mike/Music");	

	getPlaylist();
	startPlaylist();
	for(int j=0; j<this.playlist.size(); j++) {
	    Song song = this.playlist.get(j);
	    ret = ret + song.getName() + "<br />";
	}

	return ret;
    }

    
}
