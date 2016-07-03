package de.circuitco.materialirc.config;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 03/07/16.
 */
public class ChannelTable {
    public static final String TABLE_NAME = "channels";

    public static final String ID = "id";
    public static final String CHANNEL_NAME = "name";
    public static final String USER_CONFIG_ID = "user_config_id";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY," +
            USER_CONFIG_ID + "INTEGER NOT NULL," +
            CHANNEL_NAME + "TEXT)";
    public static final String CREATE_CONSTRAINT = "CREATE UNIQUE INDEX dupe_channel ON " + TABLE_NAME +
            " (" + CHANNEL_NAME + "," + USER_CONFIG_ID + ")";

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_CONSTRAINT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static List<String> getChannels(SQLiteDatabase database, long userConfigId) {
        Cursor cur = database.query(TABLE_NAME, new String[] {CHANNEL_NAME}, USER_CONFIG_ID + " = ?", new String[] {userConfigId+""},
                null, null, null);

        List<String> channels = new ArrayList<>();

        while (cur.moveToNext()) {
            channels.add(cur.getString(0));
        }

        cur.close();

        return channels;
    }

    public static long putChannels(SQLiteDatabase database, long userConfigId, List<String> channelNames) {
        // Purge config id and reinsert all channels
        database.delete(TABLE_NAME, USER_CONFIG_ID + " = ?", new String[] {userConfigId+""});

        ContentValues values = new ContentValues(2);
        values.put(USER_CONFIG_ID, userConfigId);

        int channelsInserted = 0;
        for ( String channel : channelNames ) {
            values.put(CHANNEL_NAME, channel);
            if ( database.insert(TABLE_NAME, null, values) > 0 ) {
                ++channelsInserted;
            }
        }

        return channelsInserted;
    }
}
