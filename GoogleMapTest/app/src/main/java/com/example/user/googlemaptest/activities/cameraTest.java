package com.example.user.googlemaptest.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.user.googlemaptest.R.id;
import static com.example.user.googlemaptest.R.layout;


public class cameraTest extends Activity {

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    String _name =null;
    public Camera.Parameters p;
    public String flashMood = null;
    Boolean isFlashSwtchOn=false;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_camera_test);

        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);


        DrawView _drView =new DrawView(this);
        FrameLayout fLayoutn = (FrameLayout)findViewById(id.camera_preview);

        fLayoutn.addView(mCameraPreview);
        fLayoutn.addView(_drView);

        fLayoutn.setOnTouchListener(new View.OnTouchListener() {



            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                ToastPrint("The surface is touched");
                mCamera.startPreview();
                return true;
            }
        });

    }

    ScrollView llPreview =null;
    int countimg=0;
    List<Bitmap> listBitMap =null;
    LinearLayout llimglayout=null;

    private Camera getCameraInstance() {
        Camera camera = null;
        countimg++;
        try {

            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }


    Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @SuppressLint({"SdCardPath", "NewApi"})
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

                //llPreview = (ScrollView)findViewById(id.scrollView1);

                llimglayout = (LinearLayout)findViewById(id.linearLayout2);
               // llimglayout.setId(countimg+1);
                ImageView imgView = new ImageView(getApplicationContext());
                imgView.setId(countimg);

                String pathT = "/sdcard/Pictures/CamSmartTemplate/"+_name;
                Bitmap myBitmap = BitmapFactory.decodeFile(pathT);

                imgView.setImageBitmap(Utils.getResizedBitmap(myBitmap,100,100));

                llimglayout.addView(imgView);

               // llPreview.addView(llimglayout);
                //llPreview.refreshDrawableState();

                mCamera.startPreview();

            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }

        }

    };

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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");


        return mediaFile;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({ "SdCardPath", "SimpleDateFormat" })
    public void onClickTake(View v) throws InterruptedException {


        p = mCamera.getParameters();
        //		CameraParam camObject = SetCameraParameter();

        if(!isFlashSwtchOn){
            flashMood = "off";
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        else{
            flashMood = "on";
            p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
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