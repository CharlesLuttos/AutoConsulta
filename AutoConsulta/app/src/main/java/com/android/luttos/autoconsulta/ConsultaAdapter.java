package com.android.luttos.autoconsulta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.luttos.autoconsulta.model.Consulta;

import java.util.List;

public class ConsultaAdapter extends BaseAdapter{
    Context context;
    List<Consulta> consultas;


    public ConsultaAdapter(Context context, List<Consulta>consultas){
        this.consultas = consultas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return consultas.size();
    }

    @Override
    public Object getItem(int position) {
        return consultas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Consulta consulta = consultas.get(position);

        ViewHolder holder = null;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_consulta, null);
            holder = new ViewHolder();

            holder.image = (ImageView) convertView.findViewById(R.id.imageStatus);
            holder.txtTitulo = (TextView) convertView.findViewById(R.id.txtTitulo);
            holder.txtSubtitulo = (TextView) convertView.findViewById(R.id.txtSubTitulo);
            holder.btAtualizar = (Button) convertView.findViewById(R.id.btAtualizar);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.txtSubtitulo.setText(consulta.getId().toString());
        holder.txtTitulo.setText(consulta.getProcedimento());

        return convertView;
    }

    static class ViewHolder{
        ImageView image;
        TextView txtTitulo;
        TextView txtSubtitulo;
        Button btAtualizar;
    }
}
