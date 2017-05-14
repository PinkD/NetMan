package moe.pinkd.netman.util;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;
import moe.pinkd.netman.bean.IptablesClause;
import moe.pinkd.netman.config.Config;

/**
 * Created by PinkD on 2017/5/11.
 * ShellUtil
 */

public class ShellUtil {
    private static final String TAG = "ShellUtil";

    public static List<String> SURun(String[] commands) {
        for (String command : commands) {
            Log.d(TAG, "SURun: " + command);
        }
        if (Shell.SU.available()) {
            return Shell.SU.run(commands);
        } else {//TODO kill process
            return new ArrayList<>();
        }
    }

    public static List<String> init() {
        return SURun(new String[]{
                "iptables -t filter -N net_man",
                "iptables -t filter -A OUTPUT -j net_man"
        });
    }

    public static String shellRun(String command) {
        Runtime runtime = Runtime.getRuntime();
//        command = "/system/bin/sh -c -v \"" + command + "\"";
        Log.d(TAG, "shellRun: " + command);
        try {
            Process process = runtime.exec(new String[]{"/system/bin/sh", "-c", command});
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

    public static List<String> banApp(int uid, int status) throws ArrayIndexOutOfBoundsException {
        List<String> list = new ArrayList<>();
        if ((status & Config.ALL_MASK) == Config.ALL_MASK) {
            list.add(SURun(new String[]{new IptablesClause(uid, Config.ALL_MASK).toString()}).get(0));
            return list;
        }
        if ((status & Config.CELLULAR_MASK) != 0) {
            list.add(SURun(new String[]{new IptablesClause(uid, Config.CELLULAR_MASK).toString()}).get(0));
        }
        if ((status & Config.WIFI_MASK) != 0) {
            list.add(SURun(new String[]{new IptablesClause(uid, Config.WIFI_MASK).toString()}).get(0));
        }
        if ((status & Config.VPN_MASK) != 0) {
            list.add(SURun(new String[]{new IptablesClause(uid, Config.VPN_MASK).toString()}).get(0));
        }
        return list;
    }

    public static Map<String, String> getNetworkInterfaces() {
        Map<String, String> map = new HashMap<>(3);
        map.put("wifi", "wlan0");
        map.put("vpn", "tun0");
//        Pattern pattern = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3}");
//        String result = shellRun("ip addr | grep 'inet' | grep wlan");
        Pattern pattern = Pattern.compile("10(\\.[0-9]{1,3}){3}");
        String result = shellRun("ip addr | grep \"inet 10.\" | grep net");
        Log.d(TAG, "getNetworkInterfaces: " + result);
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            Log.d(TAG, "getNetworkInterfaces: " + matcher.group());
            String[] tmp = result.split(" ");
            map.put("cellular", tmp[tmp.length - 1]
            );
        }
        return map;
    }

}
