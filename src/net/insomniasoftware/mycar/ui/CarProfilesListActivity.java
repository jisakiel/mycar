package net.insomniasoftware.mycar.ui;

import net.insomniasoftware.mycar.R;
import net.insomniasoftware.mycar.provider.Cars;
import net.insomniasoftware.mycar.provider.Cars.CarsColumns;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CarProfilesListActivity extends ListActivity {
	
	private Context mContext;
	private CarProfilesListAdapter mAdapter;
	
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
			
			mId = cursor.getInt(cursor.getColumnIndex(CarsColumns._ID));
			mNameTextView = (TextView) v.findViewById(R.id.profile_name);
			mCarIcon = (ImageView) v.findViewById(R.id.car);
			mArrowIcon = (ImageView) v.findViewById(R.id.arrow);
			mParkIcon = (ImageView) v.findViewById(R.id.park);
			
			//fill up data from database
			mNameTextView.setText(cursor.getString(cursor.getColumnIndex(CarsColumns.NAME))); //@masterj shouldn't we be using getColumnIndexOrThrow? 
			
			int carIcon = cursor.getInt(cursor.getColumnIndex(CarsColumns.RESOURCE));
			mCarIcon.setImageDrawable(getResources().getDrawable(carIcon));
			mCarIcon.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, "Go to car position of " + mId, Toast.LENGTH_LONG).show();
				}
			});
			
			mArrowIcon.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, "Directions to car of " + mId, Toast.LENGTH_LONG).show();
				}
			});
			
			mParkIcon.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, "Park car " + mId, Toast.LENGTH_LONG).show();
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Toast.makeText(mContext, "MAIN ITEM: Go to car position of " + id, Toast.LENGTH_LONG).show();
	}
    
    
}