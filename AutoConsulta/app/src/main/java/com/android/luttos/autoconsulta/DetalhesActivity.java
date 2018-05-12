package com.android.luttos.autoconsulta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;

public class DetalhesActivity extends AppCompatActivity {

    private DetalhesHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        helper = new DetalhesHelper(this);

        Intent intent = getIntent();

        Consulta consulta = (Consulta) intent.getSerializableExtra("consulta");
        if (consulta != null){
            helper.popularForm(consulta);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_salvarConsulta:

                Consulta consulta = helper.pegaConsulta();
                ConsultaDAO dao = new ConsultaDAO(this);

                if (consulta.getId() != null){
                    dao.atualizar(consulta);
                    Toast.makeText(DetalhesActivity.this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    dao.inserir(consulta);
                    Toast.makeText(DetalhesActivity.this, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                dao.close();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
