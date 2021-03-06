package net.insomniasoftware.mycar.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The Cars provider contains information about car profiles and its known locations.
 */
public class Cars {

	public static final String AUTHORITY = "net.insomniasoftware.mycar";

    /**
     * The content:// style URL for this provider
     */
    public static final Uri CONTENT_URI =
        Uri.parse("content://" + AUTHORITY);
    
    /**
     * Contains the car profiles info.
     */
    public static class CarsColumns implements BaseColumns {

    	/**
    	 * The content:// style URL for this table 
    	 */
    	public static final Uri CONTENT_URI = 
        	Uri.parse("content://" + AUTHORITY + "/calls");
        
    	public static final String CONTENT_TYPE = "";	//TODO

    	public static final String CONTENT_ITEM_TYPE = ""; //TODO
    	
    	/**
    	 * The name of the car profile.
    	 * <P>Type: TEXT</P>
    	 */
    	public static final String NAME = "name";
    	
    	/**
    	 * The latitude of the last known location.
    	 * <P>Type: INTEGER (integerE6)</P>
    	 */
    	public static final String LATITUDE = "latitude";
    	
    	/**
    	 * The longitude of the last known location.
    	 * <P>Type: INTEGER (integerE6)</P>
    	 */
    	public static final String LONGITUDE = "longitude";

    	/**
    	 * The accuracy of the last known location.
    	 * <P>Type: REAL (float)</P>
    	 */
    	public static final String ACCURACY = "accuracy";
    	
    	/**
    	 * The date in milliseconds of the last known location.
    	 * <P>Type: INTEGET (long)</P>
    	 */
    	public static final String DATE = "date";
    	
    	/**
    	 * The identifier of the resource (color/image...) of the profile.
    	 * <P>Type: INTEGET (int)</P>
    	 * TODO: a RESOURCE_TYPE column may be needed
    	 */
    	public static final String RESOURCE = "resource";

    	/**
    	 * The MAC address of the car's bluetooth
    	 * <P>Type: TEXT</P>
    	 */
    	public static final String MAC_ADDRESS = "mac_address";
    	
    	
    }
    
}
