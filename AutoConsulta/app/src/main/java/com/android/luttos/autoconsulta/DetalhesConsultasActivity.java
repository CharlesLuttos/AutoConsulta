package com.android.luttos.autoconsulta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;

public class DetalhesConsultasActivity extends AppCompatActivity {

    private DetalhesConsultasHelper helper;
    private Consulta consulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detalhes_consulta);
        obtemObjetos();
        inicializaObjetos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detalhes_consultas, menu);
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
                    exibirToast(getString(R.string.toast_atualizado));
                } else {
                    dao.inserir(consulta);
                    exibirToast(getString(R.string.toast_atualizado));
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Instancia os objetos
     */
    public void inicializaObjetos() {
        helper = new DetalhesConsultasHelper(this);
        if (consulta != null)
            helper.popularForm(consulta);
    }

    /**
     * Resgata objetos da intent
     */
    public void obtemObjetos() {
        consulta = (Consulta) getIntent().getSerializableExtra("consulta");
    }

    /**
     * Exibe toast
     * @param mensagem mensagem a ser exibida
     */
    public void exibirToast(String mensagem) {
        Toast.makeText(DetalhesConsultasActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
