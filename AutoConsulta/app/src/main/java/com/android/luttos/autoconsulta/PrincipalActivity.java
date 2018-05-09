package com.android.luttos.autoconsulta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeLayout;
    ListView listView;
    ArrayList<Consulta> listaConsultas;
    ConsultaAdapter consultaAdapter;
    ConsultaDAO consultaDAO = new ConsultaDAO(PrincipalActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroIntent = new Intent(PrincipalActivity.this, CadastroActivity.class);
                startActivityForResult(telaCadastroIntent, 1);
            }
        });

        definirSwipeToPush();
        carregarLista();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int codigo = data.getIntExtra("Solicitacao", 0);
        new ConsultaDAO(this).inserir(new Consulta(codigo));
        Toast.makeText(getApplicationContext(), "Adicionado", Toast.LENGTH_SHORT).show();
    }

    public void carregarLista() {
        listView = findViewById(R.id.ListaConsulta);
        listView.setEmptyView(findViewById(android.R.id.empty));
        listaConsultas = consultaDAO.listar();
        consultaAdapter = new ConsultaAdapter(this, listaConsultas);
        listView.setAdapter(consultaAdapter);
        registerForContextMenu(listView);
    }

    /**
     * Evento para a açāo de deslizar o dedo para baixo na tela do Android
     */
    public void definirSwipeToPush() {
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Codigo funcional
                Toast.makeText(getApplicationContext(), "Atualizado", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        if (view.getId() == R.id.ListaConsulta){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_opcoes, menu);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarLista();
    }
}
