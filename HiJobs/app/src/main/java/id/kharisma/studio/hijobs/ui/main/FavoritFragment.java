package id.kharisma.studio.hijobs.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.kharisma.studio.hijobs.R;

public class FavoritFragment extends Fragment {

    public static FavoritFragment newInstance() {
        return new FavoritFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main3, container, false);
        final TextView textView = root.findViewById(R.id.textView11);
        return root;
    }

}