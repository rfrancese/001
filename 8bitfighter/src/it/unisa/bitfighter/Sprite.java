package it.unisa.bitfighter;

import it.unisa.bitfighter.Gioca.OurView;

import java.util.Random;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	
	int x,y;
	int xSpeed, ySpeed;
	int height, width;
	int xrandom;
	int yrandom;
	Bitmap n;
	OurView ov;
	


	
	public Sprite(OurView ourView, Bitmap nemico){
		n = nemico;
		ov = ourView;
		height = n.getHeight();
		width = n.getWidth();
		x = y = 0;
		xSpeed = 0;
		ySpeed = 1;

	}
	

	
	private void update() {
		// TODO Auto-generated method stub
		
		if(y > ov.getHeight()/1.5 - height - ySpeed){
			ySpeed = -2;
			xSpeed = 0;
		}
		
		if(y < 0){
			ySpeed = 2;
			xSpeed = 0;
		}

		x += xSpeed;
		y += ySpeed;
	}

	public void onDrawn(Canvas canvas) {
		// TODO Auto-generated method stub
		update();
		Random randomGenerator = new Random();
		int dadove = randomGenerator.nextInt(width);
		x=300;
//		Rect src = new Rect(0,0, width, height);
//		Rect dst = new Rect(x,y,x+width,y+height );
		canvas.drawBitmap(n, x-width/2, y-height/2, null);
		//canvas.drawBitmap(n, src, dst, null);
	}



	
}
