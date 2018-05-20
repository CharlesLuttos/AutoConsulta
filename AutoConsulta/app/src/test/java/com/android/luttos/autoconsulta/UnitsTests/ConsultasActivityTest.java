package com.android.luttos.autoconsulta.UnitsTests;

import android.content.Intent;

import com.android.luttos.autoconsulta.CadastroConsultasActivity;
import com.android.luttos.autoconsulta.ConsultasActivity;
import com.android.luttos.autoconsulta.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ConsultasActivityTest {

    @Test
    public void clickNovaConsulta_abrirCadastroConsultaActivity(){

        ConsultasActivity activity = Robolectric.setupActivity(ConsultasActivity.class);
        activity.findViewById(R.id.fab).performClick();

        Intent expectedIntent = new Intent(activity, CadastroConsultasActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
