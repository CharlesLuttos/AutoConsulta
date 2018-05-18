package com.android.luttos.autoconsulta;

import android.widget.TextView;

import com.android.luttos.autoconsulta.model.Consulta;


public class DetalhesConsultasHelper {

    private final TextView campoCodigo;
    private final TextView campoPaciente;
    private final TextView campoProcedimento;
    private final TextView campoUnidadeAtendimento;
    private final TextView campoLocal;
    private final TextView campoData;
    private final TextView campoSituacao;

    private Consulta consulta;

    DetalhesConsultasHelper(DetalhesConsultasActivity activity){

        campoCodigo = activity.findViewById(R.id.form_codigo);
        campoPaciente = activity.findViewById(R.id.form_paciente);
        campoProcedimento = activity.findViewById(R.id.form_procedimento);
        campoUnidadeAtendimento = activity.findViewById(R.id.form_unidade_solicitante);
        campoLocal = activity.findViewById(R.id.form_local);
        campoData = activity.findViewById(R.id.form_data);
        campoSituacao = activity.findViewById(R.id.form_situacao);
        consulta = new Consulta();
    }

    public Consulta pegaConsulta() {
        consulta.setCodigoConsulta(Integer.valueOf(campoCodigo.getText().toString()));
        consulta.setPaciente(campoPaciente.getText().toString());
        consulta.setProcedimento(campoProcedimento.getText().toString());
        consulta.setUnidadeSolicitante(campoUnidadeAtendimento.getText().toString());
        consulta.setLocal(campoLocal.getText().toString());
        consulta.setData(campoData.getText().toString());
        consulta.setSituacao(Integer.valueOf(campoSituacao.getText().toString()));
        return consulta;
    }

    public void popularForm(Consulta consulta) {
        campoCodigo.setText(String.valueOf(consulta.getCodigoConsulta().intValue()));
        campoPaciente.setText(consulta.getPaciente());
        campoProcedimento.setText(consulta.getProcedimento());
        campoUnidadeAtendimento.setText(consulta.getUnidadeSolicitante());
        campoLocal.setText(consulta.getLocal());
        campoData.setText(consulta.getData());
        switch (consulta.getSituacao()){
            case 0:
                campoSituacao.setText(R.string.consulta_pendente);
                break;
            case 1:
                campoSituacao.setText(R.string.consulta_autorizada);
                break;
            case 2:
                campoSituacao.setText(R.string.consulta_expirada);
        }
        //campoSituacao.setText(String.valueOf(consulta.getSituacao().intValue()));
        this.consulta = consulta;
    }
}