package com.android.luttos.autoconsulta;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.luttos.autoconsulta.model.Consulta;
import com.android.luttos.autoconsulta.model.Usuario;

import java.util.List;

public class UsuarioAdapter extends BaseAdapter{
    private final List<Usuario> usuarios;
    private final Context context;

    public UsuarioAdapter(Context context, List<Usuario> usuarios) {
        this.context = context;
        this.usuarios = usuarios;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario usuario = usuarios.get(position);

        ViewHolder holder;
        if (convertView == null){
            Log.d("NGVL","View Nova => position: "+position);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_usuario, null);
            holder = new ViewHolder();
            holder.txtCodigo = convertView.findViewById(R.id.codigo_usuario);
            holder.txtNome = convertView.findViewById(R.id.nome_usuario);
            convertView.setTag(holder);
        } else {
            Log.d("NGVL","View existente => position: "+position);
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtCodigo.setText(String.valueOf(usuario.getId()));
        holder.txtNome.setText(usuario.getNome());

        return convertView;
    }

    static class ViewHolder{
        TextView txtCodigo;
        TextView txtNome;
    }
}
