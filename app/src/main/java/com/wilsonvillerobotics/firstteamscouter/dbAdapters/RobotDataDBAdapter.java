package com.wilsonvillerobotics.firstteamscouter.dbAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;

public class RobotDataDBAdapter implements BaseColumns {
	public static final String TABLE_NAME = "robot_data";
    public static final String COLUMN_NAME_DRIVE_TRAIN_TYPE = "drive_train_type";
    public static final String COLUMN_NAME_WHEEL_TYPE = "wheel_type";
    public static final String COLUMN_NAME_NUMBER_WHEELS = "number_of_wheels";
    public static final String COLUMN_NAME_NUMBER_TOTE_STACKS = "number_of_tote_stacks";
    public static final String COLUMN_NAME_NUMBER_TOTES_PER_STACK = "number_of_totes_per_stack";
    public static final String COLUMN_NAME_NUMBER_CANS_AT_ONCE = "number_of_cans_robot_can_handle";
    public static final String COLUMN_NAME_GET_STEP_CANS = "robot_can_get_step_cans";
    public static final String COLUMN_NAME_PUT_TOTES_ON_STEP = "robot_can_put_totes_on_step";
    public static final String COLUMN_NAME_ROBOT_SOFTWARE_LANGUAGE = "robot_software_language";
    public static final String COLUMN_NAME_TOTE_MANIPULATOR_TYPE = "tote_manipulator_type";
    public static final String COLUMN_NAME_CAN_MANIPULATOR_TYPE = "can_manipulator_type";
    public static final String COLUMN_NAME_ROBOT_DRIVE_RANGE = "robot_drive_range";
    public static final String COLUMN_NAME_COOPERTITION = "team_does_coopertition";
    public static final String COLUMN_NAME_ROBOT_STACKS_FROM = "robot_stacks_from";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private final String allColumns[] = {
            _ID,
            COLUMN_NAME_DRIVE_TRAIN_TYPE,
            COLUMN_NAME_WHEEL_TYPE,
            COLUMN_NAME_NUMBER_WHEELS,
            COLUMN_NAME_NUMBER_TOTE_STACKS,
            COLUMN_NAME_NUMBER_TOTES_PER_STACK,
            COLUMN_NAME_NUMBER_CANS_AT_ONCE,
            COLUMN_NAME_GET_STEP_CANS,
            COLUMN_NAME_PUT_TOTES_ON_STEP
    };

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
    public RobotDataDBAdapter(Context ctx) {
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
    public RobotDataDBAdapter open() throws SQLException {
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
     * @param values
     * @return rowId or -1 if failed
     */
    public long createRobotDataEntry(HashMap<String, String> values){
        ContentValues initialValues = new ContentValues();
        if(values != null) {
            initialValues.put(COLUMN_NAME_DRIVE_TRAIN_TYPE, values.get(COLUMN_NAME_DRIVE_TRAIN_TYPE));
            initialValues.put(COLUMN_NAME_WHEEL_TYPE, values.get(COLUMN_NAME_WHEEL_TYPE));
            initialValues.put(COLUMN_NAME_NUMBER_WHEELS, values.get(COLUMN_NAME_NUMBER_WHEELS));
            initialValues.put(COLUMN_NAME_NUMBER_TOTE_STACKS, values.get(COLUMN_NAME_NUMBER_TOTE_STACKS));
            initialValues.put(COLUMN_NAME_NUMBER_TOTES_PER_STACK, values.get(COLUMN_NAME_NUMBER_TOTES_PER_STACK));
            initialValues.put(COLUMN_NAME_NUMBER_CANS_AT_ONCE, values.get(COLUMN_NAME_NUMBER_CANS_AT_ONCE));
            initialValues.put(COLUMN_NAME_GET_STEP_CANS, values.get(COLUMN_NAME_GET_STEP_CANS));
            initialValues.put(COLUMN_NAME_PUT_TOTES_ON_STEP, values.get(COLUMN_NAME_PUT_TOTES_ON_STEP));
        }
        return this.mDb.insert(TABLE_NAME, null, initialValues);
    }

    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteRobotDataEntry(long rowId) {

        return this.mDb.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all entries in the database
     * 
     * @return Cursor over all Match Data entries
     */
    public Cursor getAllRobotDataEntries() {

        return this.mDb.query(TABLE_NAME, new String[] {
                _ID, COLUMN_NAME_DRIVE_TRAIN_TYPE
        		}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * @param robotId
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public HashMap<String, String> getRobotDataEntry(long robotId) throws SQLException {
        HashMap<String, String> values = new HashMap<String, String>();
        Cursor mCursor = this.mDb.query(true, TABLE_NAME, allColumns,
                _ID + "=" + robotId, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            for(String k : mCursor.getColumnNames()) {
                values.put(k, mCursor.getString(mCursor.getColumnIndex(k)));
            }
        }
        return values;
    }

    /**
     * Update the entry.
     * 
     * @param rowId
     * @param values
     * @return true if the entry was successfully updated, false otherwise
     */
    public boolean updateRobotDataEntry(long rowId, HashMap<String, String> values){
        ContentValues args = new ContentValues();
        for(String k : values.keySet()) {
            args.put(k, values.get(k));
        }
        return this.mDb.update(TABLE_NAME, args, _ID + "=" + rowId, null) >0; 
    }

}
