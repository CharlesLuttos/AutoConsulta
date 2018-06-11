package com.android.luttos.autoconsulta;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.luttos.autoconsulta.dao.UsuarioDAO;
import com.android.luttos.autoconsulta.model.Usuario;

/**
 * Created by pesso on 11/06/2018.
 */

public class CadastroUsuarioFragment extends CadastroConsultaFragment {

    private static final String TAG = "CadastroConsultaFragment";
    private UsuarioDAO usuarioDAO;
    private Usuario usuario;
    private EditText txtNomeUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.layout_cadastro_usuario, container, false);
        Button btnCadastroUser = (Button) view.findViewById(R.id.btnCadastroUsuario);
        txtNomeUsuario = (EditText) view.findViewById(R.id.txtNomeUsuario);
        setUsuarioDAO(new UsuarioDAO(getActivity()));


        btnCadastroUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsuario(new Usuario(txtNomeUsuario.getText().toString()));
                getUsuarioDAO().inserir(getUsuario());
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

}
