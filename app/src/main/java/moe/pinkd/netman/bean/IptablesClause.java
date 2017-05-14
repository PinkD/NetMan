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
        this.uid = uid;
        this.mask = mask;
    }

    @Nullable
    @Override
    public String toString() {//TODO get network interfaces
        char AorD = ban ? 'A' : 'D';
        switch (mask) {
            case Config.ALL_MASK:
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -j REJECT";
            case Config.CELLULAR_MASK:
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -j REJECT";
            case Config.WIFI_MASK:
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -j REJECT";
            case Config.VPN_MASK:
                return "iptables -t filter -" + AorD + " net_man -m owner --uid-owner " + uid + " -j REJECT";
        }
        return null;
    }
}
