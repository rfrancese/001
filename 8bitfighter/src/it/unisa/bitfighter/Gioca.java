package it.unisa.bitfighter;

import it.unisa.bitfighter.CollisionUtil;
import it.unisa.bitfighter.Sprite;

import java.util.Random;

import com.example.bitfighter.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class Gioca extends Activity implements SensorEventListener {
	
	OurView v;
	Bitmap airplane, nemico, sfondo;
	float x,y,ysfondo;
	SensorManager sensorManager = null;
	Sensor accelerometer;
	float xPosition, yPosition = 0.0f;
	int LIMITEMOVY = 5;
	int width;
	int height;
	int nemicodadove;
	String lingua;
	private int mBGFarMoveY = 0;
	private int mBGNearMoveY = 0;
	Bitmap nuovosfondo;
	Paint paint = new Paint(); 

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		lingua = b.getString("lang"); // prendo la lingua dall'extra
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	
		super.onCreate(savedInstanceState);
		v = new OurView(this);
		
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		airplane = BitmapFactory.decodeResource(getResources(), R.drawable.airplane);
		nemico = BitmapFactory.decodeResource(getResources(), R.drawable.nemico);
		sfondo = BitmapFactory.decodeResource(getResources(), R.drawable.terrain);
		
		 Matrix matrix = new Matrix();
         matrix.postScale(1f, 1f);
		
         nuovosfondo = Bitmap.createScaledBitmap(sfondo, width, sfondo.getHeight(), true);
//		nuovosfondo = Bitmap.createBitmap(sfondo, 0, 0, width, sfondo.getHeight(), matrix, true);
//		 nuovosfondo = Bitmap.createBitmap(sfondo,0,0,width,height);

		
		setContentView(v);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		x = (float)(width/1.5);
		y = (float)(height/1.5);
	
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        v.resume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
	}
	
	
	public void onBackPressed()  
	{  
	    //do whatever you want the 'Back' button to do  
	    //as an example the 'Back' button is set to start a new Activity named 'NewActivity'  
		Intent intent = new Intent(this, Menu_princ.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
	    startActivity(intent); //inizio l'attività nuova	    
	    finish(); //chiude questa attività;
	    return;  
	//	onPause();
	//	showPopup()
	} 
	
	public void showPopup() {
	    //fare il popup
	   
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		v.pause();
		sensorManager.unregisterListener(this);
	}



	public class OurView extends SurfaceView implements Runnable{
		
		Thread t = null;
		SurfaceHolder holder; 
		boolean isItOK = false;
		Sprite sprite;
		boolean spriteLoaded = false;


		public OurView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = getHolder();
		}

		
		public void run() {

			while(isItOK == true){
				//perform canvas drawing
				if(!holder.getSurface().isValid()){
					continue;
				}
				 if(!spriteLoaded){
					 sprite = new Sprite(this,nemico);
					 spriteLoaded = true;
				 }

				 Canvas c = holder.lockCanvas();
				 
				 onDraw(c);
				 holder.unlockCanvasAndPost(c);					 
			}
		}
		
		public void onDraw(Canvas canvas){
			
			updateairplane();//limita posizione dell'aereo
			aggiornasfondo(canvas); //fai scrollare lo sfondo		 
			
			paint.setColor(Color.WHITE);
			canvas.drawBitmap(airplane, x - (airplane.getWidth()/2), y - (airplane.getHeight()/2), null);
			canvas.drawText("x="+x+" y="+y, 10, 40, paint);
			
			canvas.drawText("WIDTH="+width+" HEIGHT="+height, 10, 100, paint);
			
			
			
			inseriscinemico(sprite, canvas);
			if(isCollisionDetected(airplane, (int)x, (int)y, sprite.n, sprite.x, sprite.y))
				canvas.drawText("*****COLPITO****", 10, 50, paint); 
			
		}
		

		 
		 
		private void aggiornasfondo(Canvas canvas) {
			// decrement the far background
			  mBGFarMoveY = mBGFarMoveY - 1;
			  // decrement the near background
			  mBGNearMoveY = mBGNearMoveY - 2;
			  // calculate the wrap factor for matching image draw
			  int newFarY = nuovosfondo.getHeight() - (-mBGFarMoveY);
			  // if we have scrolled all the way, reset to start
			  if (newFarY <= 0) {
				  mBGFarMoveY = 0;
			   // only need one draw
			   canvas.drawBitmap(nuovosfondo, 0,mBGFarMoveY, null);
			  } else {
			   // need to draw original and wrap
			   canvas.drawBitmap(nuovosfondo, 0,mBGFarMoveY,  null);
			   canvas.drawBitmap(nuovosfondo, 0,newFarY, null);
			  }

			
					
		}


		private void inseriscinemico(Sprite sprite2, Canvas canvas) {
			sprite2.onDrawn(canvas);
			//sprite.onDrawn(canvas,100);
			Paint paint = new Paint(); 
			paint.setColor(Color.WHITE); 
			paint.setTextSize(20); 
			
			canvas.drawText("x="+sprite2.x+" y="+sprite2.y, 10, 25, paint);
		}
		
		//vede le collisioni
		public boolean isCollisionDetected(Bitmap bitmap1, int x1, int y1,
		        Bitmap bitmap2, int x2, int y2) {

		    Rect bounds1 = new Rect(x1, y1, x1+bitmap1.getWidth(), y1+bitmap1.getHeight());
		    Rect bounds2 = new Rect(x2, y2, x2+bitmap2.getWidth(), y2+bitmap2.getHeight());

		    if (Rect.intersects(bounds1, bounds2)) {
		        Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
		        for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
		            for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
		                int bitmap1Pixel = bitmap1.getPixel(i-x1, j-y1);
		                int bitmap2Pixel = bitmap2.getPixel(i-x2, j-y2);
		                if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
		                    return true;
		                }
		            }
		        }
		    }
		    return false;
		}

		private Rect getCollisionBounds(Rect rect1, Rect rect2) {
		    int left = (int) Math.max(rect1.left, rect2.left);
		    int top = (int) Math.max(rect1.top, rect2.top);
		    int right = (int) Math.min(rect1.right, rect2.right);
		    int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
		    return new Rect(left, top, right, bottom);
		}

		private boolean isFilled(int pixel) {
		    return pixel != Color.TRANSPARENT;
		}
		
		//fine metodi collisioni

	
		
		private void updateairplane() {
		// limitiamo la posizione massima dell'aereo, per evitare che esca dallo schermo
		
			if(y >= v.getHeight()-50)
				y = v.getHeight()-50;			
			if(x >= v.getWidth())
				x=v.getWidth();
			if(y <= 0)
				y=0;
			if(x <= 0)
				x=0;
		}

		public void pause(){
			isItOK = false;
			while(true){
				try{
					t.join();
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				break;
			}
			t = null;		
		}
		
		public void resume(){
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
	}

	
	 // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent)
    {
    	  // TODO Auto-generated method stub
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        		xPosition = (float)sensorEvent.values[0] ;
        		yPosition = (float)sensorEvent.values[1];
        		
        		float yfinale = 0;
        		
        		if(yPosition < LIMITEMOVY && yPosition > -0.440)
        			yfinale = 0;
        		else if(yPosition > 4)
        			yfinale = (yPosition - LIMITEMOVY) * 5;
        		else        			
        			yfinale = yPosition * 5;

        	
        		x -= xPosition * 5;
            	y -=  yfinale;
  
        }
    }



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	
}
