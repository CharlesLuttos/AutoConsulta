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
        return consultas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Consulta consulta = consultas.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_consulta, parent, false);
        }

        TextView procedimento = view.findViewById(R.id.procedimento_consulta);
        procedimento.setText(consulta.getProcedimento());

        TextView cod_consulta = view.findViewById(R.id.codigo_consulta);
        cod_consulta.setText(consulta.getCodigoConsulta());
        return view;
    }

}
