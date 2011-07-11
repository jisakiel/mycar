package net.insomniasoftware.mycar.test;

import java.util.Date;

import net.insomniasoftware.mycar.provider.CarsInfo;
import net.insomniasoftware.mycar.provider.CarsInfo.Cars;
import net.insomniasoftware.mycar.provider.CarsProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

public class CarsProviderTestCase extends ProviderTestCase2<CarsProvider> {
	
	static final String TAG = "CarsProviderTestCase";

	public static final String CAR_NAME_1 = "MyCar1";
	public static final String CAR_NAME_2 = "MyCar2";
	public static final String CAR_NAME_3 = "MyCar3";
	
	public static final String CAR_RESOURCE_1 = "uri://car_resource_1";
	public static final String CAR_RESOURCE_2 = "uri://car_resource_2";
	public static final String CAR_RESOURCE_3 = "uri://car_resource_3";
	
	public static final String CAR_MAC_ADDRESS_1 = "00:c4:4b:21:99:07";

	public static final double LATITUDE = 40.392589;
	public static final double LONGITUDE = -3.658949;
	public static final double ACCURACY = 30;
	
	private MockContentResolver mResolver;

	public CarsProviderTestCase() {
		super(CarsProvider.class, CarsInfo.AUTHORITY);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = getMockContext();
        mResolver = getMockContentResolver();
        
        //Delete data
        deleteAllData();
    }
	
	@Override
    protected void tearDown() throws Exception {
		
		//Delete data
		deleteAllData();
		
        super.tearDown();
    }
	
	private void deleteAllData(){
		mResolver.delete(Cars.CONTENT_URI, null, null);
	}
	
	private int insertCar(String name){
		return insertCar(name, Cars.RESOURCE_TYPE_CARS_ICON, null, null);
	}
	
	private int insertCar(String name, int resourceType, String resource){
		return insertCar(name, resourceType, resource, null);
	}
	
	private int insertCar(String name, int resourceType, String resource, String mac){
		ContentValues values = new ContentValues();
		values.put(Cars.NAME, name);
		values.put(Cars.RESOURCE_TYPE, resource);
		values.put(Cars.RESOURCE_URL, resource);
		values.put(Cars.MAC_ADDRESS, mac);
		Uri uri = getMockContext().getContentResolver().insert(Cars.CONTENT_URI, values);
		return Integer.valueOf(uri.getLastPathSegment());
	}
	
	public void testOneCar(){
		
		// Insert
		int id = insertCar(CAR_NAME_1);
		assertTrue(id > 0);
		
		// Query
		Cursor c = mResolver.query(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), 
				null, null, null, Cars._ID);
		assertNotNull(c);
		assertEquals(c.getCount(), 1);
		c.moveToNext();
		assertEquals(c.getInt(c.getColumnIndex(Cars._ID)), id);
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_1);
		c.close();
		
		// Delete
		int count = mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), null, null);
		assertEquals(count, 1);
	}
	
	public void testOneCar2(){
		
		// Insert
		int id = insertCar(CAR_NAME_1, Cars.RESOURCE_TYPE_CARS_ICON, CAR_RESOURCE_1);
		assertTrue(id > 0);
		
		// Query
		Cursor c = mResolver.query(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), 
				null, null, null, Cars._ID);
		assertNotNull(c);
		assertEquals(c.getCount(), 1);
		c.moveToNext();
		assertEquals(c.getInt(c.getColumnIndex(Cars._ID)), id);
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_1);
		assertEquals(c.getInt(c.getColumnIndex(Cars.RESOURCE_TYPE)), Cars.RESOURCE_TYPE_CARS_ICON);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_1);
		c.close();
		
		// Delete
		int count = mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), null, null);
		assertEquals(count, 1);
	}
	
	public void testSomeCars(){
		
		// Insert
		int id;
		id = insertCar(CAR_NAME_1, Cars.RESOURCE_TYPE_CARS_ICON, CAR_RESOURCE_1);
		assertTrue(id > 0);
		id = insertCar(CAR_NAME_2, Cars.RESOURCE_TYPE_CARS_ICON, CAR_RESOURCE_2);
		assertTrue(id > 0);
		id = insertCar(CAR_NAME_3, Cars.RESOURCE_TYPE_PERSONAL_ICON, CAR_RESOURCE_3);
		assertTrue(id > 0);
		
		// Query
		Cursor c = mResolver.query(Cars.CONTENT_URI, null, null, null, Cars._ID);
		assertNotNull(c);
		assertEquals(c.getCount(), 3);

		c.moveToNext();
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_1);
		assertEquals(c.getInt(c.getColumnIndex(Cars.RESOURCE_TYPE)), Cars.RESOURCE_TYPE_CARS_ICON);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_1);
		
		c.moveToNext();
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_2);
		assertEquals(c.getInt(c.getColumnIndex(Cars.RESOURCE_TYPE)), Cars.RESOURCE_TYPE_CARS_ICON);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_2);
		
		c.moveToNext();
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_3);
		assertEquals(c.getInt(c.getColumnIndex(Cars.RESOURCE_TYPE)), Cars.RESOURCE_TYPE_PERSONAL_ICON);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_3);
		c.close();
		
		// Delete
		int count = mResolver.delete(Cars.CONTENT_URI, null, null);
		assertEquals(count, 3);
	}
	
	public void testOneCarWithMacAddress(){
		
		// Insert
		int id = insertCar(CAR_NAME_1, Cars.RESOURCE_TYPE_CARS_ICON, CAR_RESOURCE_1, CAR_MAC_ADDRESS_1);
		assertTrue(id > 0);
		
		// Query
		Cursor c = mResolver.query(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), 
				null, null, null, Cars._ID);
		assertNotNull(c);
		assertEquals(c.getCount(), 1);
		
		c.moveToNext();
		assertEquals(c.getInt(c.getColumnIndex(Cars._ID)), id);
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_1);
		assertEquals(c.getInt(c.getColumnIndex(Cars.RESOURCE_TYPE)), Cars.RESOURCE_TYPE_CARS_ICON);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_1);
		assertEquals(c.getString(c.getColumnIndex(Cars.MAC_ADDRESS)), CAR_MAC_ADDRESS_1);
		c.close();
		
		// Delete
		int count = mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), null, null);
		assertEquals(count, 1);
	}
	
	public void testInvalidCarName1(){
		try {
			insertCar(null);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testInvalidCarName2(){
		try {
			insertCar("");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testInvalidCarName3(){
		try {
			ContentValues values = new ContentValues();
			getMockContext().getContentResolver().insert(Cars.CONTENT_URI, values);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testInvalidResource1(){
		try {
			insertCar(CAR_NAME_1, Cars.RESOURCE_TYPE_PERSONAL_ICON, null);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testInvalidResource2(){
		try {
			insertCar(CAR_NAME_1, Cars.RESOURCE_TYPE_PERSONAL_ICON, "");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testInvalidResource3(){
		try {
			ContentValues values = new ContentValues();
			values.put(Cars.NAME, CAR_NAME_1);
			values.put(Cars.RESOURCE_TYPE, Cars.RESOURCE_TYPE_PERSONAL_ICON);
			getMockContext().getContentResolver().insert(Cars.CONTENT_URI, values);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testUpdateLocation(){
		
		// Insert
		int id = insertCar(CAR_NAME_1, Cars.RESOURCE_TYPE_CARS_ICON, CAR_RESOURCE_1, CAR_MAC_ADDRESS_1);
		assertTrue(id > 0);
		
		// Query
		Cursor c = mResolver.query(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), 
				null, null, null, Cars._ID);
		assertNotNull(c);
		assertEquals(c.getCount(), 1);
		c.moveToNext();
		assertEquals(c.getInt(c.getColumnIndex(Cars._ID)), id);
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_1);
		assertEquals(c.getInt(c.getColumnIndex(Cars.RESOURCE_TYPE)), Cars.RESOURCE_TYPE_CARS_ICON);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_1);
		assertNull(c.getString(c.getColumnIndex(Cars.LATITUDE)));
		assertNull(c.getString(c.getColumnIndex(Cars.LONGITUDE)));
		assertNull(c.getString(c.getColumnIndex(Cars.ACCURACY)));
		assertNull(c.getString(c.getColumnIndex(Cars.DATE)));
		c.close();

		// Update
		ContentValues values = new ContentValues();
		values.put(Cars.LATITUDE, LATITUDE);
		values.put(Cars.LONGITUDE, LONGITUDE);
		values.put(Cars.ACCURACY, ACCURACY);
		long date = new Date().getTime();
		values.put(Cars.DATE, date);
		
		int updatedRows = mResolver.update(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), values, null, null);
		assertEquals(updatedRows, 1);
		
		// New query
		c = mResolver.query(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), 
				null, null, null, Cars._ID);
		assertNotNull(c);
		assertEquals(c.getCount(), 1);
		c.moveToNext();
		assertEquals(c.getInt(c.getColumnIndex(Cars._ID)), id);
		assertEquals(c.getString(c.getColumnIndex(Cars.NAME)), CAR_NAME_1);
		assertEquals(c.getString(c.getColumnIndex(Cars.RESOURCE_URL)), CAR_RESOURCE_1);
		assertEquals(c.getDouble(c.getColumnIndex(Cars.LATITUDE)), LATITUDE);
		assertEquals(c.getDouble(c.getColumnIndex(Cars.LONGITUDE)), LONGITUDE);
		assertEquals(c.getDouble(c.getColumnIndex(Cars.ACCURACY)), ACCURACY);
		assertEquals(c.getLong(c.getColumnIndex(Cars.DATE)), date);
		c.close();
		
		// Delete
		int count = mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(id)), null, null);
		assertEquals(count, 1);
	}
	
	public void testWrongUris(){
		
		ContentValues values = new ContentValues();
		
		// Inserts
		try {
			mResolver.insert(Uri.withAppendedPath(Cars.CONTENT_URI, "wrong"), values);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.insert(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/wrong"), values);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		// Queries
		try {
			mResolver.query(Uri.withAppendedPath(Cars.CONTENT_URI, "wrong"), 
					null, null, null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.insert(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/wrong"), values);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.insert(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/1/wrong"), values);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		// Updates
		try {
			mResolver.update(Uri.withAppendedPath(Cars.CONTENT_URI, "wrong"), values, null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.update(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/wrong"), values, null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.update(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/1/wrong"), values, null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		// Deletes
		try {
			mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, "wrong"), null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/wrong"), null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			mResolver.delete(Uri.withAppendedPath(Cars.CONTENT_URI, "cars/1/wrong"), null, null);
		} catch (Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}

		
		
	}
	
}
