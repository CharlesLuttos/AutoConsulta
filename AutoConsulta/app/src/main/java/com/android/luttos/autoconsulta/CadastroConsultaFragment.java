package com.android.luttos.autoconsulta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by pesso on 09/06/2018.
 */

public class CadastroConsultaFragment extends android.app.DialogFragment {
    private static final String TAG = "CadastroConsultaFragment";
    private static final String USUARIO = "usuario";
    int codigoConsulta;
    EditText txtCodigo;
    ProgressDialog pd;
    JSONObject jsonObject;
    AlertDialog.Builder alerta;
    ConsultaDAO consultaDAO;
    Consulta consulta;
    Usuario usuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.layout_cadastro_consulta, container, false);
        Button btn = view.findViewById(R.id.btnCadastroCodigo);
        alerta = new AlertDialog.Builder(getActivity());
        consultaDAO = new ConsultaDAO(getActivity());
        txtCodigo = view.findViewById(R.id.txtCodigo);

        usuario = (Usuario) getArguments().getSerializable(USUARIO);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoConsulta = Integer.parseInt(txtCodigo.getText().toString());
                new ObterDadosJson().execute("http://luttos.com/autoconsulta/" + codigoConsulta);
            }
        });
        return view;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    public static CadastroConsultaFragment newInstance(Usuario usuario) {
        CadastroConsultaFragment fragment = new CadastroConsultaFragment();
        Bundle args = new Bundle();
        args.putSerializable(USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    /**
     * Classe para obter os dados da API em Json
     */
    @SuppressLint("StaticFieldLeak")
    public class ObterDadosJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage(getActivity().getResources().getString(R.string.aguarde));
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

                while ((line = reader.readLine()) != null) {
                    String lineBreak = line + "/n";
                    buffer.append(lineBreak);
                    Log.d("Response: ", "> " + line);
                }

                String jsonString = buffer.toString();

                try {
                    jsonObject = new JSONObject(jsonString);
                    Log.d("Paciente: ", jsonObject.get("paciente").toString());
                    Log.d("Procedimento: ", jsonObject.get("procedimento").toString());
                    Log.d("Unidade solicitante: ", jsonObject.get("unidade_solicitante").toString());
                    Log.d("Local atendimento: ", jsonObject.get("local_atendimento").toString());
                    Log.d("Situacao: ", jsonObject.get("situacao").toString());
                } catch (Throwable T) {
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
                } catch (IOException IOEx) {
                    IOEx.printStackTrace();
                }

                try {
                    if (jsonObject != null) {
                        formarObjetoConsulta(jsonObject, false);

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
        protected void onPostExecute(String result) {
         pd.dismiss();
         dismiss();
        }
    }

    public void exibirToast(String mensagem, int duracao) {
        Toast.makeText(getActivity(), mensagem, duracao).show();
    }

    public void exibirAlertDialog(String titulo, String mensagem) {
        alerta.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    /**
     * Forma um objeto Consulta a partir de um JSON
     * @param jsonObject objeto JSON com os dados da consulta
     * @param test Flag de testes
     * @return Objeto de consulta com os dados do JSON
     * @throws JSONException Ao nao conseguir montar o objeto
     */
    public Consulta formarObjetoConsulta(JSONObject jsonObject, boolean test) throws JSONException {
        consulta = new Consulta();
        boolean invalido = true;

        if(jsonObject.has("cod_consulta")) {
            consulta.setCodigoConsulta(jsonObject.getInt("cod_consulta"));
            invalido = false;
        } else {
            invalido = true;
        }
        if(jsonObject.has("paciente")) {
            consulta.setPaciente(jsonObject.get("paciente").toString());
            invalido = false;
        } else {
            invalido = true;
        }
        if(jsonObject.has("procedimento")) {
            consulta.setProcedimento(jsonObject.get("procedimento").toString());
            invalido = false;
        } else {
            invalido = true;
        }
        if(jsonObject.has("unidade_solicitante")) {
            consulta.setUnidadeSolicitante(jsonObject.get("unidade_solicitante").toString());
            invalido = false;
        } else {
            invalido = true;
        }
        if(jsonObject.has("local_atendimento")) {
            consulta.setLocal(jsonObject.get("local_atendimento").toString());
            invalido = false;
        } else {
            invalido = true;
        }
        if(jsonObject.has("situacao")) {
            consulta.setSituacao(Integer.parseInt(jsonObject.get("situacao").toString()));
            invalido = false;
        } else {
            invalido = true;
        }
        consulta.setUsuario(usuario);
        if(!test && !invalido) {
            inserirConsulta(consulta);
        }
        return consulta;
    }


    private void inserirConsulta(Consulta c) {
        try {
            consultaDAO.inserir(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
