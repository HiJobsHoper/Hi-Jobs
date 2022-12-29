package id.kharisma.studio.hijobs.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import id.kharisma.studio.hijobs.FirestoreAdapterBeranda;
import id.kharisma.studio.hijobs.ItemBeranda;
import id.kharisma.studio.hijobs.Pekerjaan;
import id.kharisma.studio.hijobs.R;

public class BerandaFragment extends Fragment implements FirestoreAdapterBeranda.OnListItemClick{

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreAdapterBeranda adapter;
    private String namalow;

    public static BerandaFragment newInstance() {
        return new BerandaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        try {
            db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
            recyclerView = root.findViewById(R.id.rvBeranda_item);

            //Query
            Query query = db.collection("Lowongan").orderBy("Nama", Query.Direction.ASCENDING);
            //RecyclerOptions
            FirestoreRecyclerOptions<ItemBeranda> options = new FirestoreRecyclerOptions.Builder<ItemBeranda>()
                    .setQuery(query, ItemBeranda.class)
                    .build();

            adapter = new FirestoreAdapterBeranda(options,this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {

        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemClick(View view) {
        TextView low = (TextView) view.findViewById(R.id.txtItemBeranda_NamaLow);
        namalow = low.getText().toString();

        Intent intent = new Intent(getActivity(), Pekerjaan.class);
        intent.putExtra("Id_Low",low.getTag(R.string.db_id).toString());
        intent.putExtra("Nama_Low",namalow);
        startActivity(intent);
    }
}