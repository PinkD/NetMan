package moe.pinkd.netman.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PinkD on 2017/5/13.
 * DatabaseOpener
 */

public class DatabaseOpener extends SQLiteOpenHelper {

    public DatabaseOpener(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableConfig =
                "CREATE TABLE IF NOT EXISTS `config`(" +
                        "`uid`      INTEGER PRIMARY KEY," +
                        "`status`   INTEGER UNSIGNED    DEFAULT 0" +
                        ");";
        db.execSQL(createTableConfig);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `config`;");
        onCreate(db);
    }

}
