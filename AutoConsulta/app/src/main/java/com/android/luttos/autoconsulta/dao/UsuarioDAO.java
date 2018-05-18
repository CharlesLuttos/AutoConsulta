package com.android.luttos.autoconsulta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.android.luttos.autoconsulta.model.Usuario;

import java.io.Serializable;
import java.util.ArrayList;

public class UsuarioDAO implements Serializable{

    private SQLiteDatabase database;
    private DAO dao;
    private Context context;

    public UsuarioDAO(Context context) {
        this.context = context;
        dao = DAO.getHelper(context);
        open();
    }

    private void open() throws SQLException{
        if (dao == null)
            dao = DAO.getHelper(context);
        database = dao.getWritableDatabase();
    }

    public void inserir(Usuario usuario) {
        ContentValues dados = pegarDadosUsuario(usuario);
        database.insert("Usuarios", null, dados);
    }

    private ContentValues pegarDadosUsuario(Usuario usuario) {
        ContentValues dados = new ContentValues();
        dados.put("nome", usuario.getNome());
        return dados;
    }

    public ArrayList<Usuario> listar() {
        String sql = "SELECT * FROM Usuarios;";
        SQLiteDatabase db = dao.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Usuario> usuarios = new ArrayList<>();
        while (c.moveToNext()){
            Usuario usuario = new Usuario();
            usuario.setId(c.getInt(c.getColumnIndex("id")));
            usuario.setNome(c.getString(c.getColumnIndex("nome")));
            usuarios.add(usuario);
        }
        c.close();
        return usuarios;
    }

    public void apagar(Usuario usuario) {
        String [] params = { String.valueOf(usuario.getId()) };
        apagarConsultasRelacionadas(usuario);
        database.delete("Usuarios", "id = ?;", params);
    }

    private void apagarConsultasRelacionadas(Usuario usuario) {
        String [] params = { String.valueOf(usuario.getId()) };
        database.delete("Consultas", "idUsuario = ?;", params);
    }

    @SuppressWarnings("unused")
    public void atualizar(Usuario usuario) {
        ContentValues dados = pegarDadosUsuario(usuario);
        String[] params = { String.valueOf(usuario.getId()) };
        database.update("Usuarios", dados, "id = ?", params);
    }

}
