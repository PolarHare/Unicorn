package com.polarnick.unicorn.echonest.recognizers.fingerprinters;

import com.polarnick.unicorn.utils.Utils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * @author Nickolay Polyarnyi
 */
public class ENMFPFingerPrinter implements FingerPrinter {

    private static final Logger LOG = Logger.getLogger(ENMFPFingerPrinter.class);

    private final String executablePath;

    public ENMFPFingerPrinter(String executablePath) {
        this.executablePath = executablePath;
    }

    @Nullable
    public String calculateFingerPrint(File file) throws IOException, InterruptedException {
        String command = executablePath + " \"" + file.getPath() + "\"";
        LOG.debug("Executing ENMFP: " + command);

        Process p = Runtime.getRuntime().exec(command);
        int code;
        try {
            code = p.waitFor();
        } catch (InterruptedException e) {
            LOG.warn("Interrupted!", e);
            p.destroy();
            throw e;
        }

        String output = Utils.readString(p.getInputStream());
        if (code != 1) {
            LOG.error("Code " + code + " returned, not 1. For file=" + file + "\r\nOutput:\r\n___\r\n" + output + "\r\n___\r\n");
            return null;
        }

        JSONParser parser = new JSONParser();
        String fingerPrint;
        try {
            JSONObject result = (JSONObject) parser.parse(output);
            fingerPrint = (String) result.get("code");
            if (Utils.isEmpty(fingerPrint)) {
                LOG.error("No code were returned:\r\n___\r\n" + output + "\r\n___\r\n");
                return null;
            }
        } catch (ParseException e) {
            LOG.error("Parsing of json output failed:\r\n___\r\n" + output + "\r\n___\r\n", e);
            throw new IllegalStateException(e);
        }
        return fingerPrint;
    }

}
