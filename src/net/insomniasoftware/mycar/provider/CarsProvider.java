package net.insomniasoftware.mycar.provider;

import java.util.HashMap;

import net.insomniasoftware.mycar.provider.Cars.CarsColumns;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class CarsProvider extends ContentProvider {
	
	private static final String TAG = "CarsProvider";
	
	private static final int CARS = 1;
    private static final int CARS_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(Cars.AUTHORITY, "cars", CARS);
        sURIMatcher.addURI(Cars.AUTHORITY, "cars/#", CARS_ID);
    }
    
    private static final HashMap<String, String> sCarsProjectionMap;
    static {

        // Cars projection map
        sCarsProjectionMap = new HashMap<String, String>();
        sCarsProjectionMap.put(CarsColumns._ID, CarsColumns._ID);
        sCarsProjectionMap.put(CarsColumns.NAME, CarsColumns.NAME);
        sCarsProjectionMap.put(CarsColumns.LATITUDE, CarsColumns.LATITUDE);
        sCarsProjectionMap.put(CarsColumns.LONGITUDE, CarsColumns.LONGITUDE);
        sCarsProjectionMap.put(CarsColumns.ACCURACY, CarsColumns.ACCURACY);
        sCarsProjectionMap.put(CarsColumns.DATE, CarsColumns.DATE);
        sCarsProjectionMap.put(CarsColumns.RESOURCE, CarsColumns.RESOURCE);
        sCarsProjectionMap.put(CarsColumns.MAC_ADDRESS, CarsColumns.MAC_ADDRESS);
    }

    private static CarsDatabaseHelper sDbHelper;
    private DatabaseUtils.InsertHelper mCarsInserter;
	
	@Override
	public boolean onCreate() {
		final Context context = getContext();
		
		Log.d(TAG, "OnCreate CarsProvider");

        sDbHelper = getDatabaseHelper(context);
        SQLiteDatabase db = sDbHelper.getWritableDatabase();
        mCarsInserter = new DatabaseUtils.InsertHelper(db, CarsDatabaseHelper.CARS_TABLE);

        return true;
	}
	
	private CarsDatabaseHelper getDatabaseHelper(Context context) {
		if (sDbHelper == null)
			sDbHelper = new CarsDatabaseHelper(context);
		return sDbHelper;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int match = sURIMatcher.match(uri);
        switch (match) {
            case CARS: {
                qb.setTables("cars");
                qb.setProjectionMap(sCarsProjectionMap);
                break;
            }

            case CARS_ID: {
                qb.setTables("cars");
                qb.setProjectionMap(sCarsProjectionMap);
                qb.appendWhere("cars._id=");
                qb.appendWhere(uri.getPathSegments().get(1));
                break;
            }
            
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
        
        final SQLiteDatabase db = sDbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder, null);
        
        //TODO notify
        //if (c != null) {
        //    c.setNotificationUri(getContext().getContentResolver(), CarsColumns.CONTENT_URI);
        //}
        return c;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = mCarsInserter.insert(values);
        if (rowId > 0) {
            //TODO notify
        	//notifyChange();
            return ContentUris.withAppendedId(uri, rowId);
        }
        return null;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case CARS:
                return CarsColumns.CONTENT_TYPE;
            case CARS_ID:
                return CarsColumns.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
	}

}
