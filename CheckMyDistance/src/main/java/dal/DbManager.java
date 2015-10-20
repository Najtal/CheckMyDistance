package dal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import bizz.Position;
import exceptions.FatalException;
import exceptions.InvalidArgumentException;

/**
 * This class manage all the datas that flow thought the app.
 * 
 * This class is mostly inspired by
 *	http://www.vogella.com/articles/AndroidSQLite/article.html
 * 
 * @author D13122978
 *
 */
public class DbManager {
	
	// DB PARAMETERS : 
	private static final String DATABASE_NAME = "Positions";
	private static final String DATABASE_TABLE_POSITIONS = "Positions_details";
	private static final int DATABASE_VERSION = 1;

	// ANDROID VARIABLE
	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase dbr;
	

	// DB VARIABLE : These are the names of the columns the table will contain
	public static final String KEY_POS_ROWID = "_id";
	public static final String KEY_POS_NAME = "pos_name";
	public static final String KEY_POS_LAT = "pos_lat";
	public static final String KEY_POS_LON = "pos_long";
	public static final String KEY_POS_FAVORITE = "favorite";

	// CREATE STATEMENT : This is the string containing the SQL database create statement
	private static final String DATABASE_CREATE_POSITIONS = "CREATE TABLE " 
			+ DATABASE_TABLE_POSITIONS + " ("+KEY_POS_ROWID+" integer primary key autoincrement, "
			+ " " + KEY_POS_NAME + " VARCHAR(50) not null, " 
			+ " " + KEY_POS_LAT + " DOUBLE not null, "
			+ " " + KEY_POS_LON + " DOUBLE not null, "
			+ " " + KEY_POS_FAVORITE + " BOOLEAN not null DEFAULT 0);";

	
	/**
	 * Constructor
	 * @param ctx Context of the Activity
	 */
	public DbManager(Context ctx) {
		// Context is a way that Android transfers info about Activities and apps.
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		//spm = new SharedPreferencesManager();
	}


	// This is the helper class that will create the dB if it doesn't exist and
	// upgrades it if the structure has changed. It needs a constructor, an
	// onCreate() method and an onUpgrade() method
	/**
	 * This is the helper class that will create the dB if it doesn't exist and
	 * upgrades it if the structure has changed. It needs a constructor, an
	 * onCreate() method and an onUpgrade() method
	 * @author D13122978
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		// constructor for your dB helper class. This code is standard. You've set up the parameter values for the constructor already-database name,etc
		/**
		 * Consructor
		 * @param context the context of the app
		 */
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// The "Database_create" string below needs to contain the SQL
			// statement needed to create the dB
			db.execSQL(DATABASE_CREATE_POSITIONS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*
			 * If you want to change the structure of your database, e.g. Add a
			 * new column to a table, the code will go head.. This method only
			 * triggers if the database version number has increased
			 */
		}
		
	} // end of the help class

	
	/**
	 * get a data base reader & writer
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		dbr = DBHelper.getWritableDatabase();
	}

	/**
	 * Close the data base reader & writer
	 */
	public void close() {
		DBHelper.close();
	}

	/**
	 * insert a position into the database
	 * @param name the name of the position
	 * @param lat the latitude of the position
	 * @param lon the longitude of the position
	 * @param fav if the posiion is considered as a favorite
	 * @return
	 */
	public long insertPosition(String name, double lat, double lon, boolean fav)	{
		
		long id = 0;
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_POS_NAME, name);
		initialValues.put(KEY_POS_LAT, lat);
		initialValues.put(KEY_POS_LON, lon);
		initialValues.put(KEY_POS_FAVORITE, fav);
		
		try {
			id = dbr.insert(DATABASE_TABLE_POSITIONS, null, initialValues);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return id;
		
	}

	/**
	 * Delete a position
	 * @param rowId id of the position o delete
	 * @return true if the position has beel deleted
	 */
	public boolean deletePosition(long rowId) {
		// delete statement. If any rows deleted (i.e. >0), returns true
		return dbr.delete(DATABASE_TABLE_POSITIONS, KEY_POS_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor with all positions
	 * @return a cursor with all the positions in it
	 */
	public Cursor getAllPositions() {

		return dbr.query(DATABASE_TABLE_POSITIONS, new String[] { 
				KEY_POS_ROWID, KEY_POS_NAME, KEY_POS_LAT, KEY_POS_LON, KEY_POS_FAVORITE }, 
				null, null, null, null,	null);

	}

	/**
	 * retrieves a particular row
	 * @param rowId the id of the position ou're looking for
	 * @return A cursor with the position you're looking for
	 * @throws SQLException
	 */
	public Cursor getPosition(long rowId) throws SQLException {

		return dbr.query(true, DATABASE_TABLE_POSITIONS, 
				new String[] { KEY_POS_ROWID, KEY_POS_NAME, KEY_POS_LAT, KEY_POS_LON, KEY_POS_FAVORITE }, KEY_POS_ROWID + "=" + rowId, null, null, null, null, null);

	}

	/**
	 *  update a position
	 * @param rowId the id of the row that has to be updated
	 * @param name the name of the position
	 * @param lat the latitude of the position
	 * @param lon the longitude of the position
	 * @param fav if the posiion is considered as a favorite
	 * @return
	 */
	public boolean updatePosition(long rowId, String name, double lat, double lon, boolean fav) {

		ContentValues args = new ContentValues();

		args.put(KEY_POS_NAME, name);
		args.put(KEY_POS_LAT, lat);
		args.put(KEY_POS_LON, lon);
		args.put(KEY_POS_FAVORITE, fav);
		
		return dbr.update(DATABASE_TABLE_POSITIONS, args, KEY_POS_ROWID + "=" + rowId, null) > 0;
	}

	public List<Position> getListPositions() {
		
		
		Cursor cp = this.getAllPositions();
		
		// remember where the app where first launched :)
		if (cp.getCount() < 2) {
			throw new FatalException("Please enter at least one Position");
		}
		
		List<Position> list = new ArrayList<Position>();		

					
		for(cp.moveToFirst(); cp.moveToNext(); cp.isAfterLast()) {
			boolean f = false;
			if (cp.getInt(4) == 1)
				f = true;
				
			try {
				Position p = new Position(cp.getDouble(2), cp.getDouble(3), cp.getString(1), f);
				list.add(p);
			} catch (InvalidArgumentException e) {
				throw new FatalException("Error while reading data");
			}
		}
		
		return list;
	}
	
}
