package com.github.kencordero.rishi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{   

    private static String DB_PATH = "/data/data/your.package/databases/";   
    private static String DB_NAME = "rishi.db3";
    private static int DB_VERSION = 1;

    private static short WRITE_BUFFER_SIZE = 8192;

    private Context context;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context Current application context
     */
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
    }   

  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public synchronized void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }
        else
        {
            //By calling this method and empty database will be created into the default system path
               //of your application so we are going to be able to overwrite that database with our database.
            this.getWritableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;

            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }catch(SQLException e){
            e.printStackTrace();
            //database does't exist yet.
        }catch(Exception e){
            e.printStackTrace();
        }

        if(checkDB != null){
            if(checkDB.getVersion() < DB_VERSION){
                checkDB.close();
                return false;
            }else{ 
                checkDB.close();
                return true;
            }
        }

        return false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring bytestream.
     */
    private void copyDataBase() throws IOException{     
        //Open your local db as the input stream    
        InputStream myInput = context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[WRITE_BUFFER_SIZE];
        int length;
        while ((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        this.getWritableDatabase().close();
    }

    @Override
    public synchronized void close() {
            super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   
    }

    /**
     * Delete database and create new one or copy from assets if exists.
     */
    public void clearDatabase(){      
        context.deleteDatabase(DB_NAME);
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getWritableDatabase().close();
    }
}