package id.kharisma.studio.hijobs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class RiwayatLamaran extends AppCompatActivity implements FirestoreAdapterRiwayat.OnListItemClick {

    private String email;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreAdapterRiwayat adapter;
    private TextView txtLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_riwayat_lamaran);

        //Inisialisasi material desain
        recyclerView = findViewById(R.id.rvRiwayatLamaran_item);
        txtLabel = findViewById(R.id.textView33);
        email = getIntent().getStringExtra("Email");
        db = FirebaseFirestore.getInstance();

        //Query
        Query query = db.collection("RiwayatLamaran").whereEqualTo("Email",email);

        //RecyclerOptions
        FirestoreRecyclerOptions<ItemRiwayat> options = new FirestoreRecyclerOptions.Builder<ItemRiwayat>()
                .setQuery(query, ItemRiwayat.class)
                .build();

        adapter = new FirestoreAdapterRiwayat(options,this);

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

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman profil
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
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
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemClick(View view) {
        TextView lowongan = (TextView) view.findViewById(R.id.txtItemRiwayat_NamaLow);
        String namalow = lowongan.getText().toString();

        Intent intent = new Intent(RiwayatLamaran.this,DetailRiwayat.class);
        intent.putExtra("Id_Riw",view.findViewById(R.id.txtItemRiwayat_NamaLow)
                .getTag(R.string.db_id).toString());
        startActivity(intent);
    }
}