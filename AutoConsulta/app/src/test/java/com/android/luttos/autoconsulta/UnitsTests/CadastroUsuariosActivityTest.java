package com.android.luttos.autoconsulta.UnitsTests;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.android.luttos.autoconsulta.CadastroUsuarioActivity;
import com.android.luttos.autoconsulta.R;
import com.android.luttos.autoconsulta.UsuarioActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class CadastroUsuariosActivityTest {

    @Test
    public void clickNovoUsuario_abrirCadastroUsuarioActivity(){

        UsuarioActivity activity = Robolectric.setupActivity(UsuarioActivity.class);
        activity.findViewById(R.id.fab).performClick();

        Intent expectedIntent = new Intent(activity, CadastroUsuarioActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void clickNovoUsuarioCampoUsuarioVazio_exibirToast(){
        CadastroUsuarioActivity activity = Robolectric.setupActivity(CadastroUsuarioActivity.class);
        EditText txt = activity.findViewById(R.id.txtNomeUsuario);
        txt.setText("");
        activity.findViewById(R.id.btnCadastroUsuario).performClick();

        Toast expectedToast = Toast.makeText(activity, "Campo vazio!", Toast.LENGTH_SHORT);
        Toast actualToast = (Toast) ShadowApplication.getInstance().getShownToasts();

        assertEquals(expectedToast.getDuration(), actualToast.getDuration());
    }

}

