package modobox.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Song {

    public Song(String name, String path) { 
	this.name = name;
	this.path = path;
    }
    public Song() {}
    
    private String name;
    private String path;
    private String songId;

    @Id
	public String getSongId() { return this.songId; }
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getPath() { return this.path; }
    public void setPath(String path) { this.path = path; }

}