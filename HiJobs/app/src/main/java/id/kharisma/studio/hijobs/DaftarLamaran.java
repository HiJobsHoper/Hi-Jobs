package id.kharisma.studio.hijobs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class DaftarLamaran extends AppCompatActivity implements FirestoreAdapterDaftar.OnListItemClick {

    private String nama_Low, email;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreAdapterDaftar adapter = null;
    private TextView txtLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_daftar_lamaran);

        //Inisialisasi material desain
        recyclerView = findViewById(R.id.rvDaftarLamawan_item);
        txtLabel = findViewById(R.id.textView32);

//        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
//        nama_Low = sharedPreferences.getString("Nama_Low",null);
        email = getIntent().getStringExtra("Email");

        //Query
        ArrayList<String> idLowList = (ArrayList<String>) getIntent().getSerializableExtra("idLowList");
        for (int i = 0; i < idLowList.size(); i++) {
//            Log.d("Test",idLowList.get(i)+"");
        }
//        Log.d("Test",idLowList.get(0)+"");
        db = FirebaseFirestore.getInstance();

        try {
            Query query = db.collection("DaftarPelamar").whereIn("Id_Low", idLowList);

            FirestoreRecyclerOptions<ItemDaftar> options = new FirestoreRecyclerOptions.Builder<ItemDaftar>()
                    .setQuery(query, ItemDaftar.class)
                    .build();

            adapter = new FirestoreAdapterDaftar(options,this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.isEmpty()) {
                        txtLabel.setVisibility(View.VISIBLE);
                    } else {
                        txtLabel.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } catch(Exception e) {
            txtLabel.setVisibility(View.VISIBLE);
        }
                //.whereIn("Id_Low", test);
//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                try {
//                    value.getDocuments().forEach((v) -> {
//                        Log.d("Test",v.getString("Id_Low"));
//                    });
//                } catch (Exception e) {
//
//                }
//            }
//        });

        //RecyclerOptions


        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman lowongan saya
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    //Mengaktifkan tombol kembali
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onItemClick(View view) {
        Intent intent = new Intent(DaftarLamaran.this,DetailPelamar.class);
        intent.putExtra("Id_Low",view.findViewById(R.id.txtItemDaftarLamaran_NamaDaftarLamaran)
                .getTag(R.string.db_id).toString());
        startActivity(intent);
    }
}