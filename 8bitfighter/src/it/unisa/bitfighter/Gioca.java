package it.unisa.bitfighter;

import it.unisa.bitfighter.Sprite;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.bitfighter.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class Gioca extends Activity implements SensorEventListener {
	private static final boolean VERBOSE = true;
	private static final String TAG = "Gioca";
	
	/** Sound variables */
	private SoundPool sounds;
	private int sEsplosione;
	private int sMissile;
	
	OurView v;
	Bitmap airplane, nemico,nemico2,nemico3,nemico4, sfondo, missileaereo,missilenemico, esplosione,cuorepieno,cuorevuoto;
	float x,y,ysfondo, posxmissile, posymissile;
	SensorManager sensorManager = null;
	Sensor accelerometer;
	float xPosition, yPosition = 0.0f;
	int LIMITEMOVY = 5;
	int nemici=0;
	int maxnemici = 5;
	int width;
	int height;
	int nemicodadove;
	long tempo, tempoexp = 0;
	String lingua;
	private int mBGFarMoveY = 0;
	Bitmap nuovosfondo;
	Paint paint = new Paint(); 
	Boolean lanciatomissilemio=false;
	ArrayList<MissileAirplane> listamissilimio;
	ArrayList<Sprite> listanemici;
	ArrayList<Sprite> listanemiciesplosi;
	ArrayList<MissileNemico> listamissilinemici;
	boolean primoavvio = true;
	boolean vuoiuscire = false;
	boolean inpausa = false;
	boolean mostropopup = false;
	boolean SUONO = true;
	int COLPITO;
	int xcolpito;
	int ycolpito;
	
	int VITE = 3;
	int missililanciati=0;
	int nemiciuccisi = 0;
	int velocitanemico = 4;
	int velocitamissilenemico = 7;
	int punteggio = 0;
	int tempoingressonemici=3000;//iniziamo con 3 secondi
	
	int livello = 1;


	private Handler mHandler = new Handler();	


	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		primoavvio = true;
		
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		lingua = b.getString("lang"); // prendo la lingua dall'extra
		SUONO = b.getBoolean("suono"); // prendo il booleano dei suoni
		
		
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
		nemico2 = BitmapFactory.decodeResource(getResources(), R.drawable.nemico2);
		nemico3 = BitmapFactory.decodeResource(getResources(), R.drawable.nemico3);
		nemico4 = BitmapFactory.decodeResource(getResources(), R.drawable.nemico4);
		missileaereo = BitmapFactory.decodeResource(getResources(), R.drawable.missileaereo);
		missilenemico = BitmapFactory.decodeResource(getResources(), R.drawable.missilenemico);
		sfondo = BitmapFactory.decodeResource(getResources(), R.drawable.sfondo);
		esplosione = BitmapFactory.decodeResource(getResources(), R.drawable.esplosione);
		cuorepieno = BitmapFactory.decodeResource(getResources(), R.drawable.vita);
		cuorevuoto = BitmapFactory.decodeResource(getResources(), R.drawable.vitameno);

		
		listamissilimio = new ArrayList<MissileAirplane>(); //array list missili miei
		listanemici = new ArrayList<Sprite>(); //arraylist nemici
		listanemiciesplosi = new ArrayList<Sprite>(); //arraylist nemici
		listamissilinemici = new ArrayList<MissileNemico>(); //arraylist nemici

	
		 Matrix matrix = new Matrix();
         matrix.postScale(1f, 1f);
		
         nuovosfondo = Bitmap.createScaledBitmap(sfondo, width, 6*width, true);
         
     
		
		setContentView(v);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		x = (float)(width/1.5);
		y = (float)(height/1.25);
		
		
		
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		sEsplosione = sounds.load(this, R.raw.esplode, 1);
		sMissile = sounds.load(this, R.raw.missile, 1);
		
		
		
		
	
	}
	
	
	
	private void gameover(){
		sounds.release();
		Intent intent = new Intent(this, GameOver.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
		Bundle points = new Bundle();
		points.putInt("punteggio", punteggio);
		intent.putExtras(points);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent); //inizio l'attività nuova	    
		finish();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mostropopup == false)
			v.resume();
		volumeSuoni();
        sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_GAME);
        if (VERBOSE) Log.v(TAG, "+ ON RESUME +");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if((mostropopup == false)&&(primoavvio == false)){
			showPopup();
			v.pause();
		}
		mutaSuoni();
		super.onPause();
		sensorManager.unregisterListener(this);
		if (VERBOSE) Log.v(TAG, "- ON PAUSE -");
	}
	
	private void mutaSuoni(){
		sounds.play(sMissile,  0.0f, 0.0f, 0, 0, 1.0f);		
		sounds.autoPause();
		}
	
	private void volumeSuoni(){
		sounds.autoResume();
		}
	
	private void suonomissile(){
		if(SUONO == true)
			sounds.play(sMissile,  0.5f, 0.5f, 0, 0, 1.0f);
	}
	
	private void suonoesplosione(){ 
		if(SUONO == true)
			sounds.play(sEsplosione, 0.5f, 0.5f, 0, 0, 1.5f);
	}

	





//	@Override
//    public void onStop() {
//        super.onStop();
//        if (VERBOSE) Log.v(TAG, "-- ON STOP --");
//    }
//
//   @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (VERBOSE) Log.v(TAG, "- ON DESTROY -");
//    }
	
	
	
	public void onBackPressed()  
	{  
	    //do whatever you want the 'Back' button to do  
	    //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
		v.pause();
		mutaSuoni();
		showPopup();

	} 

		
	
	public void showPopup() {
	    //fare il popup
		mostropopup = true;
		Context context = Gioca.this;
	    String title = "PAUSA";
	    String message = "sei in pausa, fai la tua scelta";
	    String button1String = "Esci";
	    String button2String = "Riprendi";
	    mutaSuoni();

	 
	     AlertDialog.Builder ad = new AlertDialog.Builder(context);
	         ad.setTitle(title);
	         ad.setMessage(message);
	 	     ad.setCancelable(false);

	         	 
	    ad.setPositiveButton(
	    button1String,
	    new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int arg1) {
	        	tornaalmenu();
	        	vuoiuscire = true;
	        	mostropopup = false;
	        	v.resume();
	        	
	        	
//		        onResume();
	        }
	    }
	    );
	 
	    ad.setNegativeButton(
	    button2String,
	    new DialogInterface.OnClickListener(){
	        public void onClick(DialogInterface dialog, int arg1) {
	        	//onResume();
	        	mostropopup = false;
	    		tempo = System.currentTimeMillis();
	        	v.resume();
	        	volumeSuoni();
	        	
//	        	onResume();
	        }
	    }
	    );
	    
	    ad.show();
	}
	
	


	//chiude l'attività e torna al menù del gioco
	private void tornaalmenu(){
		sounds.release();
	    Intent intent = new Intent(this, Menu_princ.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
		//Bundle muta = new Bundle();
		//muta.putBoolean("muta", MUTA);
		//intent.putExtras(muta);
		startActivity(intent); //inizio l'attività nuova
		finish();
		
//		super.onPause();
//		v.pause();
//		sensorManager.unregisterListener(this);
//		finish();
	}


	public class OurView extends SurfaceView implements Runnable{
		

		Thread t = null;
		SurfaceHolder holder; 
		boolean isItOK = false;
		Sprite sprite;
		MissileAirplane missileairplane;
		boolean spriteLoaded = false;
		int scaledSize = getResources().getDimensionPixelSize(R.dimen.gameFontSize);
		
		


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
					 missileairplane = new MissileAirplane(this, Gioca.this.missileaereo );
					 spriteLoaded = true;
				 }

				 Canvas c = holder.lockCanvas();
				 onDraw(c);
				 holder.unlockCanvasAndPost(c);					 
			}
		}
		
		public void onDraw(final Canvas canvas){
			
			
			
			aggiornasfondo(canvas); //fai scrollare lo sfondo		 
			
			paint.setColor(Color.WHITE);
			canvas.drawBitmap(airplane, (int)x - (airplane.getWidth()/2), (int)y - (airplane.getHeight()/2), null);
			//canvas.drawText("x="+x+" y="+y, 10, 40, paint);
			
			//canvas.drawText("WIDTH="+width+" HEIGHT="+height, 10, 100, paint);

			
		    inseriscinemico(canvas);//inserisce i nemici
			mostranemici(listanemici, canvas); //carica la posizione dei nemici dall'array list
			mostranemiciesplosi(listanemiciesplosi, canvas); //carica la posizione dei nemici esplosi dall'array list

		    caricamissilimiei(listamissilimio,canvas); //carica la posizione dei missili dall'array list

		    		   
		  	caricamissilinemici(listamissilinemici,canvas); //carica la posizione dei missili nemici dall'array list
	
		    
		    rimuovinemico(listamissilimio,listanemici,canvas); //nemico colpito da mio missile
		    
		    vitamenomissile(listamissilinemici,canvas); // vitameno per il missile
		    vitamenoaereo(listanemici); //vita meno per la collisione con il nemico
		   
		    pulizia(listamissilimio, listanemici,listamissilinemici);
		    pulizianemiciesplosi (listanemiciesplosi,canvas);		
		    
		    
	       
		    
		   		    
			
//			canvas.drawText("*****lista nemici: "+listanemici.size()+"****", 10, 350, paint);
//			canvas.drawText("*****lista nemiciesplosi: "+listanemiciesplosi.size()+"****", 10, 300, paint);
//			canvas.drawText("*****lista missili: "+tempoingressonemici+"****", 10, 320, paint);
//			canvas.drawText("*****lista missilinemici: "+listamissilinemici.size()+"****", 10, 330, paint);
			
		    Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/8bitoperator.ttf");
		    paint.setTypeface(tf);
			paint.setTextSize(scaledSize);
			paint.setColor(Color.RED);
			//canvas.drawText("VITE: "+VITE+" ", width - (width/3), 30 , paint);
			canvas.drawText(""+punteggio+"", 10, 80 , paint);
			
			controllavite(canvas);
			
			
		}
		
		

		
		
		private void controllavite(Canvas canvas) {
			if(VITE == 3){
				canvas.drawBitmap(cuorepieno, width - cuorepieno.getWidth() - 15 , 30, paint);
				canvas.drawBitmap(cuorepieno, width - cuorepieno.getWidth() - 15 - cuorepieno.getWidth() - 15 , 30, paint);
				canvas.drawBitmap(cuorepieno, width - cuorepieno.getWidth() - 15 - cuorepieno.getWidth() - 15 - cuorepieno.getWidth()- 15 , 30, paint);
				}
			if(VITE == 2){
				canvas.drawBitmap(cuorevuoto, width - cuorepieno.getWidth() - 15 , 30, paint);
				canvas.drawBitmap(cuorepieno, width - cuorepieno.getWidth() - 15 - cuorepieno.getWidth() - 15 , 30, paint);
				canvas.drawBitmap(cuorepieno, width - cuorepieno.getWidth() - 15 - cuorepieno.getWidth() - 15 - cuorepieno.getWidth()- 15 , 30, paint);
				}
			if(VITE == 1){
				canvas.drawBitmap(cuorevuoto, width - cuorepieno.getWidth() - 15 , 30, paint);
				canvas.drawBitmap(cuorevuoto, width - cuorepieno.getWidth() - 15 - cuorepieno.getWidth() - 15 , 30, paint);
				canvas.drawBitmap(cuorepieno, width - cuorepieno.getWidth() - 15 - cuorepieno.getWidth() - 15 - cuorepieno.getWidth()- 15 , 30, paint);

				}
			if(VITE == 0 )
				gameover();
			
			seicolpito(canvas);

		}


		private void gestionedifficolta() {
			if((nemiciuccisi > 5) && ((nemiciuccisi % 5) == 0)){
				tempoingressonemici = tempoingressonemici - 80;
				livello = livello + 1;
				punteggio = (punteggio + (nemiciuccisi * livello))- missililanciati; 
			}	
		}
		
		

		//inserisce il missile del nemico nell'array list
		private MissileNemico creamissilenemico(Sprite nemico){
			MissileNemico nuovo = new MissileNemico(this, missilenemico);
			nuovo.x = nemico.x+(nemico.width/2);
			nuovo.y = nemico.y+(nemico.height/2);
			listamissilinemici.add(nuovo);				
			return nuovo;
		}
		
		//mostra i nemici esplosi
		private void mostranemiciesplosi(ArrayList<Sprite> nemiciesplosi,Canvas canvas) {
			for(int i=0;i<nemiciesplosi.size();i++){
				final Sprite nemicoesploso = nemiciesplosi.get(i);
				//nemicoesploso.n = esplosione;
				//nemicoesploso.onDrawn(canvas);
							
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	nemicoesploso.y = height + 100;
		            	nemicoesploso.numeromissili = 0;
		            }
		        }, 500);
			}			
		}

		//rimuove dallo schermo i missili miei e gli aerei nemici che sono stati messi fuori schermo
		private void pulizia(ArrayList<MissileAirplane> listamissilimio,ArrayList<Sprite> listanemici, ArrayList<MissileNemico> listamissilinemici) {
			for(int i=0;i<listanemici.size();i++){
				if(listanemici.get(i).y > height + 2)
					listanemici.remove(i);
			}
			for(int i=0;i<listamissilimio.size();i++){
				if((listamissilimio.get(i).y < 0) || (listamissilimio.get(i).y > height))
					listamissilimio.remove(i);
			}
			
			for(int i=0;i<listamissilinemici.size();i++){
				if(listamissilinemici.get(i).y > height + listamissilinemici.get(i).height){
					listamissilinemici.get(i).suono = 100;
					listamissilinemici.remove(i);
				}
			}
		}
		
		//rimuove dallo schermo i nemici esplosi
		private void pulizianemiciesplosi(ArrayList<Sprite> nemiciesplosi,Canvas canvas) {
			for(int i=0;i<nemiciesplosi.size();i++){
				if(nemiciesplosi.get(i).y > height + nemiciesplosi.get(i).height)
					nemiciesplosi.remove(i);
			}
		}

		//gestisce le collisioni tra i missili e gli aerei nemici
		private void rimuovinemico(ArrayList<MissileAirplane> listamissilimio,ArrayList<Sprite> listanemici, final Canvas canvas) {
			for(int i=0;i<listanemici.size();i++){
				for(int j=0;j<listamissilimio.size();j++){
					if(listanemici.get(i).n != esplosione){
						if(checkForCollision(listamissilimio.get(j).m, (int)listamissilimio.get(j).x, (int)listamissilimio.get(j).y, listanemici.get(i).n, (int)listanemici.get(i).x, (int)listanemici.get(i).y)){
							
							esplodeaereonemico(listanemici.get(i));		
						    gestionedifficolta(); //gestisci la difficoltà
						    punteggio = punteggio + 10;					    
							nemiciuccisi = nemiciuccisi +1;
							listamissilimio.get(j).y = height + 100; //sposto il missile fuori schermo
						}
					}
				}
			}
		}
		
		//esplode aereo nemico, cambia bitmap e va nell'array list specifico
		private void esplodeaereonemico (Sprite nemico){
			nemico.ySpeed = 0;
			nemico.n = esplosione;
			suonoesplosione();
			listanemiciesplosi.add(nemico);
		}
		
		private void seicolpito(Canvas canvas){
			if(COLPITO != 0){
				canvas.drawBitmap(esplosione, xcolpito, ycolpito, paint);
				COLPITO = COLPITO -1;
			}
		}
		
		//se sei stato colpito da un missile -1 vita e fai sparire il missile che ti ha colpito
		private void vitamenomissile(ArrayList<MissileNemico> listamissilinemici,Canvas canvas){
			for(int i=0;i<listamissilinemici.size();i++){
				MissileNemico missile = listamissilinemici.get(i);
				
//				if(isCollisionDetected(missile.m, (int)missile.x, (int)missile.y, airplane, (int)x, (int)y)){
				if(checkForCollision(missile.m, (int)missile.x + (missile.width/2), (int)missile.y + (missile.height/2), airplane, (int)x, (int)y)){
//					canvas.drawBitmap(esplosione, x, y, paint);
					COLPITO = 10;
					xcolpito = (int)x;
					ycolpito = (int)y;
					suonoesplosione();
					missile.y = height + 100;
					VITE = VITE-1;
				}
				
			}
		}

		

		//se sei stato colpito dall'aereo -1 vita e fai esplodere l'aereo nemico
		private void vitamenoaereo(ArrayList<Sprite> listanemici ) {
			for(int i=0;i<listanemici.size();i++){
				Sprite nemico = listanemici.get(i);
				
				if(nemico.n != esplosione){
//					if(isCollisionDetected(nemico.n, (int)nemico.x, (int)nemico.y, airplane, (int)x, (int)y)){
					if(checkForCollision(nemico.n, (int)nemico.x + (nemico.width/2), (int)nemico.y + (nemico.height/2), airplane, (int)x, (int)y)){
						esplodeaereonemico(nemico);
						VITE = VITE-1;
					}
				}

			}
		}
		
		
		private boolean checkForCollision(Bitmap one, int posXone, int posYone, Bitmap two,int posXtwo, int posYtwo) {
            if (posXone<0 && posXtwo<0 && posYone<0 && posYtwo<0) return false;
            Rect r1 = new Rect(posXone, posYone, posXone + one.getWidth(),  posYone + one.getHeight());
            Rect r2 = new Rect(posXtwo, posYtwo, posXtwo + two.getWidth(), posYtwo + two.getWidth());
            Rect r3 = new Rect(r1);
            if(r1.intersect(r2)) {
                   for (int i = r1.left; i<r1.right; i++) {
                          for (int j = r1.top; j<r1.bottom; j++) {
                                if (one.getPixel(i-r3.left, j-r3.top)!= Color.RED) {
                                      if (two.getPixel(i-r2.left, j-r2.top) != Color.YELLOW) {
                                            return true;
                                     }
                                }
                          }
                     }
              }
              return false;
      }


		//carica la posizione dei missili dall'array list
		private void caricamissilimiei(ArrayList<MissileAirplane> listamissilimio, Canvas canvas) {
			
				for(int i=0;i<listamissilimio.size();i++){
					float xtemp = listamissilimio.get(i).x;
					float ytemp = listamissilimio.get(i).y;
					listamissilimio.get(i).onDrawn(canvas, xtemp, ytemp);
				}
		}
		
		//carica la posizione dei missili nemici dall'array list
		private void caricamissilinemici(ArrayList<MissileNemico> listamissilinemici, Canvas canvas) {
				for(int i=0;i<listamissilinemici.size();i++){
					if(listamissilinemici.get(i).suono == 0){
                		suonomissile();
                		}
					listamissilinemici.get(i).onDrawn(canvas);
				}
		}
		
	
		
		//inserisce un nemico ogni 3 secondi
		private void inseriscinemico(final Canvas canvas){			
			if (tempo == 0)  
		        tempo = System.currentTimeMillis(); //where delay is previously defined long  
		  
		    if (System.currentTimeMillis()>=tempo+tempoingressonemici){ //3000 for our 3 seconds delay  
		    	final Sprite nem = new Sprite (this, nemico);
		    	sceglinemico(nem);
				listanemici.add(nem);
				
				 new Timer().scheduleAtFixedRate(new TimerTask() { 
		                @Override
		                public void run() {
		                	if(nem.numeromissili !=0){
		                		creamissilenemico(nem);   //crea i missili
		                		nem.numeromissili = nem.numeromissili-1;
		                		}
		                }
		            }, 0, tempoingressonemici+800);//put here time 1000 milliseconds=1 second
			
				
				
				tempo = System.currentTimeMillis();
		    }
		    primoavvio = false;
		}
		
				
		//scegli la bitmap del nemico in modo casuale
		private void sceglinemico(Sprite nem) {
			Random rndNumbers = new Random(); 
			int genera    = rndNumbers.nextInt(3)+1;
			
			if(genera == 1){
				nem.n = nemico;
				nem.width = nemico.getWidth();
				nem.height = nemico.getHeight();
			}
			else if(genera == 2){
				nem.n = nemico2;
				nem.width = nemico2.getWidth();
				nem.height = nemico2.getHeight();
			}
			else if(genera == 3){
				nem.n = nemico3;
				nem.width = nemico3.getWidth();
				nem.height = nemico3.getHeight();
			}
			else if(genera == 4){
				nem.n = nemico4;
				nem.width = nemico4.getWidth();
				nem.height = nemico4.getHeight();
			}
		}


		//scorre l'array list e mostra i nemici
		private void mostranemici(ArrayList<Sprite> listanemici,final Canvas canvas) {
			for(int i=0;i<listanemici.size();i++){
				listanemici.get(i).onDrawn(canvas);
			}
		}

		


		public boolean onTouchEvent(MotionEvent ev) {
		    final int action = ev.getAction();
		    switch (action) {
		    case MotionEvent.ACTION_DOWN: {
		        
		        // Remember where we started
//		        mLastTouchX = xpress;
//		        mLastTouchY = ypress;
		        
		        
			        //quando si preme crea un nuovo oggetto missile e lo aggiunge all'array list
					MissileAirplane nuovo = new MissileAirplane(this, missileaereo);
					nuovo.x = x;
					nuovo.y = y;
					suonomissile();
					listamissilimio.add(nuovo);
					missililanciati = missililanciati+1;
									
		       
			        break;
		        
		    	}
		    }
		    return true;
		}
		
		
		
		
		//fa scorrere lo sfondo
		private void aggiornasfondo(Canvas canvas) {
			// decrement the far background
			  mBGFarMoveY = mBGFarMoveY + 1;
	
			  // calculate the wrap factor for matching image draw
			  int newFarY = nuovosfondo.getHeight() - mBGFarMoveY;
			  // if we have scrolled all the way, reset to start
			  if (newFarY <= 0) {
				  mBGFarMoveY = 0;
			   // only need one draw
			   canvas.drawBitmap(nuovosfondo, 0,mBGFarMoveY, null);
			  } else {
			   // need to draw original and wrap
			   canvas.drawBitmap(nuovosfondo, 0,mBGFarMoveY,  null);
			   canvas.drawBitmap(nuovosfondo, 0,-newFarY, null);
			  }

			
					
		}
			
	
		
/*		
		//vede le collisioni
		public boolean isCollisionDetected(Bitmap bitmap1, int x1, int y1, Bitmap bitmap2, int x2, int y2) {

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
*/
	


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
//        		yPosition = (float)sensorEvent.values[1];
//        		
//        		float yfinale = 0;
//        		
//        		if(yPosition < LIMITEMOVY && yPosition > -0.440)
//        			yfinale = 0;
//        		else if(yPosition > 4)
//        			yfinale = (yPosition - LIMITEMOVY) * 5;
//        		else        			
//        			yfinale = yPosition * 5;
        		
        		if((int)x >= v.getWidth()-(airplane.getWidth()/2))
    				x=v.getWidth()-(airplane.getWidth()/2);
    			if((int)x <= airplane.getWidth()/2)
    				x=airplane.getWidth()/2;

        	
        		x -= xPosition*4;
            //	y -=  yfinale;
  
        }
    }



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	
}
