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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Vector;

public class Lowongan extends AppCompatActivity implements FirestoreAdapterLowongan.OnListItemClick {

    private FloatingActionButton fab_Daf, fab_Tbh;
    private String email, idUsaha;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreAdapterLowongan adapter;
    private TextView txtLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_lowongan);

        //Inisialisasi material desain
        fab_Daf = findViewById(R.id.btnLow_Daftar);
        fab_Tbh =findViewById(R.id.btnLow_Tambah);
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
        recyclerView = findViewById(R.id.rvLowongan_item);
        txtLabel = findViewById(R.id.textView21);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);
        idUsaha = sharedPreferences.getString("Id_Usaha",null);

        //Query
        Query query = db.collection("Lowongan").whereEqualTo("Id_Usaha",idUsaha);

        //RecyclerOptions
        FirestoreRecyclerOptions<ItemLowongan> options = new FirestoreRecyclerOptions.Builder<ItemLowongan>()
                .setQuery(query, ItemLowongan.class)
                .build();

        adapter = new FirestoreAdapterLowongan(options,this);

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

        fab_Tbh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Lowongan.this, TambahLowongan.class));
            }
        });

        fab_Daf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rvCount = recyclerView.getChildCount();
                Log.d("Test",rvCount+"");
                ArrayList<String> idLowList = new ArrayList<>();
                for (int i = 0; i < rvCount; i++) {
                    TextView tv = recyclerView.getChildAt(i).findViewById(R.id.txtItemLowongan_NamaLowongan);
                    idLowList.add(tv.getTag(R.string.db_id).toString());
                }
                Intent intent = new Intent(Lowongan.this, DaftarLamaran.class);
                intent.putExtra("idLowList",idLowList);
                startActivity(intent);
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman...
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
        TextView usaha = (TextView) view.findViewById(R.id.txtItemLowongan_NamaLowongan);
        String namalow = usaha.getText().toString();

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NamaLow", namalow);
        editor.commit();

        startActivity(new Intent(Lowongan.this,DetailLowongan.class));
    }
}