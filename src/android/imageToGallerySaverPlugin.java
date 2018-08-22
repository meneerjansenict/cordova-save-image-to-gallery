package nl.flexkids.imageToGallerySaver;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

/**
 * Base64ImageSaverPlugin.java
 *
 * Android implementation of the Base64ImageSaverPlugin for iOS.
 * Inspirated by Joseph's "Save HTML5 Canvas Image to Gallery" plugin
 * http://jbkflex.wordpress.com/2013/06/19/save-html5-canvas-image-to-gallery-phonegap-android-plugin/
 *
 * @author Vegard Løkken <vegard@headspin.no>
 */
public class ImageToGallerySaverPlugin extends CordovaPlugin {
	private static final String ACTION = "saveImageDataToLibrary";

	// Permissions
	private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
	private static final int STORE_PHOTO_REQ_CODE = 0;

	// Utility
	private CallbackContext callbackContext;
	private Bitmap bitmapImage;

	@Override
	public boolean execute(String action, JSONArray data,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;

		if (!action.equals(ACTION)) {
			return false;
		}

		// First see if we can create the image
		String base64 = data.optString(0);
		if (base64.isEmpty()) {
			callbackContext.error("Missing base64 string");
			return true;
		}

		byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
		Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, STORE_PHOTO_REQ_CODE, decodedString.length);
		if (bmp == null) {
			callbackContext.error("The image could not be decoded");
			return true;
		}

		// Now we have the image, see if we can store the image on the filesystem..
		if (!cordova.hasPermission(WRITE_EXTERNAL_STORAGE)) {
			this.bitmapImage = bmp;
			cordova.requestPermission(this, 0, WRITE_EXTERNAL_STORAGE);
			return true;
		}

		return storePhotoInGallery(bmp, callbackContext);
	}

	private boolean storePhotoInGallery(Bitmap image, CallbackContext callbackContext)
	{
		try {
			File imageFile = storePhotoOnStorage(image);
			sendImageGalleryUpdateRequest(imageFile);
			callbackContext.success(imageFile.toString());
			return true;

		} catch (NullPointerException e) {
			callbackContext.error("Error while saving image");
		}

		return false;
	}

	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException
	{
		for (int r:grantResults) {
			if (r == PackageManager.PERMISSION_DENIED) {
				this.callbackContext.error("Permission denied");
			}
		}

		switch(requestCode) {
			case STORE_PHOTO_REQ_CODE:
				storePhotoInGallery(this.bitmapImage, this.callbackContext);
				break;
		}
	}

	private File storePhotoOnStorage(Bitmap bmp) {
		File retVal = null;

		try {
			Calendar c = Calendar.getInstance();
			String date = "" + c.get(Calendar.DAY_OF_MONTH)
					+ c.get(Calendar.MONTH)
					+ c.get(Calendar.YEAR)
					+ c.get(Calendar.HOUR_OF_DAY)
					+ c.get(Calendar.MINUTE)
					+ c.get(Calendar.SECOND);

			String deviceVersion = Build.VERSION.RELEASE;
			Log.i("Base64ImageSaverPlugin", "Android version " + deviceVersion);
			int check = deviceVersion.compareTo("2.3.3");

			File folder;
			/*
			 * File path = Environment.getExternalStoragePublicDirectory(
			 * Environment.DIRECTORY_PICTURES ); //this throws error in Android
			 * 2.2
			 */
			if (check >= 1) {
				folder = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

				if(!folder.exists()) {
					folder.mkdirs();
				}
			} else {
				folder = Environment.getExternalStorageDirectory();
			}

			File imageFile = new File(folder, "c2i_" + date + ".png");

			FileOutputStream out = new FileOutputStream(imageFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();

			retVal = imageFile;
		} catch (Exception e) {
			Log.e("imageToGallerySaver", "An exception occured while saving image: "
					+ e.toString());
		}
		return retVal;
	}

	/* Invoke the system's media scanner to add your photo to the Media Provider's database,
	 * making it available in the Android Gallery application and to other apps. */
	private void sendImageGalleryUpdateRequest(File imageFile)
	{
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(imageFile);
	    mediaScanIntent.setData(contentUri);
	    cordova.getActivity().sendBroadcast(mediaScanIntent);
	}
}
