package it.unisa.bitfighter;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.bitfighter.R;
import com.example.bitfighter.R.id;
import com.example.bitfighter.R.layout;
import com.example.bitfighter.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class GameOver extends Activity {
	
	String lingua;
	int punteggio;
	String nome = "";
	long tempo =0;
	String nomevuoto;
	boolean esci = false;
	
	EditText cliccaperinserire;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		lingua = b.getString("lang"); // prendo la lingua dall'extra
		punteggio = b.getInt("punteggio"); //prendo il punteggio ottenuto
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		TextView haitotalizzato = (TextView)findViewById(R.id.haitotalizzato);
		TextView inseriscinome = (TextView)findViewById(R.id.inseriscinome);
		cliccaperinserire = (EditText)findViewById(R.id.editText1);
		Button invia = (Button)findViewById(R.id.invia);
		
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/8bitoperator.ttf");
		haitotalizzato.setTextSize(getResources().getDimensionPixelSize(R.dimen.haitotalizzato));
		haitotalizzato.setTypeface(tf);
		
		/*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			haitotalizzato.setText((getText(R.string.haitotalizzatoITA))+" "+Integer.toString(punteggio)+" "+(getText(R.string.puntiITA)));
			inseriscinome.setText(getText(R.string.inseriscinomeITA));	
			cliccaperinserire.setHint(getText(R.string.cliccaperinserireITA));
			invia.setText(getText(R.string.inviaITA));
			nomevuoto = (String) getText(R.string.nomevuotoITA);
			
		}
		else if (lingua.equals("FRA")){
			haitotalizzato.setText((getText(R.string.haitotalizzatoFRA))+" "+Integer.toString(punteggio)+" "+(getText(R.string.puntiFRA)));
			inseriscinome.setText(getText(R.string.inseriscinomeFRA));	
			cliccaperinserire.setHint(getText(R.string.cliccaperinserireFRA));
			invia.setText(getText(R.string.inviaFRA));
			nomevuoto = (String) getText(R.string.nomevuotoFRA);
		}
		else if(lingua.equals("ENG")){
			haitotalizzato.setText((getText(R.string.haitotalizzatoENG))+" "+Integer.toString(punteggio)+" "+(getText(R.string.puntiENG)));
			inseriscinome.setText(getText(R.string.inseriscinomeENG));	
			cliccaperinserire.setHint(getText(R.string.cliccaperinserireENG));
			invia.setText(getText(R.string.inviaENG));
			nomevuoto = (String) getText(R.string.nomevuotoENG);
		}
		
		if(leggiNomedaFile().length() >= 1)
    		cliccaperinserire.setText(leggiNomedaFile());
		

		
		invia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nome = cliccaperinserire.getText().toString();
    	        Log.e("***NOME", "" + nome);
    	        
    	        if (nome.matches("")) {
    	        	mostraToast(nomevuoto);
    	        	return;
    	        }
    	        else{
    	        	new inserisciinclassifica().execute();
    	        }	
                
            }

			
        }); 
			
	}
	
	
	private void mostraToast(String string) {
    	Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
	
	
	public void scriviNomeaFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("nome.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	       // Log.d("SCRITTO", "HO SCRITTO " + data);
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}


	public String leggiNomedaFile() {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput("nome.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	
	
	public void onBackPressed()  
	{  
		tornamenu();
	}
	
	
	private void tornamenu() {
		Intent intent = new Intent(this, Menu_princ.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    startActivity(intent);
	    finish();
	    
	}


	class inserisciinclassifica extends AsyncTask<String, String, Void>
    {
    private ProgressDialog progressDialog = new ProgressDialog(GameOver.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
	           progressDialog.setMessage("...");
	           progressDialog.show();
//	           progressDialog.setOnCancelListener(new OnCancelListener()
        }

        protected void onPostExecute(Void result) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss(); 
            }
        }

		
		
		
           @Override
    	protected Void doInBackground(String... params) {
        	   
        	   
    	  String url_select = "http://aerofighter8bit.altervista.org/ANDROID_inserisciclassifica.php";

    	  HttpClient httpClient = new DefaultHttpClient();
    	  HttpPost httpPost = new HttpPost(url_select);


	    try {
	            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("nome", nome));
	            param.add(new BasicNameValuePair("punteggio", Integer.toString(punteggio)));
	    		httpPost.setEntity(new UrlEncodedFormEntity(param));
	
	    		HttpResponse httpResponse = httpClient.execute(httpPost);
//	    		HttpEntity httpEntity = httpResponse.getEntity();
	
//	    		//read content
//	    		is =  httpEntity.getContent();		
	    		
	    		scriviNomeaFile(nome);
	    		tornamenu();
    		} catch (Exception e) {

    			Log.e("log_tag", "Error in http connection "+e.toString());
    		}
    	

    			return null;

    		}
               	
		
  }
	
	
	

}
