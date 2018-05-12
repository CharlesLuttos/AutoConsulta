package com.android.luttos.autoconsulta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
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

        listView = findViewById(R.id.lista_consulta);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Consulta consulta = (Consulta) listView.getItemAtPosition(position);
                Intent formActivity = new Intent(PrincipalActivity.this, DetalhesActivity.class);
                formActivity.putExtra("consulta", consulta);
                startActivity(formActivity);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroIntent = new Intent(PrincipalActivity.this, CadastroActivity.class);
                startActivity(telaCadastroIntent);
            }
        });

        definirSwipeToPush();
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

    public void carregarLista() {
        listView = findViewById(R.id.lista_consulta);
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
                carregarLista();
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
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem buscarMapa = menu.add("Buscar no Mapa");
        MenuItem criarAlerta = menu.add("Criar Alerta");
        MenuItem apagar = menu.add("Apagar");
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                Consulta consulta = (Consulta) listView.getItemAtPosition(info.position);
                ConsultaDAO dao = new ConsultaDAO(PrincipalActivity.this);
                dao.apagar(consulta);
                dao.close();
                carregarLista();
                Toast.makeText(PrincipalActivity.this, "Consulta Excluída!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarLista();
    }
}
