package net.insomniasoftware.mycar.ui;

import java.io.BufferedInputStream;
import java.io.IOException;

import net.insomniasoftware.mycar.R;
import net.insomniasoftware.mycar.provider.CarsInfo.Cars;
import net.insomniasoftware.mycar.util.Utils;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CarProfilesListActivity extends ListActivity {

	private static final String TAG = "CarProfilesListActivity";

	private CarProfilesListAdapter mAdapter;

	private static final int SUBACTIVITY_NEW_CAR = 1;

	/** Adapter class to fill in data for the car profiles data */
	final class CarProfilesListAdapter extends ResourceCursorAdapter {

		int mId;
		TextView mNameTextView;
		ImageView mCarIcon;
		ImageView mArrowIcon;
		ImageView mParkIcon;

		public CarProfilesListAdapter(Cursor c) {
			super(CarProfilesListActivity.this, R.layout.car_profiles_list_item, c);
		}

		@Override
		public void bindView(View v, Context context, Cursor cursor) {

			mId = cursor.getInt(cursor.getColumnIndexOrThrow(Cars._ID));
			mNameTextView = (TextView) v.findViewById(R.id.profile_name);
			mCarIcon = (ImageView) v.findViewById(R.id.car_icon);
			mArrowIcon = (ImageView) v.findViewById(R.id.arrow);
			mParkIcon = (ImageView) v.findViewById(R.id.park);

			//Fill up data from database

			//Name
			mNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(Cars.NAME)));

			//Icon
			int resourceType = cursor.getInt(cursor.getColumnIndexOrThrow(Cars.RESOURCE_TYPE));
			String resourceUrl = cursor.getString(cursor.getColumnIndexOrThrow(Cars.RESOURCE_URL));

			if (Utils.isEmpty(resourceUrl)){
				//TODO 
			}
			switch (resourceType){
			case Cars.RESOURCE_TYPE_CARS_ICON:
				try {
					BufferedInputStream buf = new BufferedInputStream(getAssets().open(resourceUrl));
					Bitmap bitmap = BitmapFactory.decodeStream(buf);
					mCarIcon.setImageBitmap(bitmap);
					buf.close();
				} catch (IOException e) {
					Log.e(TAG, "Error showing icon", e);
				}
				break;
			case Cars.RESOURCE_TYPE_PERSONAL_ICON:
				mCarIcon.setImageURI(Uri.parse(resourceUrl));
				break;
			default:
				Log.e(TAG, "Invalid resourceType " + resourceType);
			}

			//setup click listeners for specific actions
			mArrowIcon.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					navigate(mId);
				}
			});

			mParkIcon.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					park(mId);
				}
			});

		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		//FIXME DEBUG DATA -- delete
		ContentValues values = new ContentValues();
		values.put(Cars.NAME, "My Car");
		values.put(Cars.RESOURCE, R.drawable.car);
		getContentResolver().insert(Cars.CONTENT_URI, values);

		values.clear();
		values.put(Cars.NAME, "My Car2");
		values.put(Cars.RESOURCE, R.drawable.car);
		getContentResolver().insert(Cars.CONTENT_URI, values);
		//END DEBUG DATA
		 */

		Cursor c = getContentResolver().query(Cars.CONTENT_URI, 
				null, null, null, null);

		mAdapter = new CarProfilesListAdapter(c);
		setListAdapter(mAdapter);

		//long click listener for the whole view
		registerForContextMenu(this.getListView());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Cursor cursor = mAdapter.getCursor();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	//Context menu

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cars_list_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//TODO continue here
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.park: 
			park(info.id);
			return true;
		case R.id.directions:
			navigate(info.id);
			return true;
		case R.id.show_car:
			showCar(info.id);
			return true;
		case R.id.edit_profile:
			return true;
		case R.id.share_location:
			shareLocation(info.id);
			return true;
		case R.id.remove:
			getContentResolver().delete(Uri.withAppendedPath(Cars.CONTENT_URI, String.valueOf(info.id)), null, null);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	//Options menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cars_list_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_profile:
			//Toast.makeText(this, "New car profile activity", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, EditCarActivity.class);
			startActivityForResult(i, SUBACTIVITY_NEW_CAR);
			return true;
		case R.id.map_view:
			//TODO
			Toast.makeText(this, "Map view", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.delete_all:
			getContentResolver().delete(Cars.CONTENT_URI, null, null);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		showCar(id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(this, "Request code " + requestCode, Toast.LENGTH_SHORT).show();
	}

	//FIXME just foolin' here
	private void park(long id) {
		Toast.makeText(CarProfilesListActivity.this, getString(R.string.park) + id, Toast.LENGTH_LONG).show();
	}

	private void navigate(long id) {
		Toast.makeText(CarProfilesListActivity.this, getString(R.string.directions) + id, Toast.LENGTH_LONG).show();
	}

	private void showCar(long id) {
		Toast.makeText(CarProfilesListActivity.this, getString(R.string.show_car) + id, Toast.LENGTH_LONG).show();
	}

	private void shareLocation(long id) {
		Toast.makeText(CarProfilesListActivity.this, getString(R.string.share_location) + id, Toast.LENGTH_LONG).show();
	}

}