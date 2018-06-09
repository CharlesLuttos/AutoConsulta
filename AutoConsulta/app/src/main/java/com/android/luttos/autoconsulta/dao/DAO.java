package com.android.luttos.autoconsulta.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAO extends SQLiteOpenHelper{

    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static DAO dao;

    private DAO(Context context) {
        super(context, "bd_autoconsulta", null, 1);
        this.context = context;
    }

    public static synchronized DAO getHelper(Context context) {
        if (dao == null)
            dao = new DAO(context);
        return dao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCriaUsuarios = "CREATE TABLE Usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT);";

        db.execSQL(sqlCriaUsuarios);

        String sqlCriaConsultas = "CREATE TABLE Consultas (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "codigoConsulta INTEGER NOT NULL UNIQUE, " +
                "paciente TEXT, " +
                "procedimento TEXT, " +
                "data TEXT, " +
                "unidadeSolicitante TEXT, " +
                "local TEXT, " +
                "situacao INTEGER," +
                "idUsuario INTEGER REFERENCES Usuarios(id));";

        db.execSQL(sqlCriaConsultas);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDropConsultas = "DROP TABLE IF EXISTS Consultas;";
        db.execSQL(sqlDropConsultas);
        onCreate(db);

        String sqlDropUsuarios = "DROP TABLE IF EXISTS Usuarios;";
        db.execSQL(sqlDropUsuarios);
        onCreate(db);
    }
}
