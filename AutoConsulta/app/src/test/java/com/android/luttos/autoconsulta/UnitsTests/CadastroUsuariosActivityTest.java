package com.android.luttos.autoconsulta.UnitsTests;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.luttos.autoconsulta.CadastroUsuarioActivity;
import com.android.luttos.autoconsulta.R;
import com.android.luttos.autoconsulta.UsuarioActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class CadastroUsuariosActivityTest {

    @Test
    public void clickNovoUsuario_abrirCadastroUsuarioActivity() {
        UsuarioActivity activity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
        activity.findViewById(R.id.fab).performClick();
        Intent expectedIntent = new Intent(activity, CadastroUsuarioActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void clickNovoUsuarioVazio(){
        CadastroUsuarioActivity activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().visible().get();
        EditText txt = activity.findViewById(R.id.txtNomeUsuario);
        txt.setText("");
        activity.findViewById(R.id.btnCadastroUsuario).performClick();
        assertFalse(activity.isFinishing());
    }

    @Test
    public void testDefinirToolbarIcone(){
        CadastroUsuarioActivity activity = Robolectric.setupActivity(CadastroUsuarioActivity.class);
        boolean result = activity.definirToolbarIcon();
        assertTrue(result);
    }

    @Test
    public void testCreate() {
        CadastroUsuarioActivity activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().get();
        boolean result;
        result = activity != null;
        assertTrue(result);
    }

    @Test
    public void testInicializaBancoCadastroUsuario() {
        CadastroUsuarioActivity activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().get();
        assertTrue(activity.getUsuarioDAO() != null);
    }

    @Test
    public void testeInicializaComponentes() {
        CadastroUsuarioActivity activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().get();
        assertTrue(activity.getButtonCadastroUsuario() != null);
        assertTrue(activity.getTxtNomeUsuario() != null);
    }

    @Test
    public void criarMenuOpcoes() {
        CadastroUsuarioActivity activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().visible().get();
        activity.openOptionsMenu();
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        assertTrue(shadowActivity.getOptionsMenu() != null);
    }

    @Test
    public void selecionarItemMenuOpcoes() {
        CadastroUsuarioActivity activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().visible().get();
        MenuItem menuItem = new RoboMenuItem(R.id.action_settings);
        assertTrue(activity.onOptionsItemSelected(menuItem));
    }

}
