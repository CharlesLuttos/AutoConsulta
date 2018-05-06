package com.android.luttos.autoconsulta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.android.luttos.autoconsulta.model.Consulta;

import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO extends SQLiteOpenHelper {

    public ConsultaDAO(Context context) {
        super(context, "bd_autoconsulta", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Consultas (id INTEGER PRIMARY KEY, codigoConsulta INTEGER NOT NULL, paciente TEXT NOT NULL, procedimento TEXT NOT NULL, data TEXT NOT NULL, unidadeSolicitante TEXT NOT NULL, local TEXT NOT NULL, situacao TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Consultas;";
        db.execSQL(sql);
        onCreate(db);
    }

    public void inserir(Consulta consulta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegarDadosConsulta(consulta);
        db.insert("Consultas", null, dados);
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
        return dados;
    }

    public List<Consulta> listar() {
        String sql = "SELECT * FROM Consultas;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<Consulta> consultas = new ArrayList<Consulta>();
        while (c.moveToNext()){
            Consulta consulta = new Consulta();
            consulta.setId(c.getLong(c.getColumnIndex("id")));
            consulta.setCodigoConsulta(c.getInt(c.getColumnIndex("codigoConsulta")));
            consulta.setPaciente(c.getString(c.getColumnIndex("paciente")));
            consulta.setProcedimento(c.getString(c.getColumnIndex("procedimento")));
            consulta.setData(c.getString(c.getColumnIndex("data")));
            consulta.setUnidadeSolicitante(c.getString(c.getColumnIndex("unidadeSolicitante")));
            consulta.setLocal(c.getString(c.getColumnIndex("local")));
            consulta.setSituacao(c.getString(c.getColumnIndex("situacao")));

            consultas.add(consulta);
        }
        c.close();
        return consultas;
    }

    public void apagar(Consulta consulta) {
        SQLiteDatabase db = getWritableDatabase();
        String [] params = {consulta.getId().toString()};
        db.delete("Consultas", "id = ?;", params);
    }

    public void atualizar(Consulta consulta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegarDadosConsulta(consulta);

        String[] params = {consulta.getId().toString()};
        db.update("Consultas", dados, "id = ?", params);
    }
}