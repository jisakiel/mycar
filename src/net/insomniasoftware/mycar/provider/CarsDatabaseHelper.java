package net.insomniasoftware.mycar.provider;

import net.insomniasoftware.mycar.R;
import net.insomniasoftware.mycar.provider.Cars.CarsColumns;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarsDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "CarsDatabaseHelper";
	private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "cars.db";
    
    public static final String CARS_TABLE = "cars";
    
	public CarsDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + CARS_TABLE + " (" +
				CarsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				CarsColumns.NAME + " TEXT, " +
				CarsColumns.LATITUDE + " INTEGER, " +
				CarsColumns.LONGITUDE + " INTEGER, " +
				CarsColumns.ACCURACY + " REAL, " +
				CarsColumns.DATE + " INTEGER, " +
				CarsColumns.RESOURCE + " INTEGER, " +
				CarsColumns.MAC_ADDRESS + " TEXT" +
			");");
		
		createDefaultProfile(db);
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
	
	private void createDefaultProfile(SQLiteDatabase db){
		ContentValues values = new ContentValues();
		values.put(CarsColumns.NAME, "My Car");
		values.put(CarsColumns.RESOURCE, R.drawable.car);
		db.insertOrThrow(CARS_TABLE, null, values);
		
		Log.d(TAG, "Default car profile");
	}

}
