package moe.pinkd.netman.config;

import java.util.List;

import moe.pinkd.netman.bean.AppStatus;

/**
 * Created by PinkD on 2017/5/13.
 * Config
 */

public class Config {
    public static String CELLULAR_INTERFACE;
    public static final int NONE_MASK = 0x000;
    public static final int CELLULAR_MASK = 0x001;
    public static final int WIFI_MASK = 0x010;
    public static final int VPN_MASK = 0x100;
    public static final int ALL_MASK = 0x111;
}
