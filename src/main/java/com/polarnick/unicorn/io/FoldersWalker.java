package com.polarnick.unicorn.io;

import com.polarnick.unicorn.echonest.recognizers.SongRecognizer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nickolay Polyarnyi
 */
public class FoldersWalker implements FileVisitor<Path> {

    private static final Logger LOG = Logger.getLogger(FoldersWalker.class);

    private final SongRecognizer recognizer;
    private final AtomicInteger recognizedCount;
    private final AtomicInteger totalCount;

    public FoldersWalker(SongRecognizer recognizer) {
        this.recognizer = recognizer;
        this.recognizedCount = new AtomicInteger(0);
        this.totalCount = new AtomicInteger(0);
    }

    public ProcessingResult process(File[] files) throws InterruptedException {
        recognizedCount.set(0);
        totalCount.set(0);
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    Files.walkFileTree(file.toPath(), this);
                } else {
                    process(file);
                }
            }
        } catch (IOException e) {
            LOG.error("IO error, while walking", e);
        }
        return new ProcessingResult(recognizedCount.get(), totalCount.get());
    }

    private void process(File file) throws InterruptedException {
        System.out.println("Processing: " + file);
        if (recognizer.getSong(file) != null) {
            System.out.println("          + " + file);
            recognizedCount.incrementAndGet();
        } else {
            System.out.println("          - " + file);
        }
        totalCount.incrementAndGet();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        try {
            process(file.toFile());
        } catch (InterruptedException e) {
            LOG.error("Interrupted, while walking", e);
            Thread.currentThread().interrupt();
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
