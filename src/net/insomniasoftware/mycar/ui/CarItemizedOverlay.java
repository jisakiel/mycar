package net.insomniasoftware.mycar.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;

public class CarItemizedOverlay extends com.google.android.maps.ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>(); //TODO one for each vehicle...
	private Context mContext;
	
	public CarItemizedOverlay(Drawable defaultMarker, Context c) {
		super(defaultMarker);
		mContext = c;
		
		
	}

	@Override
	protected boolean onTap(int index) {
		//TODO go to the specific vehicle instead of showing this dialog
		//TODO implement double tap to zoom
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

}
