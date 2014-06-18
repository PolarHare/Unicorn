package com.polarnick.unicorn;

import com.polarnick.unicorn.echonest.recognizers.FingerPrintRecognizer;
import com.polarnick.unicorn.io.FoldersWalker;
import com.polarnick.unicorn.io.ProcessingResult;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.io.File;

/**
 * @author Nickolay Polyarnyi
 */
public class App {

    private static final Logger LOG = Logger.getLogger(FingerPrintRecognizer.class);

    public static void main(String[] args) throws BeansException, InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        FoldersWalker foldersWalker = (FoldersWalker) context.getBean("foldersProcessor");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fileopen = new JFileChooser();
        fileopen.setMultiSelectionEnabled(true);

        boolean canceled = false;
        while (!canceled) {
            int selectedOption = fileopen.showDialog(null, "Choose mp3 file(s) or folder(s)");
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                File[] files = fileopen.getSelectedFiles();
                ProcessingResult result = foldersWalker.process(files);
                System.out.println(result);
            } else if (selectedOption == JFileChooser.CANCEL_OPTION) {
                canceled = true;
            } else {
                throw new IllegalStateException("File chooser returned " + selectedOption + "!");
            }
        }
    }

}
