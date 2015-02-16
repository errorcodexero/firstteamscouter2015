package com.wilsonvillerobotics.firstteamscouter.dbAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class PitPicturesDBAdapter implements BaseColumns {
	public static final String TABLE_NAME = "pit_pictures";
    public static final String COLUMN_NAME_PIT_ID = "pit_id";
    public static final String COLUMN_NAME_PICTURE_ID = "picture_id";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        
        @Override
    	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    public PitPicturesDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the FirstTeamScouter database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public PitPicturesDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
    }

    /**
     * Create a new entry. If the entry is successfully created return the new
     * rowId for that entry, otherwise return a -1 to indicate failure.
     * 
     * @param pit_picture_id
     * @param pit_id
     * @param picture_id
     * @return rowId or -1 if failed
     */
    public long createPitPicture(int pit_picture_id, int pit_id, int picture_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(_ID, pit_picture_id);
        initialValues.put(COLUMN_NAME_PIT_ID, pit_id);
        initialValues.put(COLUMN_NAME_PICTURE_ID, picture_id);
        return this.mDb.insert(TABLE_NAME, null, initialValues);
    }

    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deletePitPicture(long rowId) {

        return this.mDb.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all entries in the database
     * 
     * @return Cursor over all Match Data entries
     */
    public Cursor getAllPitPictures() {

        return this.mDb.query(TABLE_NAME, new String[] { _ID,
        		_ID, COLUMN_NAME_PIT_ID, COLUMN_NAME_PICTURE_ID
        		}, null, null, null, null, null);
    }

    public ArrayList<Long> getAllPictureIDsForPit(long pitId) {
        ArrayList<Long> idArray = new ArrayList<Long>();
        String WHERE = COLUMN_NAME_PIT_ID + "=" + pitId;
        Cursor C = this.mDb.query(TABLE_NAME, new String[] { _ID,
                _ID, COLUMN_NAME_PIT_ID, COLUMN_NAME_PICTURE_ID
        }, WHERE, null, null, null, null);

        while(C.moveToNext()) {
            idArray.add(C.getLong(C.getColumnIndexOrThrow(COLUMN_NAME_PICTURE_ID)));
        }
        return idArray;
    }

    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor getPitPicture(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, TABLE_NAME, new String[] { _ID, 
        		_ID, COLUMN_NAME_PIT_ID, COLUMN_NAME_PICTURE_ID
        		}, _ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the entry.
     * 
     * @param rowId
     * @param pit_picture_id
     * @param pit_id
     * @param picture_id
     * @return true if the entry was successfully updated, false otherwise
     */
    public boolean updatePitPicture(int rowId, int pit_picture_id, int pit_id, int picture_id){
        ContentValues args = new ContentValues();
        args.put(_ID, pit_picture_id);
        args.put(COLUMN_NAME_PIT_ID, pit_id);
        args.put(COLUMN_NAME_PICTURE_ID, picture_id);
        return this.mDb.update(TABLE_NAME, args, _ID + "=" + rowId, null) >0; 
    }

}
