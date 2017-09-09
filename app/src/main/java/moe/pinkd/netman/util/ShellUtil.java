package moe.pinkd.netman.util;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.pinkd.netman.App;
import moe.pinkd.netman.R;
import moe.pinkd.netman.bean.IptablesClause;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.ui.activity.NoRootActivity;

/**
 * Created by PinkD on 2017/5/11.
 * ShellUtil
 */

public class ShellUtil {
    private static final String TAG = "ShellUtil";
    private static final String SU = "su";
    private static final String _C = "-c";
    private static final String ID = "id";
    private static final String UID_EQUALS_0 = "id=0";
    private static final String ERROR = "ERROR";
    private static final boolean SKIP_ROOT = false;
    private static final boolean SKIP_CELLULAR = false;
    private static boolean isRoot;
    private static String param;

    private static boolean rootAvailable() {
        if (isRoot) {
            return true;
        }
        String result = ShellUtil.shellRun(SU, _C, ID);
        if (result.contains(UID_EQUALS_0)) {
            isRoot = true;
            param = _C;
        }
        return isRoot;
    }

    private static void SURun(List<IptablesClause> iptablesClauses) {
        String[] clauses = new String[iptablesClauses.size()];
        for (int i = 0; i < iptablesClauses.size(); i++) {
            clauses[i] = iptablesClauses.get(i).toString();
        }
        SURun(clauses);
    }

    @Nullable
    public static List<String> SURun(String[] commands) {
        List<String> result = new ArrayList<>(commands.length);
        String line;
        for (String command : commands) {
            line = SURun(command);
            if (ERROR.equals(line)) {
                return null;
            } else {
                result.add(line);
            }
        }
        return result;
    }

    public static String SURun(String command) throws RootNotAvailableException {
        if (rootAvailable()) {
            String result = shellRun(SU, param, command);
            Log.d(TAG, "SURun: " + command + " ---> " + result);
            return result;
        } else {
            if (!SKIP_ROOT) {
                App.getContext().startActivity(new Intent(App.getContext(), NoRootActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
            return ERROR;
        }
    }

    public static void init(Context context) {
        ShellUtil.initCellularInterfaces(context);
        initIptables();
    }


    public static void banApp(Context context, int uid, int status) {
        clearTableWithUid(uid);
        List<IptablesClause> commands = new ArrayList<>();
        if ((status & Config.ALL_MASK) == Config.ALL_MASK) {
            Log.d(TAG, "banApp: ALL_MASK");
            IptablesClause iptablesClause = new IptablesClause(uid, Config.ALL_MASK);
            commands.add(iptablesClause);
            DatabaseUtil.writeRecord(iptablesClause);
            SURun(iptablesClause.toString());
            SharedPreferenceUtil.setNeedRestore(context);
            return;
        }
        if ((status & Config.CELLULAR_MASK) != 0) {
            Log.d(TAG, "banApp: CELLULAR_MASK");
            commands.add(new IptablesClause(uid, Config.CELLULAR_MASK));
        }
        if ((status & Config.WIFI_MASK) != 0) {
            Log.d(TAG, "banApp: WIFI_MASK");
            commands.add(new IptablesClause(uid, Config.WIFI_MASK));
        }
        if ((status & Config.VPN_MASK) != 0) {
            Log.d(TAG, "banApp: VPN_MASK");
            commands.add(new IptablesClause(uid, Config.VPN_MASK));
        }
        for (IptablesClause command : commands) {
            DatabaseUtil.writeRecord(command);
            SURun(command.toString());
        }
        SharedPreferenceUtil.setNeedRestore(context);
    }

    private static void clearTableWithUid(int uid) {
        SURun(new IptablesClause(uid, Config.ALL_MASK, false).toString());
        SURun(new IptablesClause(uid, Config.CELLULAR_MASK, false).toString());
        SURun(new IptablesClause(uid, Config.WIFI_MASK, false).toString());
        SURun(new IptablesClause(uid, Config.VPN_MASK, false).toString());
        DatabaseUtil.clearRecord(uid);
    }

    public static void initCellularInterfaces(Context context) {
        if (SharedPreferenceUtil.loadCellularInterfaceName(context)) {
            return;
        }
//        Pattern pattern = Pattern.compile("10(\\.[0-9]{1,3}){3}");
//        String result = shellRun("ip addr | grep \"inet 10.\" | grep net");

        Pattern pattern = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3}");
        String result = SKIP_CELLULAR ? "eth0 10.0.0.172" : shellRun("ip addr | grep \"inet \" | grep rmnet");
        Log.d(TAG, "initCellularInterfaces: " + result);
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            Log.d(TAG, "initCellularInterfaces: " + matcher.group());
            String[] tmp = result.split(" ");
            Log.d(TAG, "initCellularInterfaces: " + tmp[tmp.length - 1]);
            Config.CELLULAR_INTERFACE = tmp[tmp.length - 1];
            SharedPreferenceUtil.saveCellularInterfaceName(context);
        }
    }

    private static boolean ruleExists() {
        if (SURun("iptables -t filter -L").contains("net_man")) {
            Log.d(TAG, "initIptables: Chain net_man exists");
            return true;
        }
        return false;
    }

    public static void initIptables() {
        if (!ruleExists()) {
            SURun(new String[]{
                    "iptables -t filter -N net_man",
                    "iptables -t filter -I OUTPUT 1 -j net_man",
                    "iptables -t filter -I FORWARD 1 -j net_man"
            });
        }
    }

    public static String shellRun(String shell, String param, String command) {
        Runtime runtime = Runtime.getRuntime();
        Log.d(TAG, "shellRun: " + command);
        try {
            java.lang.Process process = runtime.exec(new String[]{shell, param, command});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String tmp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((tmp = bufferedReader.readLine()) != null) {
                stringBuilder.append(tmp);
            }
            process.destroy();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static String shellRun(String command) {
        return shellRun("/system/bin/sh", "-c", command);
    }

    public static void restoreRules(Context context) {
        if (ruleExists()) {
            Log.d(TAG, "restoreRules: return");
            return;
        }
        Log.d(TAG, "restoreRules: restore");
        init(context);
        DatabaseUtil.init(context);
        Map<Integer, Integer> record = DatabaseUtil.readRecord();
        List<IptablesClause> iptablesClauses = new ArrayList<>();
        for (Integer uid : record.keySet()) {
            iptablesClauses.add(new IptablesClause(uid, record.get(uid)));
        }
        SURun(iptablesClauses);
        Toast.makeText(context, R.string.operation_done, Toast.LENGTH_SHORT).show();
    }

    private static class RootNotAvailableException extends RuntimeException {
        public RootNotAvailableException(String message) {
            super(message);
        }
    }

}
