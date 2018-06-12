package com.android.luttos.autoconsulta.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.luttos.autoconsulta.CadastroConsultasActivity;
import com.android.luttos.autoconsulta.R;
import com.android.luttos.autoconsulta.model.Consulta;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ObterDadosJson2 extends AsyncTask<String, String, String> {
    private WeakReference<CadastroConsultasActivity> cadastroConsultasActivityWeakReference;
    private final String URL = "http://192.168.7.2:8000/autoconsulta/";
    private JSONObject jsonObject;
    private ProgressDialog pd;
    private Consulta consulta;

    public ObterDadosJson2(CadastroConsultasActivity cadastroConsultasActivity) {
        cadastroConsultasActivityWeakReference = new WeakReference<>(cadastroConsultasActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(cadastroConsultasActivityWeakReference.get());
        pd.setMessage(cadastroConsultasActivityWeakReference.get().getResources().getString(R.string.aguarde));
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            java.net.URL url = new URL(URL+strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                String lineBreak = line + "/n";
                buffer.append(lineBreak);
                Log.d("Response: ", "> " + line);
            }

            String jsonString = buffer.toString();

            try {
                jsonObject = new JSONObject(jsonString);
                setObjetoJSON(jsonObject);
            } catch (Throwable T) {
                T.printStackTrace();
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("Falha na URL", "URL de serviços inválida");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Falha na API","Falha na conexão com a API de serviços");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException IOEx) {
                IOEx.printStackTrace();
            }

//            cadastroConsultasActivityWeakReference.get().finish();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(cadastroConsultasActivityWeakReference.get().getBaseContext(),"Consulta cadastrada!", Toast.LENGTH_SHORT).show();
    }

    public JSONObject getObjetoJSON() {
        return jsonObject;
    }

    private void setObjetoJSON(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
}
