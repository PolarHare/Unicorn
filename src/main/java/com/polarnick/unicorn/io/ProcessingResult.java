package com.polarnick.unicorn.io;

/**
 * @author Nickolay Polyarnyi
 */
public class ProcessingResult {

    private final int recognizedMp3Files;
    private final int mp3Files;

    public ProcessingResult(int recognizedMp3Files, int mp3Files) {
        this.recognizedMp3Files = recognizedMp3Files;
        this.mp3Files = mp3Files;
    }

    public int getRecognizedMp3Files() {
        return recognizedMp3Files;
    }

    public int getMp3Files() {
        return mp3Files;
    }

    @Override
    public String toString() {
        return "Recognized mp3 files count: " + recognizedMp3Files + "\r\nTotal mp3 files count: " + mp3Files;
    }
}
