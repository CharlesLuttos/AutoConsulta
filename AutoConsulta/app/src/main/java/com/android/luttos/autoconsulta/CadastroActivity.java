package com.android.luttos.autoconsulta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;
import com.android.luttos.autoconsulta.util.Util;

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
    ProgressDialog pd;
    AlertDialog.Builder alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        alerta = new AlertDialog.Builder(this);

        consultaDAO = new ConsultaDAO(this);

        txtCodigo = findViewById(R.id.txtCodigo);

        button = findViewById(R.id.btnCadastroCodigo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCodigo.getText() != null && txtCodigo.getText().length() > 0){
                    codigoConsulta = Integer.parseInt(txtCodigo.getText().toString());
                    new ObterDadosJson().execute("http://172.16.1.167/autoconsulta/"+codigoConsulta);

                } else {
                    exibirAlertDialog("Dados", "Digite o código da solicitaçao");
                }
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
            pd = new ProgressDialog(CadastroActivity.this);
            pd.setMessage("Aguarde");
            pd.setCancelable(false);
            pd.show();
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
                exibirAlertDialog("Falha na API", "API de serviços inválida");
            } catch (IOException e) {
                e.printStackTrace();
                exibirToast("Falha na conexão com a API de serviços", Toast.LENGTH_SHORT);
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
                }catch (IOException IOEx) {
                    IOEx.printStackTrace();
                }

                try {
                    if (jsonObject != null){
                        formarObjetoConsulta(jsonObject);
                        finish();
                    } else {
                        exibirToast("Consulta não cadastrada", Toast.LENGTH_LONG);
                    }
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

    public void exibirToast(String mensagem, int duracao){
        Toast.makeText(this, mensagem, duracao).show();
    }

    public void exibirAlertDialog(String titulo, String mensagem) {
        alerta.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
