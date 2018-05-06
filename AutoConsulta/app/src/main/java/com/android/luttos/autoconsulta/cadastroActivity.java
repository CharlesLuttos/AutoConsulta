package com.android.luttos.autoconsulta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class cadastroActivity extends AppCompatActivity {

    EditText txtCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        txtCodigo = findViewById(R.id.editText2);

    }

        public void cadastroCodigo(View view){
        Intent returnIntent = new Intent(this, PrincipalActivity.class);
        int codigo = Integer.parseInt(txtCodigo.getText().toString());
        returnIntent.putExtra("Solicitacao", codigo);
        setResult(1, returnIntent);
        finish();
    }
}
