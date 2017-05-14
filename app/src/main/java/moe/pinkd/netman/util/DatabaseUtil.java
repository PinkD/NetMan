package moe.pinkd.netman.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Map;

import moe.pinkd.netman.config.DatabaseOpener;

/**
 * Created by PinkD on 2017/5/13.
 * DatabaseUtil
 */

public class DatabaseUtil {
    private static DatabaseOpener databaseOpener;
    private static final Object LOCK = new Object();

    public static void init(Context context) {
        databaseOpener = new DatabaseOpener(context, "settings.db", null, 1);
    }

    public static Map<Integer, Integer> readRecord() {
        SparseIntArray sparseIntArray = new SparseIntArray();
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
        boolean update = readRecord().get(uid) > 0;
        synchronized (LOCK) {
            SQLiteDatabase database = databaseOpener.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", uid);
            contentValues.put("status", status);
            if (update) {
                database.update("config", contentValues, "uid = ?", new String[]{String.valueOf(uid)});
            } else {
                database.insert("config", null, contentValues);
            }
            database.close();
        }
    }

}
