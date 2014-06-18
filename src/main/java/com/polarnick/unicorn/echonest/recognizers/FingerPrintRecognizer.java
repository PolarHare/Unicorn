package com.polarnick.unicorn.echonest.recognizers;

import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Params;
import com.echonest.api.v4.Song;
import com.polarnick.unicorn.echonest.EchonestAPIProvider;
import com.polarnick.unicorn.echonest.recognizers.fingerprinters.FingerPrinter;
import org.apache.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Nickolay Polyarnyi
 */
public class FingerPrintRecognizer implements SongRecognizer {

    private static final Logger LOG = Logger.getLogger(FingerPrintRecognizer.class);

    private final EchonestAPIProvider apiProvider;
    private final FingerPrinter fingerPrinter;
    private final String fingerPrinterVersion;

    public FingerPrintRecognizer(EchonestAPIProvider apiProvider, FingerPrinter fingerPrinter, String fingerPrinterVersion) {
        this.apiProvider = apiProvider;
        this.fingerPrinter = fingerPrinter;
        this.fingerPrinterVersion = fingerPrinterVersion;
    }

    @Nullable
    @Override
    public Song getSong(File file) throws InterruptedException {
        LOG.debug("Recognizing by finger print...\nFile: " + file);

        final String code;
        try {
            code = fingerPrinter.calculateFingerPrint(file);
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }

        Params params = new Params();
        params.add("version", fingerPrinterVersion);
        params.add("code", code);

        List<Song> songs;
        try {
            LOG.debug("Finger print was sent for recognizing...");
            songs = apiProvider.getEchoNestAPI().identifySongs(params);
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
