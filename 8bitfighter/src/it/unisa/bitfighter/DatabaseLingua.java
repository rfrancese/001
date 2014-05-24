package it.unisa.bitfighter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseLingua extends SQLiteOpenHelper { 
	public static final String MYDATABASE_NAME = "8bitdatabase";
	public static final String MYDATABASE_TABLE_LINGUA = "lingua";
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_SCELTA = "sigla";
 
	public DatabaseLingua(Context context) {
        super(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		 String CREATE_LINGUA_TABLE = "CREATE TABLE " + MYDATABASE_TABLE_LINGUA + "(" + KEY_SCELTA + " TEXT"+")";
	        db.execSQL(CREATE_LINGUA_TABLE);
	        salvaLingua("niente"); //creo un'entrata con lingua = niente
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MYDATABASE_TABLE_LINGUA);
 
        // Create tables again
        onCreate(db);
    }
	
	
    // Aggiungi lingua
	public void salvaLingua(String lingua) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_SCELTA, lingua); 
	    
	 
	    // Inserting Row
	    db.insert(MYDATABASE_TABLE_LINGUA, null, values);
	    db.close(); // Closing database connection
}
	
//	public String ottieniLingua() {
//	    SQLiteDatabase db = this.getReadableDatabase();
//	    String returnlingua;
//	 
//	    Cursor cursor = db.query(MYDATABASE_TABLE_LINGUA, new String[] { KEY_SCELTA }, KEY_SCELTA + "=?",
//	            new String[] { String.valueOf(id) }, null, null, null, null);
//	    if (cursor != null)
//	        cursor.moveToFirst();
//	 
//	    Contact contact = new Contact(Integer.parseInt(cursor.getString(0)));
//	    
//	    return returnlingua;
//	}


} 
                
