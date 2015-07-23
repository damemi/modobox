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

@RestController
public class ModoController {

    Process p;
    SongRepo songRepo;

    @Autowired
	ModoController(SongRepo songRepo) {
	this.songRepo = songRepo;
    }

    public ModoController() {}

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

	try {
	    if(p != null) {
		p.destroy();
	    }
	    ProcessBuilder pb = new ProcessBuilder("play", song.getPath());
	    p = pb.start();
	} catch(IOException e) {
	    e.printStackTrace();
	}
	return "Playing "+id+"<br /><a href=\"/\">Go back</a>";
    }

    @RequestMapping("/")
    public String home() {
	String ret = "";
	String[] filetypes = {".mp3",".wav"};

	File homeDir = new File("/home/pi");
	File[] files = homeDir.listFiles();
	for(int i=0; i<files.length; i++) {
	    File f = files[i];
	    for(int j=0; j<filetypes.length; j++) {
		if(f.getName().contains(filetypes[j])) {
		    Song s = new Song(f.getName(), f.getPath());
		    songRepo.insert(s);
		    ret = ret + "<a href=\"/play/"+ s.getSongId() + "\">"+f.getName()+"</a><br />";
		    break;
		}
	    }
	}
	return ret;
    }

    
}
