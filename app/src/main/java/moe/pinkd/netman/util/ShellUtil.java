package moe.pinkd.netman.util;


import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;
import moe.pinkd.netman.App;
import moe.pinkd.netman.bean.IptablesClause;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.ui.activity.NoRootActivity;

/**
 * Created by PinkD on 2017/5/11.
 * ShellUtil
 */

public class ShellUtil {
    private static final String TAG = "ShellUtil";

    public static void SURun(String[] commands) {
        for (String command : commands) {
            Log.d(TAG, "SURun: " + command);
        }
        if (Shell.SU.available()) {
            Log.d(TAG, "SURun: " + Shell.SU.run(commands));
        } else {
            App.getContext().startActivity(new Intent(App.getContext(), NoRootActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            Log.d(TAG, "SURun: []");
        }
    }

    public static void SURun(String command) {
        Log.d(TAG, "SURun: " + command);
        if (Shell.SU.available()) {
            Log.d(TAG, "SURun: " + Shell.SU.run(command));
        } else {
            App.getContext().startActivity(new Intent(App.getContext(), NoRootActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            Log.d(TAG, "SURun: []");
        }
    }

    public static void init() {
        initCellularInterfaces();
        SURun(new String[]{
                "iptables -t filter -N net_man",
                "iptables -t filter -I OUTPUT 1 -j net_man",
                "iptables -t filter -I FORWARD 1 -j net_man"
        });
    }


    public static void banApp(int uid, int status) {
        clearTableWithUid(uid);
        if ((status & Config.ALL_MASK) == Config.ALL_MASK) {
            Log.d(TAG, "banApp: ALL_MASK");
            SURun(new IptablesClause(uid, Config.ALL_MASK).toString());
            return;
        }
        if ((status & Config.CELLULAR_MASK) != 0) {
            Log.d(TAG, "banApp: CELLULAR_MASK");
            SURun(new IptablesClause(uid, Config.CELLULAR_MASK).toString());
        }
        if ((status & Config.WIFI_MASK) != 0) {
            Log.d(TAG, "banApp: WIFI_MASK");
            SURun(new IptablesClause(uid, Config.WIFI_MASK).toString());
        }
        if ((status & Config.VPN_MASK) != 0) {
            Log.d(TAG, "banApp: VPN_MASK");
            SURun(new IptablesClause(uid, Config.VPN_MASK).toString());
        }
    }

    private static void clearTableWithUid(int uid) {
        SURun(new IptablesClause(uid, Config.ALL_MASK, false).toString());
        SURun(new IptablesClause(uid, Config.CELLULAR_MASK, false).toString());
        SURun(new IptablesClause(uid, Config.WIFI_MASK, false).toString());
        SURun(new IptablesClause(uid, Config.VPN_MASK, false).toString());
    }

    public static void initCellularInterfaces() {
        Pattern pattern = Pattern.compile("10(\\.[0-9]{1,3}){3}");
        String result = shellRun("ip addr | grep \"inet 10.\" | grep net");
        Log.d(TAG, "initCellularInterfaces: " + result);
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            Log.d(TAG, "initCellularInterfaces: " + matcher.group());
            String[] tmp = result.split(" ");
            Log.d(TAG, "initCellularInterfaces: " + tmp[tmp.length - 1]);
            Config.CELLULAR_INTERFACE = tmp[tmp.length - 1];
        }
    }

    public static String shellRun(String command) {
        Runtime runtime = Runtime.getRuntime();
        Log.d(TAG, "shellRun: " + command);
        try {
            java.lang.Process process = runtime.exec(new String[]{"/system/bin/sh", "-c", command});
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
