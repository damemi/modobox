package modobox;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
import java.text.DateFormat;
import java.io.*;
import java.net.*;
import sun.audio.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

@RestController
public class HelloController {

    Process p;

    @RequestMapping("/on")
    public String on() {
	try {
	    if(p != null) {
		p.destroy();
	    }
	    ProcessBuilder pb = new ProcessBuilder("play", "/home/mike/mnc.mp3");
	    p = pb.start();
	} catch(IOException e) {
	    e.printStackTrace();
	}

        return "Turning on. <a href=\"/off\">Turn off</a>";
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
	try {
	    if(p != null) {
		p.destroy();
	    }
	    ProcessBuilder pb = new ProcessBuilder("play", URLDecoder.decode(path,"UTF-8"));
	    p = pb.start();
	} catch(IOException e) {
	    e.printStackTrace();
	}
	return "Playing "+path+"<br /><a href=\"/\">Go back</a>";
    }

    @RequestMapping("/")
    public String home() {
	String ret = "";
	String[] filetypes = {".mp3",".wav"};

	File homeDir = new File("/home/mike");
	File[] files = homeDir.listFiles();
	for(int i=0; i<files.length; i++) {
	    File f = files[i];
	    for(int j=0; j<filetypes.length; j++) {
		if(f.getName().contains(filetypes[j])) {

		    try {
			ret = ret + "<a href=\"/play/"+URLEncoder.encode(f.getPath(), "UTF-8") + "\">"+f.getName()+"</a><br />";
		    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		    }
		    break;
		}
	    }
	}
	return ret;
    }

    
}
