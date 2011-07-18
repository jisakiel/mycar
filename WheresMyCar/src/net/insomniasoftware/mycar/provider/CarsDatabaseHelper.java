package net.insomniasoftware.mycar.provider;

import net.insomniasoftware.mycar.provider.CarsInfo.Cars;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarsDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "CarsDatabaseHelper";
	
	private static final int DATABASE_VERSION = 3;
		// V2: Latitude and longitude are REALs. Name not null.
		// V3: Added 'resource_type' column.

    private static final String DATABASE_NAME = "cars.db";
    
    public static final String CARS_TABLE = CarsProvider.TABLE_NAME;
    
	public CarsDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + CARS_TABLE + " (" +
				Cars._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				Cars.NAME + " TEXT NOT NULL, " +
				Cars.LATITUDE + " REAL, " +
				Cars.LONGITUDE + " REAL, " +
				Cars.ACCURACY + " REAL, " +
				Cars.DATE + " INTEGER, " +
				Cars.RESOURCE_URL + " TEXT, " +
				Cars.RESOURCE_TYPE + " INT, " +
				Cars.MAC_ADDRESS + " TEXT" +
			");");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade");
		//TODO revisar
		if (oldVersion < newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + CARS_TABLE + ";");
			onCreate(db);
		}
	}

}
