package com.android.luttos.autoconsulta.model;

public class Consulta {
    private Long id;
    private Integer codigoConsulta;
    private String data;
    private String paciente;
    private String procedimento;
    private String unidadeSolicitante;
    private String local;
    private Integer situacao; // 0=Pendente;1=Autorizada;2=Expirada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(String procedimento) {
        this.procedimento = procedimento;
    }

    public String getUnidadeSolicitante() {
        return unidadeSolicitante;
    }

    public void setUnidadeSolicitante(String unidadeSolicitante) {
        this.unidadeSolicitante = unidadeSolicitante;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Integer getCodigoConsulta() {
        return codigoConsulta;
    }

    public void setCodigoConsulta(Integer codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }

    @Override
    public String toString() {
        return getId() + " - " + getCodigoConsulta() + " - " + getPaciente();
    }

    public Consulta() {

    }

    public Consulta(int codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }
}
