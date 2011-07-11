package net.insomniasoftware.mycar.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The Cars provider contains information about car profiles and its know positions.
 */
public class CarsInfo {

	public static final String AUTHORITY = "net.insomniasoftware.mycar";
	
    /**
     * The content:// style URL for this provider
     */
    public static final Uri CONTENT_URI =
        Uri.parse("content://" + AUTHORITY);
    
    /**
     * Contains the car profiles info.
     */
    public static class Cars implements BaseColumns {
    	
        /**
         * The content:// style URL for this provider
         */
        public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/cars");
        
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.insomniasoftware.cars";	

    	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.insomniasoftware.cars"; 
    	
    	/**
    	 * The name of the car profile.
    	 * <P>Type: TEXT</P>
    	 */
    	public static final String NAME = "name";
    	
    	/**
    	 * The latitude of the last know position.
    	 * <P>Type: INTEGER (integerE6)</P>
    	 */
    	public static final String LATITUDE = "latitude";
    	
    	/**
    	 * The longitude of the last know position.
    	 * <P>Type: INTEGER (integerE6)</P>
    	 */
    	public static final String LONGITUDE = "longitude";

    	/**
    	 * The accuracy of the last know position.
    	 * <P>Type: REAL (float)</P>
    	 */
    	public static final String ACCURACY = "accuracy";
    	
    	/**
    	 * The date in milliseconds of the last know position.
    	 * <P>Type: INTEGET (long)</P>
    	 */
    	public static final String DATE = "date";
    	
    	/**
    	 * The identifier of the resource (color/image...) of the profile.
    	 * <P>Type: STRING (TEXT)</P>
    	 * TODO: a RESOURCE_TYPE column my be needed
    	 */
    	public static final String RESOURCE_URL = "resource";
    	
    	/**
    	 * The identifier of the resource (color/image...) of the profile.
    	 * <P>Type: INTEGET (int)</P>
    	 * @see RESOURCE_TYPE
    	 */
    	public static final String RESOURCE_TYPE = "resource_type";

    	/**
    	 * The MAC address of the car bluetooth
    	 * <P>Type: TEXT</P>
    	 */
    	public static final String MAC_ADDRESS = "mac_address";
    	
    	
    	
    	/**
    	 * Types of resource_type column
    	 * <ul>
    	 * 	<li>CARS_ICON: the resource is a Car icon included in the icon set of the app</li>
    	 * 	<li>PERSONAL_ICON: the resource is an image/icon selected by the user</li>
    	 * </ul>
    	 *
    	 */
    	public enum RESOURCE_TYPE { CARS_ICON, PERSONAL_ICON };
    	
    	
    }
    
}
