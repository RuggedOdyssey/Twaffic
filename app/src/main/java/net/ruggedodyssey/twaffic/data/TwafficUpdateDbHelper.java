package net.ruggedodyssey.twaffic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.ruggedodyssey.twaffic.data.TwafficUpdateContract.TweetEntry;

/**
 * Created by daneel on 2014/08/25.
 */
public class TwafficUpdateDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "tweets.db";

    public TwafficUpdateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + TweetEntry.TABLE_NAME + " (" +
                TweetEntry._ID + " INTEGER PRIMARY KEY," +
                TweetEntry.COLUMN_TWEET_Date + " TEXT NULL, " +
                TweetEntry.COLUMN_TWEET_Text + " TEXT NOT NULL, " +
                TweetEntry.COLUMN_TWEET_Url + " TEXT NULL, " +
                TweetEntry.COLUMN_TWEET_Keywords + " TEXT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TweetEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
