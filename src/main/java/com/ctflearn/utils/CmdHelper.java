package com.ctflearn.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdHelper {

    public static String executeCommandAndWaitForOutput(String cmd) {
        String fullLogs = "";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").contains("Windows")) {
                processBuilder.command("cmd.exe", "/c", cmd);
            } else {
                processBuilder.command("bash", "-c", cmd);
            }

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                fullLogs = fullLogs.concat("\n" + line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullLogs;
    }

}
