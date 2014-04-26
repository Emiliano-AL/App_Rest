package com.test.apprest.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Lista extends ActionBarActivity {

    private ListView lstUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lstUsers = (ListView) findViewById(R.id.lstUsers);
        TareaWSListar task = new TareaWSListar();
        task.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Clase para obtener el listado
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {
        private String[] clientes;
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del = new HttpGet("http://10.0.2.2/WebServiceRestAndroid/Clientes/clientes");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                clientes = new String[respJSON.length()];
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    int idCli = obj.getInt("Id");
                    String nombCli = obj.getString("Nombre");
                    int telefCli = obj.getInt("Telefono");
                    clientes[i] = "" + idCli + "-" + nombCli + "-" + telefCli;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result)
            {
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Lista.this, android.R.layout.simple_list_item_1, clientes);
                lstUsers.setAdapter(adaptador);
            }
        }
    }
}
