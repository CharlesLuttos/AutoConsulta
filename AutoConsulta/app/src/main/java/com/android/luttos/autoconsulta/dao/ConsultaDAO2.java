package com.android.luttos.autoconsulta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.android.luttos.autoconsulta.model.Consulta;
import com.android.luttos.autoconsulta.model.Usuario;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsultaDAO2 implements Serializable{

    SQLiteDatabase database;
    public DAO dao;
    Context context;

    public ConsultaDAO2(Context context) {
        this.context = context;
        dao = DAO.getHelper(context);
        open();
    }

    private void open() {
        if (dao == null)
            dao = DAO.getHelper(context);
        database = dao.getWritableDatabase();
    }

    public void inserir(Consulta consulta) {
        ContentValues dados = pegarDadosConsulta(consulta);
        database.insert("Consultas", null, dados);
    }

    @NonNull
    private ContentValues pegarDadosConsulta(Consulta consulta) {
        ContentValues dados = new ContentValues();
        dados.put("codigoConsulta", consulta.getCodigoConsulta());
        dados.put("paciente", consulta.getPaciente());
        dados.put("procedimento", consulta.getProcedimento());
        dados.put("data", consulta.getData());
        dados.put("unidadeSolicitante", consulta.getUnidadeSolicitante());
        dados.put("local", consulta.getLocal());
        dados.put("situacao", consulta.getSituacao());
        dados.put("idUsuario", consulta.getUsuario().getId());
        return dados;
    }

    public ArrayList<Consulta> listar(Usuario usuario) {
        String sql = "SELECT * FROM Consultas WHERE idUsuario = "+usuario.getId()+";";
        SQLiteDatabase db = dao.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Consulta> consultas = new ArrayList<Consulta>();
        while (c.moveToNext()){
            Consulta consulta = new Consulta();
            consulta.setId(c.getLong(c.getColumnIndex("id")));
            consulta.setCodigoConsulta(c.getInt(c.getColumnIndex("codigoConsulta")));
            consulta.setPaciente(c.getString(c.getColumnIndex("paciente")));
            consulta.setProcedimento(c.getString(c.getColumnIndex("procedimento")));
            consulta.setData(c.getString(c.getColumnIndex("data")));
            consulta.setUnidadeSolicitante(c.getString(c.getColumnIndex("unidadeSolicitante")));
            consulta.setLocal(c.getString(c.getColumnIndex("local")));
            consulta.setSituacao(c.getInt(c.getColumnIndex("situacao")));

            consultas.add(consulta);
        }
        c.close();
        return consultas;
    }

    public void apagar(Consulta consulta) {
        String [] params = {consulta.getId().toString()};
        database.delete("Consultas", "id = ?;", params);
    }

    public void atualizar(Consulta consulta) {
        ContentValues dados = pegarDadosConsulta(consulta);

        String[] params = {consulta.getId().toString()};
        database.update("Consultas", dados, "id = ?", params);
    }
}