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

import java.util.List;

public class ConsultaAdapter extends BaseAdapter{
    private final List<Consulta> consultas;
    private final Context context;

    public ConsultaAdapter(Context context, List<Consulta> consultas) {
        this.context = context;
        this.consultas = consultas;
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

        ViewHolder holder;
        if (convertView == null){
            Log.d("NGVL","View Nova => position: "+position);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_consulta, null);
            holder = new ViewHolder();
            holder.txtCodigo = convertView.findViewById(R.id.codigo_consulta);
            holder.imgSituacao = convertView.findViewById(R.id.image_status);
            convertView.setTag(holder);
        } else {
            Log.d("NGVL","View existente => position: "+position);
            holder = (ViewHolder) convertView.getTag();
        }
        // 0=Pendente;1=Autorizada;2=Expirada;
        Resources res = context.getResources();
        TypedArray situacoes = res.obtainTypedArray(R.array.situacoes);
        holder.imgSituacao.setImageDrawable(situacoes.getDrawable(consulta.getSituacao()));
        holder.txtCodigo.setText(String.valueOf(consulta.getCodigoConsulta()));

        return convertView;
    }

    static class ViewHolder{
        TextView txtCodigo;
        ImageView imgSituacao;
    }
}
