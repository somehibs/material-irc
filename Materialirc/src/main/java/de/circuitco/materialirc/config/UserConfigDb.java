package de.circuitco.materialirc.config;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 12/06/16.
 */
public class UserConfigDb extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "irc_user_config";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("+
            UserConfig.ID + " INTEGER PRIMARY KEY NOT NULL, " +
            UserConfig.CONFIG_NAME + " TEXT NOT NULL," +
            UserConfig.USERNAME + " TEXT NOT NULL,"+
            UserConfig.SERVER_ADDRESS + " TEXT NOT NULL,"+
            UserConfig.SERVER_PORT + " INTEGER,"+
            UserConfig.ALTERNATE_SERVER_ADDRESS + " TEXT NOT NULL,"+
            UserConfig.ALTERNATE_SERVER_PORT+ " INTEGER,"+
            UserConfig.NICKSERV_PASSWORD + " TEXT,"+
            UserConfig.AUTORUN + " TEXT);";
    public static final int INVALID_ID = -1;

    private static SQLiteDatabase db = null;

    public UserConfigDb(Context context) {
        super(context, TABLE_NAME, null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static Cursor query(String where, String[] whereArgs, String groupBy, String orderBy, String limit) {
        return db.query(TABLE_NAME, UserConfig.fullProjection, where, whereArgs, groupBy, null, orderBy, limit);
    }

    public UserConfig getConfig(String config_name) {
        UserConfig config = null;

        Cursor cur = query(UserConfig.CONFIG_NAME + " = ?", new String[]{config_name}, null, null, null);

        if ( cur.moveToNext() ) {
            config = configFromCursor(cur);
        }

        return config;
    }

    public long insertConfig(UserConfig config) {
        return db.insert(TABLE_NAME, null, config.toValues());
    }

    public int updateConfig(UserConfig config) {
        // idiot, you can't update a config that's not in the db
        if ( config.id == INVALID_ID )
            return -1;

        ChannelTable.putChannels(db, config.id, config.autojoinChannels);

        return db.update(TABLE_NAME, config.toValues(), UserConfig.ID + " = ?", new String[] {config.id+""});
    }

    private UserConfig configFromCursor(Cursor cur) {
        UserConfig config = new UserConfig();

        config.id = cur.getLong(cur.getColumnIndex(UserConfig.ID));
        config.name = cur.getString(cur.getColumnIndex(UserConfig.CONFIG_NAME));
        config.username = cur.getString(cur.getColumnIndex(UserConfig.USERNAME));
        config.serverAddress = cur.getString(cur.getColumnIndex(UserConfig.SERVER_ADDRESS));
        config.serverPort = cur.getInt(cur.getColumnIndex(UserConfig.SERVER_PORT));
        config.alternateServerAddress = cur.getString(cur.getColumnIndex(UserConfig.ALTERNATE_SERVER_ADDRESS));
        config.alternateServerPort = cur.getInt(cur.getColumnIndex(UserConfig.ALTERNATE_SERVER_PORT));
        config.autorunLine = cur.getString(cur.getColumnIndex(UserConfig.AUTORUN));
        config.nickservPassword = cur.getString(cur.getColumnIndex(UserConfig.NICKSERV_PASSWORD));

        config.autojoinChannels = ChannelTable.getChannels(db, config.id);

        return config;
    }

    public boolean hasConfig() {
        Cursor cur = db.query(TABLE_NAME, new String[] {"COUNT(*)"}, null, null, null, null, null);
        cur.moveToFirst();
        boolean has = cur.getLong(0) > 0;
        cur.close();
        return has;
    }
}
