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

import com.android.luttos.autoconsulta.dao.ConsultaDAO2;
import com.android.luttos.autoconsulta.dao.DAO;
import com.android.luttos.autoconsulta.dao.UsuarioDAO2;
import com.android.luttos.autoconsulta.model.Usuario;

import java.util.ArrayList;

public class UsuarioActivity extends AppCompatActivity {
    ArrayList<Usuario> listaUsuarios;
    UsuarioAdapter usuarioAdapter;
    ListView listViewUsuarios;
    Usuario usuario;
    UsuarioDAO2 usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        listViewUsuarios = findViewById(R.id.lista_usuario);
        DAO.getHelper(getApplicationContext());
        usuarioDAO  = new UsuarioDAO2(getBaseContext());

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Usuario usuario = (Usuario) listViewUsuarios.getItemAtPosition(position);
                Intent formActivity = new Intent(UsuarioActivity.this, PrincipalActivity.class);
                formActivity.putExtra("usuario", usuario);

                startActivity(formActivity);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroUsuario = new Intent(UsuarioActivity.this, CadastroUsuario.class);
                startActivity(telaCadastroUsuario);
            }
        });
    }

    /**
     * Define adapter e carrega lista com dados do banco
     */
    public void carregarLista() {
        listViewUsuarios.setEmptyView(findViewById(android.R.id.empty));
        listaUsuarios = usuarioDAO.listar();
        usuarioAdapter = new UsuarioAdapter(this, listaUsuarios);
        listViewUsuarios.setAdapter(usuarioAdapter);
        registerForContextMenu(listViewUsuarios);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem apagar = menu.add("Apagar");
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                usuario = (Usuario) listViewUsuarios.getItemAtPosition(info.position);
                usuarioDAO.apagar(usuario);
                carregarLista();
                exibirToast("Usuário excluído!");
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
     * Exibe toast
     * @param mensagem mensagem a ser exibida
     */
    public void exibirToast(String mensagem) {
        Toast.makeText(UsuarioActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
