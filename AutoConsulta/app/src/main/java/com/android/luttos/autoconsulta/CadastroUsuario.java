package com.android.luttos.autoconsulta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.luttos.autoconsulta.dao.UsuarioDAO2;
import com.android.luttos.autoconsulta.dao.UsuarioDAO2;
import com.android.luttos.autoconsulta.model.Usuario;

public class CadastroUsuario extends AppCompatActivity {
    Button buttonCadastroUsuario;
    Usuario usuario;
    EditText txtNomeUsuario;
    UsuarioDAO2 usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        buttonCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        buttonCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioDAO = new UsuarioDAO2(CadastroUsuario.this);
                usuario = new Usuario(txtNomeUsuario.getText().toString());
                usuarioDAO.inserir(usuario);
                finish();
            }
        });

    }
}
