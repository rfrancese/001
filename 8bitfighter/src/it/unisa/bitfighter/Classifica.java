/*Attività per la classifica*/
package it.unisa.bitfighter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.bitfighter.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class Classifica extends Activity {
	
	TableLayout ll;
	String nienteconnessione = "";
	String carica;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		String lingua = b.getString("lang"); // prendo la lingua dall'extra
            
		
		setContentView(R.layout.activity_classifica);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		TextView titoloclassifica = (TextView)findViewById(R.id.classifica);
	    Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/8bitoperator.ttf");
	    titoloclassifica.setTypeface(tf);
		
		ll = (TableLayout) findViewById(R.id.tabellaclassifica);
                	
		new task().execute(); //legge la classifica dal database

       
        /*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			titoloclassifica.setText(getText(R.string.classificaITA));
			nienteconnessione = (String) getText(R.string.nessunaconnessioneITA);
			carica = getString(R.string.caricamentoITA);
		}
		else if (lingua.equals("FRA")){
			titoloclassifica.setText(getText(R.string.classificaFRA));
			nienteconnessione = (String) getText(R.string.nessunaconnessioneFRA);
			carica = getString(R.string.caricamentoFRA);
		}
		else if(lingua.equals("ENG")){
			titoloclassifica.setText(getText(R.string.classificaENG));
			nienteconnessione = (String) getText(R.string.nessunaconnessioneENG);
			carica = getString(R.string.caricamentoENG);
		}
	



	}
	
	
	 class task extends AsyncTask<String, String, Void>
	    {
	    private ProgressDialog progressDialog = new ProgressDialog(Classifica.this);
	        InputStream is = null ;
	        String result = "";
	        protected void onPreExecute() {
	           progressDialog.setMessage(carica);
	           progressDialog.show();
	           progressDialog.setOnCancelListener(new OnCancelListener() {
	        	   
	        	   
	    	@Override
	    		public void onCancel(DialogInterface arg0) {
	    			task.this.cancel(true);
	    	   }
	    	});
	         }
	           @Override
	    	protected Void doInBackground(String... params) {
	    	  String url_select = "http://aerofighter8bit.altervista.org/ANDROID_interrogaclassifica.php";

	    	  HttpClient httpClient = new DefaultHttpClient();
	    	  HttpPost httpPost = new HttpPost(url_select);

	            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

		    	try {
		    		httpPost.setEntity(new UrlEncodedFormEntity(param));
		    		HttpResponse httpResponse = httpClient.execute(httpPost);
		    		HttpEntity httpEntity = httpResponse.getEntity();
	
		    		//read content
		    		is =  httpEntity.getContent();		


	    		} catch (Exception e) {
	    		
	    		noconn("niente");
	    		progressDialog.dismiss();
	    		Log.e("log_tag", "Error in http connection "+e.toString());
	    		}
	    	try {
	    	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    		StringBuilder sb = new StringBuilder();
	    		String line = "";
	    		while((line=br.readLine())!=null)
	    		{
	    		   sb.append(line+"\n");
	    		}
	    			is.close();
	    			result=sb.toString();				

	    				} catch (Exception e) {
	    					// TODO: handle exception
	    					Log.e("log_tag", "Error converting result "+e.toString());
	    				}

	    			return null;

	    		}
	           
	           
	    	protected void onPostExecute(Void v) {
	    		

	    		// ambil data dari Json database
	    		try {
	    			JSONArray Jarray = new JSONArray(result);
	    			for(int i=0;i<Jarray.length();i++){
		    			JSONObject Jasonobject = null;
		    			//text_1 = (TextView)findViewById(R.id.datiRicevuti);
		    			Jasonobject = Jarray.getJSONObject(i);
		
		    			//get an output on the screen
		    			String id = Jasonobject.getString("id");
		    			String nome = Jasonobject.getString("nome");
		    			String punti = Jasonobject.getString("punteggio");
		    			aggiungi(nome,punti,i); //aggiunge la riga alla tabella per visualizzare l'entrata trovata

	    			}
	    			this.progressDialog.dismiss();

	    		} catch (Exception e) {
	    			// TODO: handle exception
	    			Log.e("log_tag", "Error parsing data "+e.toString());
	    		}
	    	}
	    	
	    		    	
	   	
			
	  }
	 
	 
	 private void noconn(String testo){
		 TableRow row= new TableRow(this);
	     TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
	     lp.setMargins(0, 5, 0, 5);
	     row.setLayoutParams(lp);
	     TextView utente = new TextView(this);
	     utente.setText(testo);
	     utente.setTextColor(Color.WHITE);
	     row.addView(utente);
	     ll.addView(row,0);
	 }
	 
	 private void aggiungi(String nome, String punti, int i) {
		 Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/8bitoperator.ttf");
		 TableRow row= new TableRow(this);
	     TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
	     lp.setMargins(0, 5, 0, 5);
	     TextView utente = new TextView(this);
	     TextView punteggio = new TextView(this);
	     utente.setTypeface(tf);
	     punteggio.setTypeface(tf);
	     utente.setText(nome);
	     utente.setTextSize(getResources().getDimensionPixelSize(R.dimen.classificaFontSize));
	     utente.setTextColor(Color.WHITE);
	     punteggio.setText(punti);
	     punteggio.setTextSize(getResources().getDimensionPixelSize(R.dimen.classificaFontSize));
	     punteggio.setGravity(Gravity.RIGHT);
	     punteggio.setTextColor(Color.WHITE);
	     row.addView(utente);
	     row.addView(punteggio);
	     ll.addView(row,i);
		}
	 
	 
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_esci) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

}
