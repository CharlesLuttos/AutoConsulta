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
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private ArrayList<Consulta> listaConsultas;
    private ConsultaAdapter consultaAdapter;
    private OnFragmentInteractionListener mListener;

    FloatingActionButton fab;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_content_consulta, container, false);
        inicializaBanco();
        inicializaComponentes(view);
        definirSwipeToPush();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(USUARIO);
        }
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
        swipeLayout = view.findViewById(R.id.swipe_container);
    }

    /**
     * Carrega lista com dados do banco
     * aplicando adapter
     * @param view view
     */
    public void carregarLista(View view) {
        listView = view.findViewById(R.id.lista_consulta);
        listView.setEmptyView(view.findViewById(android.R.id.empty));
        listaConsultas = consultaDAO.listar(usuario); // Necessario informar usuario para saber quais consultas listar
        consultaAdapter = new ConsultaAdapter(getContext(), listaConsultas);
        listView.setAdapter(consultaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Consulta consulta = (Consulta) listView.getItemAtPosition(position);
                Intent activityDetalhesConsulta = new Intent(getActivity(), DetalhesConsultasActivity.class);
                activityDetalhesConsulta.putExtra("consulta", consulta);
                startActivity(activityDetalhesConsulta);
            }
        });
        registerForContextMenu(listView);
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem buscarMapa = menu.add(R.string.menu_suspenso_maps);
        MenuItem criarAlerta = menu.add(R.string.menu_suspenso_alerta);
        MenuItem apagar = menu.add(R.string.menu_suspenso_apagar);
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                Consulta consulta = (Consulta) listView.getItemAtPosition(info.position);
                ConsultaDAO dao = new ConsultaDAO(getActivity());
                dao.apagar(consulta);
                carregarLista(getView());
                Toast.makeText(getActivity(), R.string.toast_excluir_consulta, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    /**
     * Evento para a açāo de deslizar o dedo para baixo na tela do Android
     */
    private void definirSwipeToPush() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Codigo funcional
                for (Consulta c : listaConsultas) {
                    getConsulta(c.getCodigoConsulta());
                }
                carregarLista(getView());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000);
                exibirToast(getString(R.string.toast_atualizado));
            }
        });
        swipeLayout.setColorSchemeColors( // Muda a cor da animacao (1 segundo para cada cor)
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }

    /**
     * Obtem uma consulta atualizada da API
     * @param codigo codigo da consulta
     */
    private void getConsulta(Integer codigo){
        AndroidNetworking.get("http:// 172.16.3.54:8080/autoconsulta/{codConsulta}")
                .addPathParameter("codConsulta", codigo.toString())
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Consulta.class, new ParsedRequestListener<Consulta>() {
                    @Override
                    public void onResponse(Consulta user) {
                        for(Consulta c : listaConsultas){
                            if(c.getSituacao().equals(user.getSituacao())){
                                c.setPaciente(user.getPaciente());
                                c.setUnidadeSolicitante(user.getUnidadeSolicitante());
                                c.setLocal(user.getLocal());
                                c.setProcedimento(user.getProcedimento());
                                consultaDAO.atualizar(c);
                            }
                        }
                        consultaAdapter = new ConsultaAdapter(getContext(), listaConsultas);
                        listView.setAdapter(consultaAdapter);
                        registerForContextMenu(listView);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Error: ", anError.getMessage());
                    }
                });
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
    public void onResume() {
        super.onResume();
        carregarLista(getView());
        definirSwipeToPush();
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
