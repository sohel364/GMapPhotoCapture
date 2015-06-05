package com.example.user.googlemaptest.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
 
    public DrawView(Context context) {
        super(context);            
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.DKGRAY);
         
        canvas.drawRect(0, 200, 3000, 201, paint);
        canvas.drawRect(0, 400, 3000, 401, paint);
        canvas.drawRect(0, 600, 3000, 601, paint);
        canvas.drawRect(0, 800, 3000, 801, paint);
        canvas.drawRect(0, 1000, 3000, 1001, paint);
        canvas.drawRect(0, 1200, 3000, 1201, paint);
        canvas.drawRect(0, 1400, 3000, 1401, paint);
        canvas.drawRect(0, 1600, 3000, 1601, paint);
        canvas.drawRect(0, 1800, 3000, 1801, paint);
        canvas.drawRect(0, 2000, 3000, 2001, paint);
        canvas.drawRect(0, 2200, 3000, 2201, paint);
        canvas.drawRect(0, 2400, 3000, 2401, paint);
        canvas.drawRect(0, 2600, 3000, 2601, paint);
        canvas.drawRect(0, 2800, 3000, 2801, paint);
        canvas.drawRect(0, 3000, 3000, 3001, paint);
        
        canvas.drawRect(200, 0, 201, 3000, paint);
        canvas.drawRect(400, 0, 401, 3000, paint);
        canvas.drawRect(600, 0, 601, 3000, paint);
        canvas.drawRect(800, 0, 801, 3000, paint);
        canvas.drawRect(1000, 0, 1001, 3000, paint);
        canvas.drawRect(1200, 0, 1201, 3000, paint);
        canvas.drawRect(1400, 0, 1401, 3000, paint);
        canvas.drawRect(1600, 0, 1601, 3000, paint);
        canvas.drawRect(1800, 0, 1801, 3000, paint);
        canvas.drawRect(2000, 0, 2001, 3000, paint);
        canvas.drawRect(2200, 0, 2201, 3000, paint);
        canvas.drawRect(2400, 0, 2401, 3000, paint);
        canvas.drawRect(2600, 0, 2601, 3000, paint);
        canvas.drawRect(2800, 0, 2801, 3000, paint);
        canvas.drawRect(3000, 0, 3001, 3000, paint);
        
    }
    
    @Override
    public void draw(Canvas canvas) {
    	super.draw(canvas);
    	
    }
    
}