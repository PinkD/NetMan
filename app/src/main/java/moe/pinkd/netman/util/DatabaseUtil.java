package moe.pinkd.netman.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import moe.pinkd.netman.bean.IptablesClause;
import moe.pinkd.netman.config.DatabaseOpener;

/**
 * Created by PinkD on 2017/5/13.
 * DatabaseUtil
 */

public class DatabaseUtil {
    private static DatabaseOpener databaseOpener;
    private static final Object LOCK = new Object();

    public static void init(Context context) {
        if (databaseOpener == null) {
            databaseOpener = new DatabaseOpener(context, "settings.db", null, 1);
        }
    }

    public static Map<Integer, Integer> readRecord() {
        Map<Integer, Integer> statuses = new HashMap<>();
        synchronized (LOCK) {
            SQLiteDatabase database = databaseOpener.getReadableDatabase();
            Cursor cursor = database.query("config", new String[]{"uid", "status"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                statuses.put(cursor.getInt(cursor.getColumnIndex("uid")), cursor.getInt(cursor.getColumnIndex("status")));
            }
            cursor.close();
            database.close();
        }
        return statuses;
    }

    public static void writeRecord(int uid, int status) {
        synchronized (LOCK) {
            SQLiteDatabase database = databaseOpener.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", uid);
            contentValues.put("status", status);
            database.insert("config", null, contentValues);
            database.close();
        }
    }

    public static void writeRecord(IptablesClause iptablesClause) {
        writeRecord(iptablesClause.getUid(), iptablesClause.getMask());
    }

    public static void clearRecord(int uid) {
        synchronized (LOCK) {
            SQLiteDatabase database = databaseOpener.getWritableDatabase();
            database.delete("config", "uid = ?", new String[]{String.valueOf(uid)});
            database.close();
        }
    }
}
