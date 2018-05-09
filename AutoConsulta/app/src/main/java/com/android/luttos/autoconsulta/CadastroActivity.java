package com.android.luttos.autoconsulta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;

public class CadastroActivity extends AppCompatActivity {
    EditText txtCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtCodigo = findViewById(R.id.txtCodigo);
    }

    public void cadastroCodigo(View view) {
        new ConsultaDAO(this).inserir(new Consulta(Integer.parseInt(txtCodigo.getText().toString())));
        finish();
    }
}
