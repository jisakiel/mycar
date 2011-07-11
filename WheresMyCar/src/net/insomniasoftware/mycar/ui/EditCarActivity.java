package net.insomniasoftware.mycar.ui;

import net.insomniasoftware.mycar.R;
import net.insomniasoftware.mycar.provider.CarsInfo.Cars;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditCarActivity extends Activity {

	private ImageView mIconButton;
	private EditText mProfileName;
	private ImageView mBluetoothButton;
	
	private String mImageUri;
	private int mResourceType;

	private static final int DIALOG_ICON_SELECION = 1;
	private static final int DIALOG_ICON_GALLERY = 2;

	private static final int ARRAY_CARS_GALLERY = 0;
	private static final int ARRAY_ANDROID_GALLERY = 1;

	private static final int SUBACTIVITY_PICK_CARS_GALLERY = 1;
	private static final int SUBACTIVITY_PICK_ANDROID_GALLERY = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_car_layout);
		
		mIconButton = (ImageView) findViewById(R.id.bt_add_icon);
		mProfileName = (EditText) findViewById(R.id.profile_name);
		mBluetoothButton = (ImageView) findViewById(R.id.bt_bluetooth);
		
		mIconButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_ICON_SELECION);
			}
		});
		
		mBluetoothButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(), "Bluetooth", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//Options menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_car_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_profile:
			//Save
			saveProfile();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	// Dialogs
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id){
		case DIALOG_ICON_SELECION:
			AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivity.this);
			builder.setTitle(R.string.profile_icon);
			builder.setItems(R.array.icon_selection, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	switch (item){
			    	case ARRAY_CARS_GALLERY:
			    		//TODO show grid layout
			    		break;
			    	case ARRAY_ANDROID_GALLERY:
			    		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			    		intent.setType("image/*");
			    		startActivityForResult(intent, SUBACTIVITY_PICK_ANDROID_GALLERY);
			    		break;
			    	}
			    }
			});
			return builder.create();
		case DIALOG_ICON_GALLERY:
			return null;
		default:
			return null;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
            switch (requestCode){
            case SUBACTIVITY_PICK_CARS_GALLERY:
            	
            	//TODO
            	mResourceType = 0;	//TODO Cars.CONSTANT
            	mImageUri = null;	//TODo get from intent
            	
            	
            	break;
            case SUBACTIVITY_PICK_ANDROID_GALLERY:
                Uri uri = data.getData();
                mImageUri = uri.toString();
                mResourceType = 1;	//TODO Cars.CONSTANT
               	mIconButton.setImageURI(uri);
                break;
            }
		}
	}

	private void saveProfile(){
		String name = mProfileName.getText().toString();
		String macAddress = null;
		
		ContentValues values = new ContentValues();
		values.put(Cars.NAME, name);
		//TODO RESOURCE_TYPE Y RESOURCE_URI
		//values.put(Cars.RESOURCE, icon);
		values.put(Cars.MAC_ADDRESS, macAddress);
		
		Uri res = getContentResolver().insert(Cars.CONTENT_URI, values);
		
		//TODO strings
		if (res != null)
			Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
		else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
	}
	
}
