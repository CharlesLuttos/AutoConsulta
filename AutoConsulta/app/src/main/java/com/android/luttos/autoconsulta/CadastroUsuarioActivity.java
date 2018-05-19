package com.android.luttos.autoconsulta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.UsuarioDAO;
import com.android.luttos.autoconsulta.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {
    Button buttonCadastroUsuario;
    Usuario usuario;
    EditText txtNomeUsuario;
    UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cadastro_usuario);

        inicializaBanco();
        inicializarComponentes();

        buttonCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNomeUsuario.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, R.string.toast_usuario_vazio, Toast.LENGTH_SHORT).show();
                } else {
                    usuario = new Usuario(txtNomeUsuario.getText().toString());
                    usuarioDAO.inserir(usuario);
                    finish();
                }
            }
        });

    }

    /**
     * Inicializa banco criando conexao e tabelas
     */
    private void inicializaBanco() {
        usuarioDAO = new UsuarioDAO(getBaseContext());
    }

    /**
     * Instancia os componentes
     */
    private void inicializarComponentes() {
        buttonCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
    }
}