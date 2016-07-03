package de.circuitco.materialirc.config;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.pircbotx.Configuration;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 12/06/16.
 */
public class UserConfig implements Serializable {
    private static final String LOG_TAG = "UserConfig";

    public static final String ID = "id";
    public static final String CONFIG_NAME = "nickname";
    public static final String USERNAME = "username";
    public static final String SERVER_ADDRESS = "server_address";
    public static final String SERVER_PORT = "server_port";
    public static final String ALTERNATE_SERVER_ADDRESS = "alt_server_address";
    public static final String ALTERNATE_SERVER_PORT = "alt_server_port";
    public static final String AUTORUN = "autorun_line";
    public static final String NICKSERV_PASSWORD = "nickserv_password";

    public static final String[] fullProjection = new String[] {
            ID, CONFIG_NAME, USERNAME, SERVER_ADDRESS, SERVER_PORT,
            ALTERNATE_SERVER_ADDRESS, ALTERNATE_SERVER_PORT,
            AUTORUN, NICKSERV_PASSWORD
    };

    public long id = UserConfigDb.INVALID_ID;
    public String name = null;
    public String username = null;
    public String serverAddress = null;
    public int serverPort = UserConfigDb.INVALID_ID;
    public String alternateServerAddress = null;
    public int alternateServerPort = UserConfigDb.INVALID_ID;
    public String autorunLine = null;
    public String nickservPassword = null;
    public List<String> autojoinChannels = new ArrayList<>();

    private static UserConfigDb db = null;

    public UserConfig() {
    }

    public UserConfig(String name) {
        this.name = name;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(CONFIG_NAME, name);
        values.put(USERNAME, username);
        values.put(SERVER_ADDRESS, serverAddress);
        values.put(SERVER_PORT, serverPort);
        values.put(ALTERNATE_SERVER_ADDRESS, alternateServerAddress);
        values.put(ALTERNATE_SERVER_PORT, alternateServerPort);
        values.put(AUTORUN, autorunLine);
        values.put(NICKSERV_PASSWORD, nickservPassword);
        return values;
    }

    public Configuration toConfig() {
        Configuration.Builder builder = new Configuration.Builder();

        if ( serverAddress != null && serverPort != UserConfigDb.INVALID_ID )
            builder.addServer(serverAddress, serverPort);
        if ( alternateServerAddress != null && alternateServerPort != UserConfigDb.INVALID_ID )
            builder.addServer(alternateServerAddress, alternateServerPort);

        builder.setAutoReconnect(true);
        builder.setAutoReconnectAttempts(100);
        builder.setAutoReconnectDelay(2400);

        // If the user enters too much text, we'll automatically have it split, woo!
        builder.setAutoSplitMessage(true);

        // If we get the public IP address for this user (ip.appspot.com?)
        // then we can use it to accept dcc connections.
        // The problem with this bot framework is targeting static servers, where the
        // public ip address rarely changes. I suppose disconnection could prompt
        // a rebuild of the config and we retry connecting.
        //builder.setDccPublicAddress()

        builder.setName(username);
        builder.setRealName(username);
        builder.setLogin(username);

        if ( nickservPassword != null ) {
            builder.setNickservNick("NickServ");
            builder.setNickservDelayJoin(true);
            builder.setNickservPassword(nickservPassword);
            // string used to tell when nickserv reports success
            // builder.setNickservOnSuccess()
        }

        builder.setWebIrcEnabled(false);
        builder.setSnapshotsEnabled(false); // might save some cpu since no channel user list clone

        // Must start the ident server if setting this true.
        //builder.setIdentServerEnabled(true);

        builder.setMessageDelay(750);
        builder.setEncoding(Charset.forName("UTF-8"));
        builder.addAutoJoinChannels(autojoinChannels);

        return builder.buildConfiguration();
    }

    public String getString(String key) {
        switch ( key ) {
            case ALTERNATE_SERVER_ADDRESS:
                return alternateServerAddress;
            case ALTERNATE_SERVER_PORT:
                if ( alternateServerPort == UserConfigDb.INVALID_ID ) return "";
                return alternateServerPort+"";
            case SERVER_ADDRESS:
                return serverAddress;
            case SERVER_PORT:
                if ( serverPort == UserConfigDb.INVALID_ID ) return "";
                return serverPort+"";
            case AUTORUN:
                return autorunLine;
            case CONFIG_NAME:
                return name;
            case ID:
                if ( id == UserConfigDb.INVALID_ID ) return "";
                return id+"";
            case USERNAME:
                return username;
        }
        return "";
    }

    public void putString(String key, String value) {
        // If the value is an int, we'll try and lazy parse it up here.
        int integer = -1;
        try {
            integer = Integer.valueOf(value);
        } catch ( Exception e ){
            // Not an integer.
        }

        switch ( key ) {
            case ALTERNATE_SERVER_ADDRESS:
                alternateServerAddress = value;
                break;
            case ALTERNATE_SERVER_PORT:
                if ( integer != UserConfigDb.INVALID_ID ) {
                    alternateServerPort = integer;
                }
                break;
            case SERVER_ADDRESS:
                serverAddress = value;
                break;
            case SERVER_PORT:
                if ( integer != UserConfigDb.INVALID_ID ) {
                    serverPort =  integer;
                }
                break;
            case AUTORUN:
                autorunLine = value;
                break;
            case CONFIG_NAME:
                name = value;
                break;
            case ID:
                // Don't want to support this.
                //id+"";
                break;
            case USERNAME:
                username = value;
                break;
            default:
                Log.e(LOG_TAG, "Attempted to set UserConfig which is not supported");
                break;
        }
    }
}
