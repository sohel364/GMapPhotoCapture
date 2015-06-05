package com.example.user.googlemaptest.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


@SuppressLint({ "NewApi", "ViewConstructor" })
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
 
    //Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        
        
        
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this); // we get notified when underlying surface is created and destroyed
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //this is a deprecated method, is not requierd after 3.0
    }
 
    
    @Override
    public void draw(Canvas canvas) {
    	
    	//canvas.drawColor(Color:Re);
        super.draw(canvas);
        
        Paint p = new Paint(Color.RED);
        canvas.drawText("PREVIEW", canvas.getWidth() / 2,
        canvas.getHeight() / 2, p);

        // TODO draw grid
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
        	
        	Camera.Parameters parameters = mCamera.getParameters();
            
        	parameters.set("orientation", "portrait");
            parameters.setRotation(90);
            mCamera.setDisplayOrientation(90);
            //mCamera.setParameters(parameters);
            
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
          // left blank for now
        }
 
    }
     
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
    }
 
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
            int width, int height) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // intentionally left blank for a test
        }
    }
}
