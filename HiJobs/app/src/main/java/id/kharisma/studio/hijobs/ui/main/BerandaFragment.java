package id.kharisma.studio.hijobs.ui.main;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import id.kharisma.studio.hijobs.DaftarChat;
import id.kharisma.studio.hijobs.Pencarian;
import id.kharisma.studio.hijobs.R;

public class BerandaFragment extends Fragment {

    private MenuItem iChat;

    public static BerandaFragment newInstance() {
        return new BerandaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.textView16);
        return root;
    }
}