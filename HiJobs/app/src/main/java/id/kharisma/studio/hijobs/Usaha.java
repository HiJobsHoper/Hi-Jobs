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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class Usaha extends AppCompatActivity implements FirestoreAdapterUsaha.OnListItemClick {

    private FloatingActionButton fab;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreAdapterUsaha adapter;
    private FirebaseAuth firebaseAuth;
    private TextView txtLabel;
    private String email, namausaha, desk, lokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_usaha);

        //Inisialisasi material desain
        fab = findViewById(R.id.fabUsaha_Add);
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
        recyclerView = findViewById(R.id.rvUsaha_item);
        firebaseAuth = FirebaseAuth.getInstance(); //Menghubungkan dengan firebase authentifikasi
        txtLabel = findViewById(R.id.textView20);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);

        //Query
        Query query = db.collection("Usaha").whereEqualTo("Owner",email);
        //RecyclerOptions
        FirestoreRecyclerOptions<ItemUsaha> options = new FirestoreRecyclerOptions.Builder<ItemUsaha>()
                .setQuery(query, ItemUsaha.class)
                .build();

        adapter = new FirestoreAdapterUsaha(options,this);

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

        //Membuat floating action button
        fab.bringToFront(); //Memindahkan floting button ke depan
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Usaha.this, TambahUsaha.class));
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
        TextView usaha = (TextView) view.findViewById(R.id.txtItemUsaha_NamaUsaha);
        namausaha = usaha.getText().toString();

        db.collection("Usaha").document(email+"_"+namausaha)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        desk = snapshot.getString("Deskripsi");
                        lokasi = snapshot.getString("Lokasi");

                        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Id_Usaha",email+"_"+namausaha);
                        editor.putString("Desk_Usaha",desk);
                        editor.putString("Lokasi_Usaha",lokasi);
                        editor.putString("Nama_Usaha",namausaha);
                        editor.commit();

                        startActivity(new Intent(Usaha.this, Lowongan.class)); //Membuka halaman lowongan
                    }
                });
    }
}