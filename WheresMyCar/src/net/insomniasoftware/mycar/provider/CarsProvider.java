package net.insomniasoftware.mycar.provider;

import java.util.HashMap;

import net.insomniasoftware.mycar.provider.CarsInfo.Cars;

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
import android.text.TextUtils;
import android.util.Log;

public class CarsProvider extends ContentProvider {
	
	private static final String TAG = "CarsProvider";
	
	public static final String TABLE_NAME = "cars";
	
	private static final int CARS = 1;
    private static final int CARS_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(CarsInfo.AUTHORITY, "cars", CARS);
        sURIMatcher.addURI(CarsInfo.AUTHORITY, "cars/#", CARS_ID);
    }
    
    private static final HashMap<String, String> sCarsProjectionMap;
    static {

        // Cars projection map
        sCarsProjectionMap = new HashMap<String, String>();
        sCarsProjectionMap.put(Cars._ID, Cars._ID);
        sCarsProjectionMap.put(Cars.NAME, Cars.NAME);
        sCarsProjectionMap.put(Cars.LATITUDE, Cars.LATITUDE);
        sCarsProjectionMap.put(Cars.LONGITUDE, Cars.LONGITUDE);
        sCarsProjectionMap.put(Cars.ACCURACY, Cars.ACCURACY);
        sCarsProjectionMap.put(Cars.DATE, Cars.DATE);
        sCarsProjectionMap.put(Cars.RESOURCE, Cars.RESOURCE);
        sCarsProjectionMap.put(Cars.MAC_ADDRESS, Cars.MAC_ADDRESS);
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
                qb.setTables(TABLE_NAME);
                qb.setProjectionMap(sCarsProjectionMap);
                break;
            }

            case CARS_ID: {
                qb.setTables(TABLE_NAME);
                qb.setProjectionMap(sCarsProjectionMap);
                qb.appendWhere(TABLE_NAME + "." + Cars._ID + "=");
                qb.appendWhere(uri.getPathSegments().get(1));
                break;
            }
            
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
        
        final SQLiteDatabase db = sDbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder, null);
        
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), CarsInfo.CONTENT_URI);
        }
        return c;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = mCarsInserter.insert(values);
        if (rowId > 0) {
        	notifyChange();
            return ContentUris.withAppendedId(uri, rowId);
        }
        return null;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		final SQLiteDatabase db = sDbHelper.getWritableDatabase();
        String where;
        final int match = sURIMatcher.match(uri);
        
        switch (match) {
        case CARS: {
        	where = selection;
            break;
        }

        case CARS_ID: {
        	String whereId = Cars._ID + "=" + uri.getPathSegments().get(1);
        	if (TextUtils.isEmpty(selection))
        		where = whereId;
        	else where = "(" + selection + ") AND (" + whereId + ")";
            break;
        }
        
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
    }
        int count = db.update(TABLE_NAME, values, where, selectionArgs);
    	notifyChange();
        return count;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = sDbHelper.getWritableDatabase();
		String where;
		final int match = sURIMatcher.match(uri);
		switch (match) {
			case CARS:
				where = selection;
				break;
				
			case CARS_ID:
				where = TABLE_NAME + "." + Cars._ID + "=" + uri.getPathSegments().get(1);
				break;
				
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
		int count = db.delete(TABLE_NAME, where, selectionArgs);
		notifyChange();
		return count;
	}
	
	@Override
	public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case CARS:
                return Cars.CONTENT_TYPE;
            case CARS_ID:
                return Cars.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
	}
	
    protected void notifyChange() {
        getContext().getContentResolver().notifyChange(CarsInfo.CONTENT_URI, null,
                false /* wake up sync adapters */);
    }

}
