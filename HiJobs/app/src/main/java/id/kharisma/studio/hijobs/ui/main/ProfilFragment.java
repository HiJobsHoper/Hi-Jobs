package id.kharisma.studio.hijobs.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import id.kharisma.studio.hijobs.Login;
import id.kharisma.studio.hijobs.R;
import id.kharisma.studio.hijobs.Registrasi;

public class ProfilFragment extends Fragment {

    //private EditText Nama, KP, US, RL, HK, Pengaturan, Keluar;
    //private ImageView Display_Picture;

    public static ProfilFragment newInstance() {
        return new ProfilFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main2, container, false);
        final TextView textView = root.findViewById(R.id.txtMain2_Nama);
        return root;
    }
}