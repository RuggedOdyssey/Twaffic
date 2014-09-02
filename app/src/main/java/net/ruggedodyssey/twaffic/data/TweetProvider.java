package net.ruggedodyssey.twaffic.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by daneel on 2014/08/25.
 */
public class TweetProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private TwafficUpdateDbHelper mOpenHelper;

    private static final int TWEET_ID = 100;
    private static final int TWEETS = 101;
    private static final int TWEET_WITH_DATE = 102;


    private static final SQLiteQueryBuilder sTweetQueryBuilder;

    static {
        sTweetQueryBuilder = new SQLiteQueryBuilder();
        sTweetQueryBuilder.setTables(TwafficUpdateContract.TweetEntry.TABLE_NAME);
    }

    private static final String sTweetSelection =
            TwafficUpdateContract.TweetEntry.TABLE_NAME +
                    "." + TwafficUpdateContract.TweetEntry.COLUMN_TWEET_Date + " = ?";

    private static final String sTweetSelectionById =
            TwafficUpdateContract.TweetEntry.TABLE_NAME +
                    "." + TwafficUpdateContract.TweetEntry.COLUMN_TWEET_Id + " = ?";

    private Cursor getTweetById(Uri uri, String[] projection, String sortOrder) {
        long id = TwafficUpdateContract.TweetEntry.getTweetIdFromUri(uri);

        return sTweetQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTweetSelectionById,
                new String[]{String.valueOf(id)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTweets(Uri uri, String[] projection, String sortOrder) {
        String[] selectionArgs = null;
        String selection;

        selection = sTweetSelection;

        return sTweetQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TwafficUpdateContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TwafficUpdateContract.PATH_TWEETS, TWEETS);
        matcher.addURI(authority, TwafficUpdateContract.PATH_TWEETS + "/*", TWEET_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TwafficUpdateDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "tweets/*"
            case TWEET_ID:
            {
                retCursor = getTweetById(uri, projection, sortOrder);
                break;
            }
            // "tweets"
            case TWEETS:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TwafficUpdateContract.TweetEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        TwafficUpdateContract.TweetEntry.COLUMN_TWEET_Date + " DESC"
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match){
            case TWEET_WITH_DATE:
                return TwafficUpdateContract.TweetEntry.CONTENT_TYPE;
            case TWEETS:
                return TwafficUpdateContract.TweetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TWEETS: {
                long _id = db.insert(TwafficUpdateContract.TweetEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TwafficUpdateContract.TweetEntry.buildTweetUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case TWEETS:
                rowsDeleted = db.delete(
                        TwafficUpdateContract.TweetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsUpdated;

            switch (match) {
                case TWEETS:
                    rowsUpdated = db.update(TwafficUpdateContract.TweetEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
    }
}
