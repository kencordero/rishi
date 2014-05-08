package com.kentheken.rishi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	    private static final String DB_NAME = "rishi.db3";
	    private static final int DB_VERSION = 1;

	    private SQLiteDatabase mDatabase;
	    private final Context mContext;

	    public SQLiteDatabase getDb() {
	    	return mDatabase;
	    }
	    
	    /**
	     * Constructor
	     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	     * @param context
	     */
	    public DatabaseOpenHelper(Context context) {
	        super(context, DB_NAME, null, DB_VERSION);
	        this.mContext = context;
	        openDatabase();
	    }   

	  /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     * */
	    public void createDatabase() {

	        boolean dbExists = checkDatabase();

	        if (!dbExists) {
	            // By calling this method, an empty database will be created into the default system path
	            // of your application so we are able to overwrite that database with ours.
	            this.getReadableDatabase();

	            try {
	                copyDatabase();
	            } catch (IOException e) {
	                throw new Error("Error copying database");
	            }
	        }
	    }

	    /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDatabase(){
	        SQLiteDatabase database = null;

	        try {
	            database = SQLiteDatabase.openDatabase(getPath(), null, SQLiteDatabase.OPEN_READONLY);
	        } catch(SQLiteException e) {
	        	Log.e(this.getClass().toString(), "Error while checking db");	            
	        }

	        if (database != null)
	            database.close();

	        return database != null;
	    }

	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transferring bytestream.
	     * */
	    private void copyDatabase() throws IOException{
	        //Open your local db as the input stream
	        InputStream myInput = mContext.getAssets().open(DB_NAME);

	        //Open the empty db as the output stream
	        OutputStream myOutput = new FileOutputStream(getPath());

	        //transfer bytes from the inputfile to the outputfile
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = myInput.read(buffer)) > 0) {
	            myOutput.write(buffer, 0, length);
	        }

	        //Close the streams
	        myOutput.flush();
	        myOutput.close();
	        myInput.close();
	    }
	    
	    private String getPath() {
	    	return mContext.getFilesDir().getPath() + DB_NAME;
	    }

	    public SQLiteDatabase openDatabase() throws SQLException{
	        //Open the database	      
	    	if (mDatabase == null) {
	    		createDatabase();
	    		mDatabase = SQLiteDatabase.openDatabase(getPath(), null,  SQLiteDatabase.OPEN_READONLY);
	    	}
	        return mDatabase;
	    }

	    @Override
	    public synchronized void close() {
	            if(mDatabase != null)
	                mDatabase.close();

	            super.close();
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) { }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

	    // Add your public helper methods to access and get content from the database.
	    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	    // to create adapters for your views.
	
}