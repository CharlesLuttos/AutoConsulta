package com.android.luttos.autoconsulta.UnitsTests;

import com.android.luttos.autoconsulta.CadastroConsultasActivity;
import com.android.luttos.autoconsulta.model.Consulta;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CadastroConsultasActivityTest {

    @Test
    public void testeCreate() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        assertTrue(activity!=null);
    }

    @Test
    public void testeCriarObjetoConsultaValido() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        JSONObject jsonObject = new JSONObject();
        Consulta consulta = new Consulta();
        try {
            jsonObject.accumulate("cod_consulta", 222757399);
            jsonObject.accumulate("paciente", "Ilza do nascimento");
            jsonObject.accumulate("procedimento", "Imunologia");
            jsonObject.accumulate("unidade_solicitante", "Unid. Solicitacao");
            jsonObject.accumulate("local_atendimento", "—");
            jsonObject.accumulate("situacao", 1);
            consulta = activity.formarObjetoConsulta(jsonObject, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(jsonObject.getString("cod_consulta"), consulta.getCodigoConsulta().toString());
            assertEquals(jsonObject.getString("paciente"), consulta.getPaciente());
            assertEquals(jsonObject.getString("procedimento"), consulta.getProcedimento());
            assertEquals(jsonObject.getString("unidade_solicitante"), consulta.getUnidadeSolicitante());
            assertEquals(jsonObject.getString("local_atendimento"), consulta.getLocal());
            assertEquals(jsonObject.getString("situacao"), consulta.getSituacao().toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testeCriarObjetoConsultaInvalido() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        JSONObject jsonObject = new JSONObject();
        Consulta consulta = new Consulta();
        try {
            jsonObject.accumulate("cod_consulta", 222757399);
            consulta = activity.formarObjetoConsulta(jsonObject, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(jsonObject.getString("cod_consulta"), consulta.getCodigoConsulta().toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

