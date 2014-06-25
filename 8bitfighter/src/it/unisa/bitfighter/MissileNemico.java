package it.unisa.bitfighter;

import com.example.bitfighter.R;

import it.unisa.bitfighter.Gioca.OurView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;

public class MissileNemico {

	float x,y;
	int dimensioneschermo,altezzaschermo;
	int xSpeed, ySpeed;
	int height, width;
	int xrandom;
	int yrandom;
	int suono;
	Bitmap m;
	OurView ov;
	int sMissile;
	

	
	
	public MissileNemico(OurView ourView, Bitmap missilenemico){
		m = missilenemico;
		ov = ourView;
		height = m.getHeight();
		width = m.getWidth();
		x = y =0;
		xSpeed = 0;
		dimensioneschermo = ov.getWidth();
		altezzaschermo = ov.getHeight();
		ySpeed = (int)(0.7 * altezzaschermo)/100;
		suono = 0;

	}
	
	

	
	private void update() {
		// TODO Auto-generated method stub
		
		

		x += xSpeed;
		y += ySpeed; //il missile deve scendere
		suono = 100;
	}

	public void onDrawn(Canvas canvas) {
		// TODO Auto-generated method stub
		
		
		update();
		

		canvas.drawBitmap(m, x, y, null);
		//canvas.drawBitmap(n, src, dst, null);
	}
}
