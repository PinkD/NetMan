package moe.pinkd.netman.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by PinkD on 2017/5/11.
 * ShellUtil
 */

public class ShellUtil {

    public static List<String> SURun(String[] commands) {
        if (Shell.SU.available()) {
            return Shell.SU.run(commands);
        } else {
            List<String> result = new ArrayList<>();
            result.add("SU not available");
            return result;
        }
    }

    public static String shellRun(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String tmp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((tmp = bufferedReader.readLine()) != null) {
                stringBuilder.append(tmp);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
