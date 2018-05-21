package com.android.luttos.autoconsulta.UnitsTests;

import android.content.Intent;
import android.test.ActivityTestCase;

import com.android.luttos.autoconsulta.CadastroConsultasActivity;
import com.android.luttos.autoconsulta.ConsultasActivity;
import com.android.luttos.autoconsulta.R;
import com.android.luttos.autoconsulta.dao.DAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class ConsultasActivityTest extends ActivityTestCase{

    @Test
    public void clickNovaConsulta_abrirCadastroConsultaActivity(){

        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        activity.findViewById(R.id.fab).performClick();

        Intent expectedIntent = new Intent(activity, CadastroConsultasActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}

