package com.polarnick.unicorn.echonest.recognizers;

import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.Track;
import com.polarnick.unicorn.echonest.EchonestAPIProvider;
import org.apache.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Nickolay Polyarnyi
 */
public class FullFileRecognizer implements SongRecognizer {

    private static final Logger LOG = Logger.getLogger(FingerPrintRecognizer.class);

    private final EchonestAPIProvider apiProvider;
    private final int analysisTimeout;
    private final int maxAttempts;

    public FullFileRecognizer(EchonestAPIProvider apiProvider, int analysisTimeout, int maxAttempts) {
        this.apiProvider = apiProvider;
        this.analysisTimeout = analysisTimeout;
        this.maxAttempts = maxAttempts;
    }

    @Nullable
    @Override
    public Song getSong(File file) throws InterruptedException {
        LOG.debug("Recognizing by full file...\nFile: " + file);

        final List<Song> songs;
        try {
            Track track = null;
            for (int i = 1; i <= maxAttempts && track == null; i++) {
                LOG.debug("Uploading file... (attempt #" + i + ")");
                try {
                    track = apiProvider.getEchoNestAPI().uploadTrack(file);
                } catch (IOException e) {
                    LOG.warn("Exception while uploading!", e);
                }
            }
            if (track == null) {
                LOG.error("Uploading failed!");
                return null;
            }

            LOG.debug("Waiting for analysis...");
            track.waitForAnalysis(analysisTimeout);

            SongParams params = new SongParams();
            params.setID(track.getSongID());

            LOG.debug("Getting song by songID=" + track.getSongID() + "...");
            songs = apiProvider.getEchoNestAPI().getSongs(params);
        } catch (EchoNestException e) {
            LOG.error("Identifying failed!", e);
            return null;
        }

        if (songs.size() == 0) {
            LOG.warn("No songs were found!");
            return null;
        }
        if (songs.size() > 1) {
            LOG.warn("Many songs were found! Songs: " + songs);
        }

        Song song = songs.get(0);
        LOG.debug("Recognized: songID=" + song.getID());
        return song;
    }
}
