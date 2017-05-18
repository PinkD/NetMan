package moe.pinkd.netman.bean;

import android.support.annotation.Nullable;

import moe.pinkd.netman.config.Config;

/**
 * Created by PinkD on 2017/5/13.
 * clause for iptables
 */

public class IptablesClause {

    private int uid;
    private int mask;
    private boolean ban;

    public IptablesClause(int uid, int mask, boolean ban) {
        this.uid = uid;
        this.mask = mask;
        this.ban = ban;
    }

    public IptablesClause(int uid, int mask) {
        this(uid, mask, true);
    }

    @Nullable
    @Override
    public String toString() {
        char AorD = ban ? 'A' : 'D';
        if ((mask & Config.ALL_MASK) == Config.ALL_MASK) {
            return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -j REJECT";
        } else {
            if ((mask & Config.CELLULAR_MASK) == Config.CELLULAR_MASK) {
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -o " + Config.CELLULAR_INTERFACE + " -j REJECT";
//                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -o rmnet0 -j REJECT";
            }
            if ((mask & Config.WIFI_MASK) == Config.WIFI_MASK) {
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -o wlan0  -j REJECT";
            }
            if (mask == Config.VPN_MASK) {
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -o tun0  -j REJECT";
            }
        }
        return null;
    }
}
