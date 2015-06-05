package com.example.user.googlemaptest.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.googlemaptest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class ImagePickActivity extends Activity{

	
	final Context context = this;
	ImageView wevw=null;
	
	/*
	 * Code add for camera 
	 */
	protected static final String MEDIA_TYPE_IMAGEE = null;
	private Camera mCamera;
    private CameraPreview mCameraPreview;

	Button btnCameraOpen;
	
	TextView txtTemplateName;

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	//private static final int MY_BUTTON = 0;
	
	String _name =null;
	public String flashMood = null;

	public enum   lbl_prop { 
					Flash, 
					clrspc,
				    expsr,
				    Metrng,
				    whitebalance
				  };
	
				  
    /*
	 * The Dynamic Controls actions are gonna picked up with the following variables 
	 * 		  
	 */
	TextView txtVw;
	
	public Parameters p; //Camera Parameter Instance
	//public CameraParam CameraParameters;
	
	Boolean isFlashSwtchOn=false;
	int ExposureValue;
	String meetringMode;
	String WhiteBalance;
	
	public void ClearCntrl(){
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //final View addView = layoutInflater.inflate(R.layout.activity_image_pick, null);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout2);
        ll.removeAllViews();
        //((LinearLayout)addView.getParent()).removeView(addView);
	}
	
	
	boolean isClearAble=false;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	

	int clickCount =0;
	public ArrayList<String> lstIPropertyValues = null;

    int click = 0;
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        clickCount = 0;
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        
        
        DrawView _drView =new DrawView(this);
        FrameLayout fLayoutn = (FrameLayout)findViewById(R.id.camera_preview);

        fLayoutn.addView(mCameraPreview);
        fLayoutn.addView(_drView);

        fLayoutn.setOnTouchListener(new OnTouchListener() {
		
        	
        	
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				return true;
			}
		});
	    
        

	}
	

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_pick, menu);
        return true;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
			if (resultCode == RESULT_OK){
				ToastPrint("Image Captured Successfully");
			} else if (resultCode == RESULT_CANCELED) {
				ToastPrint("Canceled");
			} else {
				ToastPrint("Failed");
			}
		}
	}
	
    public void ToastPrint(String str){
    	Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, str, duration);
		toast.show();
    }
    
    
    private Camera getCameraInstance() {
        Camera camera = null;
 
        try {
           
        	camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

	
	PictureCallback mPicture = new PictureCallback() {
		 
        @SuppressLint("SdCardPath")
		@Override
        public void onPictureTaken(byte[] data, Camera camera) {
             File pictureFile = getOutputMediaFile();
                if (pictureFile == null){
                    return;
                }
 
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                  
                    _name = new String();
                    _name = pictureFile.getName();
                    
                   
                    wevw = (ImageView)findViewById(R.id.imgpreviewpnae);
                    String pathT = "/sdcard/Pictures/CamSmartTemplate/"+_name;
        		    Bitmap myBitmap =BitmapFactory.decodeFile(pathT);
        		    //Bitmap myBitmapTemp = Utility.getResizedBitmap(myBitmap, 80, 80);
        		    wevw.setImageBitmap(myBitmap);
        		    
      
        		    
        		    //listner for imageview popup
        		    wevw.setOnClickListener(new OnClickListener() {
        				@SuppressLint("SdCardPath")
        				@Override
        				public void onClick(View v) {
        					LayoutInflater li = LayoutInflater.from(context);
        					View proView = li.inflate(R.layout.image_view_big, null);
        					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        					alertDialogBuilder.setView(proView);
        					
        					final ImageView imageViewBig = (ImageView) proView.findViewById(R.id.imageViewBig);
        					String pathT = "/sdcard/Pictures/CamSmartTemplate/"+_name;
        					
        					Bitmap bm = BitmapFactory.decodeFile(pathT);
        					//Bitmap bmm = Utility.getResizedBitmap(bm,450,450);
        					imageViewBig.setImageBitmap(bm);
        					
        					alertDialogBuilder.setCancelable(false)
        					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        						
        						@Override
        						public void onClick(DialogInterface dialog, int id) {
        							
        							dialog.cancel();
        						}
        					});
        					
        					AlertDialog alertDialog = alertDialogBuilder.create();
        					alertDialog.show();
        				}
        			});
        		    
        		    
        		    
                    
                } catch (FileNotFoundException e) {
 
                } catch (IOException e) {
                 
                }

        }

	};
        

     @SuppressLint("SdCardPath")
	public void imageBigView(){
    	 LayoutInflater li = LayoutInflater.from(context);
			View proView = li.inflate(R.layout.image_view_big, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setView(proView);
			
			final ImageView imageViewBig = (ImageView) proView.findViewById(R.id.imageViewBig);
			
			if(_name!=null){
				
				Bitmap bm = BitmapFactory.decodeFile("/sdcard/Pictures/CamSmartTemplate/"+_name);
				//Bitmap bmm = Utility.getResizedBitmap(bm,450,450);
				imageViewBig.setImageBitmap(bm);
			}
			
			alertDialogBuilder.setCancelable(false)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					
					dialog.cancel();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
     }
     
	
	/** Create a File for saving the image */
    @SuppressLint("SimpleDateFormat")
	private  File getOutputMediaFile(){
 
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "CamSmartTemplate");
 
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CamSmartTemplate", "failed to create directory");
                return null;
            }
        }
 
        // Create a media file name
        String timeStamp = new  SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
         

        return mediaFile;       
        
    }
   
   // public CameraParam SetCameraParameter(){
	//	CameraParam camParam = new CameraParam();
	//	return camParam.MakeParam(flashMood, ExposureValue, WhiteBalance, meetringMode);
   // }
       
    @SuppressLint({ "SdCardPath", "SimpleDateFormat" })
	public void onClickTake(View v) throws InterruptedException {
                    		
    				
    				p = mCamera.getParameters();
    		//		CameraParam camObject = SetCameraParameter();
    				
    				if(!isFlashSwtchOn){
    					flashMood = "off";
    					p.setFlashMode(Parameters.FLASH_MODE_OFF);
    				}
    				else{
    					flashMood = "on";
    					   p.setFlashMode(Parameters.FLASH_MODE_ON);
    				}
    	
    				
                  //  p.setExposureCompensation(camObject.getExposureValue());
                    //p.setMeteringAreas(camObject.getMeetring());
                   // p.setWhiteBalance(WhiteBalance);
                    
                    mCamera.setParameters(p);
                   // mCamera.startPreview();
                    mCamera.takePicture(null, null, mPicture);
                    //p.setFlashMode(Parameters.FLASH_MODE_OFF);

                    
                    Context context = getApplicationContext();
                    //CharSequence text = "FLASH MODE :"+p.getFlashMode()+"COLOR EFFECT :"+p.getColorEffect()+"EXPOSOUR : "+p.getExposureCompensation();
                    CharSequence text = "Captured !";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
        
}