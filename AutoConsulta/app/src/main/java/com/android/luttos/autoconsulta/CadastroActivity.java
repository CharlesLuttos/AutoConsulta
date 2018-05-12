package com.android.luttos.autoconsulta;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CadastroActivity extends AppCompatActivity {
    EditText txtCodigo;
    Button button;
    Consulta consulta;
    int codigoConsulta;
    ConsultaDAO consultaDAO;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        consultaDAO = new ConsultaDAO(this);

        txtCodigo = findViewById(R.id.txtCodigo);

        button = findViewById(R.id.btnCadastroCodigo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ObterDadosJson().execute("http://samples.openweathermap.org/data/2.5/weather?q=London,uk&coord&appid=b6907d289e10d714a6e88b30761fae22");
                codigoConsulta = Integer.parseInt(txtCodigo.getText().toString());
                new ObterDadosJson().execute("http://luttos.com/autoconsulta/"+codigoConsulta);
            }
        });
    }

    /**
     * Classe para obter os dados da API em Json
     */
    private class ObterDadosJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null){
                    String lineBreak = line+"/n";
                    buffer.append(lineBreak);
                    Log.d("Response: ", "> "+line);
                }

                String jsonString = buffer.toString();

                try {
                    jsonObject = new JSONObject(jsonString);
                    Log.d("Paciente: ", jsonObject.get("paciente").toString());
                    Log.d("Procedimento: ", jsonObject.get("procedimento").toString());
                    Log.d("Unidade solicitante: ", jsonObject.get("unidade_solicitante").toString());
                    Log.d("Local atendimento: ", jsonObject.get("local_atendimento").toString());
                    Log.d("Situacao: ", jsonObject.get("situacao").toString());
                }catch (Throwable T) {
                    T.printStackTrace();
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                }catch (IOException IOEx) {
                    IOEx.printStackTrace();
                }

                try {
                    formarObjetoConsulta(jsonObject);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){

        }
    }

    private void formarObjetoConsulta(JSONObject jsonObject) throws JSONException{
        consulta = new Consulta();
        consulta.setCodigoConsulta(codigoConsulta);
        consulta.setPaciente(jsonObject.get("paciente").toString());
        consulta.setProcedimento(jsonObject.get("procedimento").toString());
        consulta.setUnidadeSolicitante(jsonObject.get("unidade_solicitante").toString());
        consulta.setLocal(jsonObject.get("local_atendimento").toString());
        consulta.setSituacao(Integer.parseInt(jsonObject.get("situacao").toString()));
        inserirConsulta(consulta);
    }

    private void inserirConsulta(Consulta c) {
        try {
             consultaDAO.inserir(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
