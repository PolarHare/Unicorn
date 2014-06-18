package com.polarnick.unicorn.echonest.recognizers.fingerprinters;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * @author Nickolay Polyarnyi
 */
public interface FingerPrinter {

    /**
     * @param file mp3 file to be fingerprinted
     * @return fingerprint code, or {@code null}, if something went bad
     * @throws IOException if some sort of IO exceptions occurs. (With exec for example)
     * @throws InterruptedException if execution was interrupted
     */
    @Nullable
    public String calculateFingerPrint(File file) throws IOException, InterruptedException;

}
