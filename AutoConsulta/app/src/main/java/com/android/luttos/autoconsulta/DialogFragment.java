package com.android.luttos.autoconsulta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pesso on 09/06/2018.
 */

public class DialogFragment extends android.app.DialogFragment {
    private static final String TAG = "DialogFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.layout_cadastro_consulta, container, false);

        return view;
    }
}
