package net.insomniasoftware.mycar.ui;

import net.insomniasoftware.mycar.R;
import net.insomniasoftware.mycar.provider.Cars;
import net.insomniasoftware.mycar.provider.Cars.CarsColumns;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CarProfilesListActivity extends ListActivity {
	
	private Context mContext;
	private CarProfilesListAdapter mAdapter;
	
	/** Adapter class to fill in data for the car profiles data */
    final class CarProfilesListAdapter extends ResourceCursorAdapter { //@masterj why final?
    	
    	int mId;
    	TextView mNameTextView;
    	ImageView mCarIcon;
    	ImageView mArrowIcon;
    	ImageView mParkIcon;

		public CarProfilesListAdapter(Cursor c) {
			super(CarProfilesListActivity.this, R.layout.car_profiles_list_item, c); //@masterj shouldn't autorequery be 1?
		}

		@Override
		public void bindView(View v, Context context, Cursor cursor) {
			
			mId = cursor.getInt(cursor.getColumnIndex(CarsColumns._ID));
			mNameTextView = (TextView) v.findViewById(R.id.profile_name);
			mCarIcon = (ImageView) v.findViewById(R.id.car);
			mArrowIcon = (ImageView) v.findViewById(R.id.arrow);
			mParkIcon = (ImageView) v.findViewById(R.id.park);
			
			//fill up data from database
			mNameTextView.setText(cursor.getString(cursor.getColumnIndex(CarsColumns.NAME))); //@masterj shouldn't we be using getColumnIndexOrThrow? 
			
			int carIcon = cursor.getInt(cursor.getColumnIndex(CarsColumns.RESOURCE));
			mCarIcon.setImageDrawable(getResources().getDrawable(carIcon));
			
			
			//setup click listeners for specific actions

			
//			//if the whole listview takes you to the car this should be redundant. 
//
//			mCarIcon.setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					showCar(mId);
//				}
//			});
			

			
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
        
        mContext = getApplicationContext();
        
        Uri uri = Uri.withAppendedPath(Cars.CONTENT_URI, "cars");
        Cursor c = getContentResolver().query(uri, 
        		null, null, null, null);
        
        mAdapter = new CarProfilesListAdapter(c);
        setListAdapter(mAdapter);
        
		//long click listener for the whole view
		registerForContextMenu(this.getListView());
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long mId) {
				showCar(mId);
			}
		});
    }

    
    
    //context menu
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.car_menu, menu);
	}
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
	    	case R.id.park: 
	    		park(info.id);
	    		return true;
	    	case R.id.arrow:
	    		navigate(info.id);
	    		return true;
	    	case R.id.car:
	    		showCar(info.id);
	    		return true;
	    	case R.id.remove:
	    		removeCar(info.id);
	    		return true;
	    		
	    	default:
	    		return super.onContextItemSelected(item);
	    }
    }
    

	@Override
    protected void onDestroy() {
        super.onDestroy();
        Cursor cursor = mAdapter.getCursor();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long mId) {
		super.onListItemClick(l, v, position, mId);
		showCar(mId);
	}
    
	
	//FIXME just foolin' here
	private void park(long mId) {
		Toast.makeText(mContext, getString(R.string.park) + mId, Toast.LENGTH_LONG).show();
	}
	
	private void navigate(long mId) {
		Toast.makeText(mContext, getString(R.string.arrow) + mId, Toast.LENGTH_LONG).show();
	}
    
	private void showCar(long mId) {
		Toast.makeText(mContext, getString(R.string.car) + mId, Toast.LENGTH_LONG).show();
	}
	private void removeCar(long mId) {
		Toast.makeText(mContext, getString(R.string.remove) + mId, Toast.LENGTH_LONG).show();
	}
}