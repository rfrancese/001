package it.unisa.bitfighter;

import it.unisa.bitfighter.Gioca.OurView;

import java.util.Random;


import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	
	float x,y,dadove,dimensioneschermo,altezzaschermo;
	int xSpeed, ySpeed, numeromissili;
	int height, width;
	int xrandom;
	int yrandom;
	boolean sparato;
	Bitmap n;
	OurView ov;
	
	
		
	public Sprite(OurView ourView, Bitmap nemico){
		n = nemico;
		ov = ourView;
		height = n.getHeight();
		width = n.getWidth();
		x = y = 0;
		numeromissili = 3;
		sparato = false;
		altezzaschermo = ov.getHeight();
		xSpeed = 0;
		ySpeed = (int)(0.3 * altezzaschermo)/100;
		dimensioneschermo = ov.getWidth();
		
		float Min = width/2;
		float Max = dimensioneschermo-width;
		//generiamo una posizione casuale tra 1 e la dimensione dello schermo in larghezza
		x = Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
	

	
	private void update() {
		// TODO Auto-generated method stub
		
//		if(y > ov.getHeight()/1.5 - height - ySpeed){
//			ySpeed = -2;
//			xSpeed = 0;
//		}
		if(ySpeed < 2)
			ySpeed = 2;
		

		x += xSpeed;
		y += ySpeed;
	}

	public void onDrawn(Canvas canvas) {
		// TODO Auto-generated method stub
		update();

//		Rect src = new Rect(0,0, width, height);
//		Rect dst = new Rect(x,y,x+width,y+height );
		canvas.drawBitmap(n, x, y, null);
		//canvas.drawBitmap(n, src, dst, null);
	}
	
	
}
