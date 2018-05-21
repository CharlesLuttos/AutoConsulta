package com.android.luttos.autoconsulta;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.UsuarioDAO;
import com.android.luttos.autoconsulta.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private Button buttonCadastroUsuario;
    private Usuario usuario;
    private EditText txtNomeUsuario;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cadastro_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializaBanco();
        inicializarComponentes();
        definirToolbarIcon();
        getButtonCadastroUsuario().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTxtNomeUsuario().getText().toString().isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, R.string.toast_usuario_vazio, Toast.LENGTH_SHORT).show();
                } else {
                    setUsuario(new Usuario(getTxtNomeUsuario().getText().toString()));
                    getUsuarioDAO().inserir(getUsuario());
                    finish();
                }
            }
        });

    }

    /**
     * Inicializa banco criando conexao e tabelas
     */
    private void inicializaBanco() {
        setUsuarioDAO(new UsuarioDAO(getBaseContext()));
    }

    /**
     * Instancia os componentes
     */
    private void inicializarComponentes() {
        setButtonCadastroUsuario((Button) findViewById(R.id.btnCadastroUsuario));
        setTxtNomeUsuario((EditText) findViewById(R.id.txtNomeUsuario));
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

    public Button getButtonCadastroUsuario() {
        return buttonCadastroUsuario;
    }

    public void setButtonCadastroUsuario(Button buttonCadastroUsuario) {
        this.buttonCadastroUsuario = buttonCadastroUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EditText getTxtNomeUsuario() {
        return txtNomeUsuario;
    }

    public void setTxtNomeUsuario(EditText txtNomeUsuario) {
        this.txtNomeUsuario = txtNomeUsuario;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
}
