package com.polarnick.unicorn.echonest.recognizers;

import com.echonest.api.v4.Song;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

/**
 * @author Nickolay Polyarnyi
 */
public class SongRecognizerAppender implements SongRecognizer {

    private final List<SongRecognizer> recognizerList;

    public SongRecognizerAppender(List<SongRecognizer> recognizerList) {
        this.recognizerList = recognizerList;
    }

    @Nullable
    @Override
    public Song getSong(File file) throws InterruptedException {
        for (SongRecognizer recognizer : recognizerList) {
            Song song = recognizer.getSong(file);
            if (song != null) {
                return song;
            }
        }
        return null;
    }
}
