package com.example.user.ling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.settings.ion.mylibrary.Settingion;

/**
 * Created by USER on 18.10.2016.
 */

public class DialogSettings extends DialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_settings, null);
        Settingion mSettingsEngine = (Settingion) v.findViewById(R.id.setting_panel);
        mSettingsEngine.setModelClass(Settings.class, getActivity());
        builder.setView(v);
        return builder.create();
    }

}
