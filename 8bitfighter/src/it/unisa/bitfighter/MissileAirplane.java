package it.unisa.bitfighter;

import it.unisa.bitfighter.Gioca.OurView;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MissileAirplane {
	
	float x,y;
	int dimensioneschermo,altezzaschermo;
	int xSpeed, ySpeed;
	int height, width;
	int xrandom;
	int yrandom;
	Bitmap m;
	OurView ov;

	
	
	public MissileAirplane(OurView ourView, Bitmap missileaereo){
		m = missileaereo;
		ov = ourView;
		height = m.getHeight();
		width = m.getWidth();
		x = y;
		xSpeed = 0;
		altezzaschermo = ov.getHeight();
		ySpeed = -(int)(0.7 * altezzaschermo)/100;
		dimensioneschermo = ov.getWidth();

	}
	
	

	
	private void update() {
		// TODO Auto-generated method stub
		
		

		x += xSpeed;
		y += ySpeed; //il missile deve salire verso l'alto
	}

	public void onDrawn(Canvas canvas, float posx, float posy) {
		// TODO Auto-generated method stub
		x = posx;
		y = posy;
		update();
		
//		Rect src = new Rect(0,0, width, height);
//		Rect dst = new Rect(x,y,x+width,y+height );
		canvas.drawBitmap(m, x, y, null);
		//canvas.drawBitmap(n, src, dst, null);
	}
	



}
