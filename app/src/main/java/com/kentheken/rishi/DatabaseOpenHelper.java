package com.kentheken.rishi;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseOpenHelper.class.getSimpleName();
	private static final String DB_NAME = "rishi.db3";
    private static final int DB_VERSION = 2;

    private static DatabaseOpenHelper sDbHelper;

    private SQLiteDatabase mDatabase;
    private final Context mContext;

    private DatabaseOpenHelper(Context appContext) {
        super(appContext, DB_NAME, null, DB_VERSION);
        mContext = appContext;
        openDatabase();
    }

    public static DatabaseOpenHelper get(Context context) {
        if (sDbHelper == null) {
            sDbHelper = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return sDbHelper;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    private void createDatabase() {
        boolean dbExists = checkDatabase();

        Log.i(TAG, "Database exists: " + dbExists);
        if (!dbExists) {
	        // By calling this method, an empty database will be created into the default system path
	        // of your application so we are able to overwrite that database with ours.
	        createNewDatabase();
        }
    }

    private void createNewDatabase() {
        this.getReadableDatabase();

        try {
            copyDatabase();
        } catch (IOException e) {
            throw new Error("Error copying database");

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
    private void copyDatabase() throws IOException {
        Log.i(TAG, "Copying database");
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(getPath());

        //transfer bytes from the input file to the output file
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
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createNewDatabase();
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to create adapters for your views.

    public String getText(String fileName, int language) {
        String languageCode = getLocaleCode(language);
        Cursor cursor = mDatabase.rawQuery("SELECT display_name " +
                "FROM imagelocale INNER JOIN image " +
                "ON imagelocale.image_id = image._id " +
                "INNER JOIN locale ON imagelocale.locale_id = locale._id " +
                "WHERE file_name = ? AND code = ?", new String[] {fileName, languageCode});
        String displayName = null;
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                do {
                    displayName = cursor.getString(cursor.getColumnIndex("display_name"));
                } while (cursor.moveToNext());
            } catch (CursorIndexOutOfBoundsException e) {
                Toast.makeText(mContext, "No text found for " + fileName, Toast.LENGTH_LONG).show();
            }
            finally {
                cursor.close();
            }
        }
        return displayName;
    }

    private String getLocaleCode(int language) {
        switch (language) {
            case TTSEngine.LANGUAGE_ENGLISH:
                return "en";
            case TTSEngine.LANGUAGE_MARATHI:
                return "mr";
            case TTSEngine.LANGUAGE_SPANISH:
                return "es";
            default:
                return "";
        }
    }
}
