package com.polarnick.unicorn.echonest.recognizers;

import com.echonest.api.v4.Song;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Interface of song recognizer.
 *
 * @author Nickolay Polyarnyi
 */
public interface SongRecognizer {

    /**
     * Trying to recognize song by mp3 file.
     *
     * @param file song mp3 file to be recognized.
     * @return echonest song. Returns {@code null}, if the file was not recognized, or some problems occured.
     */
    @Nullable
    public Song getSong(File file) throws InterruptedException;

}
