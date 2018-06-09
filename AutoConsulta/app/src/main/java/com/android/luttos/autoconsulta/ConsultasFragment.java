package com.android.luttos.autoconsulta;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.luttos.autoconsulta.adapters.ConsultaAdapter;
import com.android.luttos.autoconsulta.dao.ConsultaDAO;
import com.android.luttos.autoconsulta.model.Consulta;
import com.android.luttos.autoconsulta.model.Usuario;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultasFragment extends Fragment {

    private static final String USUARIO = "usuario";

    private Usuario usuario;
    private ConsultaDAO consultaDAO;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton fab;

    public ConsultasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario Objeto de Usuario contendo id.
     * @return Nova instancia do fragmento ConsultasFragment.
     */
    public static ConsultasFragment newInstance(Usuario usuario) {
        ConsultasFragment fragment = new ConsultasFragment();
        Bundle args = new Bundle();
        args.putSerializable(USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_content_consulta, container, false);
        inicializaBanco();
        carregarLista(view);
        inicializaComponentes(view);
        return view;
    }



    /**
     * Inicializa banco de dados
     */
    public void inicializaBanco() {
        consultaDAO = new ConsultaDAO(getContext());
    }

    /**
     * Instância componentes
     * @param view view
     */
    public void inicializaComponentes(View view) {
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroIntent = new Intent(getContext(), CadastroConsultasActivity.class);
                telaCadastroIntent.putExtra("usuario", usuario);
                startActivity(telaCadastroIntent);
            }
        });
    }

    /**
     * Carrega lista com dados do banco
     * aplicando adapter
     * @param view view
     */
    public void carregarLista(View view) {
        ListView listView = view.findViewById(R.id.lista_consulta);
        listView.setEmptyView(view.findViewById(android.R.id.empty));
        ArrayList<Consulta> listaConsultas = consultaDAO.listar(usuario); // Necessario informar usuario para saber quais consultas listar
        ConsultaAdapter consultaAdapter = new ConsultaAdapter(getContext(), listaConsultas);
        listView.setAdapter(consultaAdapter);

    }

    /**
     * Exibe toast
     * @param mensagem mensagem a ser exibida
     */
    public void exibirToast(String mensagem) {
        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unused")
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
