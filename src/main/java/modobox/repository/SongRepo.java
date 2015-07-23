package modobox.repository;

import modobox.domain.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepo extends MongoRepository<Song, String> {
}