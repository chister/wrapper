package com.wrapper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
	// @Id
    public static final String KEY_ROWID = "_id";
    // Columns
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";    
    // Log flag
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "mobile";
    private static final String DATABASE_TABLE = "site";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table site (_id integer primary key autoincrement, "
        + "url text not null, title text not null, " 
        + "description text not null);";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS site");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a site into the database---
    public long insertSite(String url, String title, String description) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_URL, url);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DESCRIPTION, description);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular site---
    public boolean deleteSite(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the sites---
    public Cursor getAllSites() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_URL,
        		KEY_TITLE,
                KEY_DESCRIPTION}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves a particular site---
    public Cursor getSite(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_URL, 
                		KEY_TITLE,
                		KEY_DESCRIPTION
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a site---
    public boolean updateSite(long rowId, String isbn, 
    String title, String publisher) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_URL, isbn);
        args.put(KEY_TITLE, title);
        args.put(KEY_DESCRIPTION, publisher);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
}
