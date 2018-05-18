package com.android.luttos.autoconsulta;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.luttos.autoconsulta.adapters.UsuarioAdapter;
import com.android.luttos.autoconsulta.dao.DAO;
import com.android.luttos.autoconsulta.dao.UsuarioDAO;
import com.android.luttos.autoconsulta.model.Usuario;

import java.util.ArrayList;

/**********************
 * Activity principal *
 *********************/
public class UsuarioActivity extends AppCompatActivity {
    ArrayList<Usuario> listaUsuarios;
    UsuarioAdapter usuarioAdapter;
    ListView listViewUsuarios;
    Usuario usuario;
    UsuarioDAO usuarioDAO;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_usuario);

        inicializaBanco();
        inicializaComponentes();

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Usuario usuario = (Usuario) listViewUsuarios.getItemAtPosition(position);
                Intent formActivity = new Intent(UsuarioActivity.this, ConsultasActivity.class);
                formActivity.putExtra("usuario", usuario);
                startActivity(formActivity);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroUsuario = new Intent(UsuarioActivity.this, CadastroUsuarioActivity.class);
                startActivity(telaCadastroUsuario);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem apagar = menu.add(R.string.apagar);
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                usuario = (Usuario) listViewUsuarios.getItemAtPosition(info.position);
                usuarioDAO.apagar(usuario);
                carregarLista();
                exibirToast(getString(R.string.usuario_excluido));
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarLista();
    }

    /**
     * Inicializa banco criando conexao e tabelas
     */
    private void inicializaBanco(){
        DAO.getHelper(getBaseContext());
        usuarioDAO  = new UsuarioDAO(getBaseContext());
    }

    /**
     * Instancia os componentes
     */
    private void inicializaComponentes() {
        listViewUsuarios = findViewById(R.id.lista_usuario);
        fab = findViewById(R.id.fab);
    }

    /**
     * Define adapter e carrega lista com dados do banco
     */
    private void carregarLista() {
        listViewUsuarios.setEmptyView(findViewById(android.R.id.empty));
        listaUsuarios = usuarioDAO.listar();
        usuarioAdapter = new UsuarioAdapter(this, listaUsuarios);
        listViewUsuarios.setAdapter(usuarioAdapter);
        // Registra para o menu de contexto (exibido ao manter o toque sobre um item da lista)
        registerForContextMenu(listViewUsuarios);
    }

    /**
     * Exibe toast
     * @param mensagem mensagem a ser exibida
     */
    private void exibirToast(String mensagem) {
        Toast.makeText(UsuarioActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
