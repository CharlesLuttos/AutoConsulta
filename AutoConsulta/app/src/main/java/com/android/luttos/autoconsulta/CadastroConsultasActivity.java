package com.android.luttos.autoconsulta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;
import com.android.luttos.autoconsulta.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CadastroConsultasActivity extends AppCompatActivity {
    EditText txtCodigo;
    Button button;
    Consulta consulta;
    String codigoConsulta;
    ConsultaDAO consultaDAO;
    JSONObject jsonObject;
    ProgressDialog pd;
    AlertDialog.Builder alerta;
    Usuario usuario;
    ObterDadosJson obterDadosJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cadastro_consulta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resgatarObjetos();
        inicializarObjetos();
        inicializarComponentes();
        definirToolbarIcon();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroConsultasActivity.this, R.string.toast_consulta_vazio, Toast.LENGTH_SHORT).show();
                }else {
                    codigoConsulta = txtCodigo.getText().toString();
                    obterDadosJson.execute(codigoConsulta);
                }
            }
        });
    }

    /**
     * Resgata objetos de uma intent
     */
    private void resgatarObjetos() {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
    }

    /**
     * Instancia demais objetos
     */
    private void inicializarObjetos() {
        alerta = new AlertDialog.Builder(this);
        consultaDAO = new ConsultaDAO(getBaseContext());
        obterDadosJson = new ObterDadosJson();
    }

    /**
     * Instancia os componentes
     */
    private void inicializarComponentes() {
        txtCodigo = findViewById(R.id.txtCodigo);
        button = findViewById(R.id.btnCadastroCodigo);
    }

    /**
     * Classe para obter os dados da API em Json
     */
    @SuppressLint("StaticFieldLeak")
    private class ObterDadosJson extends AsyncTask<String, String, String> {
        private boolean malformedURL = false;
        private boolean ioex = false;
        private boolean ex = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CadastroConsultasActivity.this);
            pd.setMessage(CadastroConsultasActivity.this.getResources().getString(R.string.aguarde));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                String URL = "http://192.168.7.2:8000/autoconsulta/";
                URL url = new URL(URL + strings[0]);
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
                    consulta = formarObjetoConsulta(jsonObject);
                } catch (Throwable T) {
                    T.printStackTrace();
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                Log.d("MalformedURLException", e.getMessage());
                malformedURL = true;
            } catch (IOException e) {
                Log.d("IOException", e.getMessage());
                ioex = true;
            } catch (Exception ex) {
                Log.d("Exception", ex.getMessage());
                this.ex = true;
            } finally {
                if (consulta != null) {
                    inserirConsulta(consulta);
                }
                if (!malformedURL && !ioex && !ex) finish();
                else pd.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (malformedURL)
                exibirToast("URL de serviços inválida", Toast.LENGTH_SHORT);
            if (ioex)
                exibirToast("Falha na conexão com a API de serviços", Toast.LENGTH_SHORT);
            if (ex)
                exibirToast("Erro", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Forma um objeto Consulta a partir de um JSON
     * @param jsonObject objeto JSON com os dados da consulta
     * @return Objeto de consulta com os dados do JSON
     * @throws JSONException Ao nao conseguir montar o objeto
     */
    public Consulta formarObjetoConsulta(JSONObject jsonObject) throws JSONException {
        consulta = new Consulta();
        boolean invalido = false;

        if(jsonObject.has("cod_consulta")) {
            consulta.setCodigoConsulta(jsonObject.getInt("cod_consulta"));
        } else {
            invalido = true;
        }
        if(jsonObject.has("paciente")) {
            consulta.setPaciente(jsonObject.get("paciente").toString());
        } else {
            invalido = true;
        }
        if(jsonObject.has("procedimento")) {
            consulta.setProcedimento(jsonObject.get("procedimento").toString());
        } else {
            invalido = true;
        }
        if(jsonObject.has("unidade_solicitante")) {
            consulta.setUnidadeSolicitante(jsonObject.get("unidade_solicitante").toString());
        } else {
            invalido = true;
        }
        if(jsonObject.has("local_atendimento")) {
            consulta.setLocal(jsonObject.get("local_atendimento").toString());
        } else {
            invalido = true;
        }
        if(jsonObject.has("situacao")) {
            consulta.setSituacao(Integer.parseInt(jsonObject.get("situacao").toString()));
        } else {
            invalido = true;
        }
        consulta.setUsuario(usuario);
        if(!invalido) {
            return consulta;
        }
        return null;
    }

    /**
     * Insere consulta no banco de dados
     * @param consulta Objeto de Consulta
     */
    private void inserirConsulta(Consulta consulta) {
        try {
            consultaDAO.inserir(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exibirToast(String mensagem, int duracao) {
        Toast.makeText(this, mensagem, duracao).show();
    }

    @SuppressWarnings("unused")
    public void exibirAlertDialog(String titulo, String mensagem) {
        alerta.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Define icone da toolbar
     */
    private void definirToolbarIcon() {
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_toolbar);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
