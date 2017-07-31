package com.example.etaspare.stitchcounter;

/**
 * Created by ETASpare on 7/3/2017.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/* A custom Content Provider to do the database operations */
public class CounterProjectContentProvider extends ContentProvider{

    public static final String PROVIDER_NAME = "com.example.etaspare.stitchcounter.counterProject";

    /* A uri to do operations on cust_master table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/projects" );

    /* Constants to identify the requested operation */
    private static final int PROJECTS = 1;

    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "projects", PROJECTS);
    }

    StitchCounterDbHelper dbHelper; /* This content provider does the database operations by this object */
    SQLiteDatabase db;

    /* A callback method which is invoked when the content provider is starting up */
    @Override
    public boolean onCreate() {
        dbHelper = new StitchCounterDbHelper(getContext());
        db = dbHelper.getReadableDatabase();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /* A callback method which is by the default content uri */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri)==PROJECTS){
            return dbHelper.getAllProjects(db, projection, selection, selectionArgs, sortOrder);
        }else{
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }
}