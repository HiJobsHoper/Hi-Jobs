package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Rekomendasi extends AppCompatActivity {

    private Button btnLewati, btnPilih1, btnPilih2;
    private String nama, email, telvon;
    private FirebaseFirestore db;
    private final String TAG = "Rekomendasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_rekomendasi);

        //Inisialisasi material desain
        btnLewati = findViewById(R.id.btnRekomendasi_Lewati);
        btnPilih1 = findViewById(R.id.btnRekomendasi_Pilih1);
        btnPilih2 = findViewById(R.id.btnRekomendasi_Pilih2);
        db = FirebaseFirestore.getInstance();
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");
        telvon = getIntent().getStringExtra("telvon");

        //Button lewati
        btnLewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataAkun(nama,email,telvon,"");
                setDataProfil(email);
                Intent intent = new Intent(Rekomendasi.this, HalamanUtama.class); //Membuka halaman utama
                intent.putExtra("Nama", nama);
                intent.putExtra("Email", email);
                startActivity(intent); //Membuka halaman utama
                finish(); //Menutup halaman rekomendasi
            }
        });

        //Button barang
        btnPilih1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataAkun(nama,email,telvon,"Barang");
                setDataProfil(email);
                Intent intent = new Intent(Rekomendasi.this, HalamanUtama.class); //Membuka halaman utama
                intent.putExtra("Nama", nama);
                intent.putExtra("Email", email);
                startActivity(intent); //Membuka halaman utama
                finish(); //Menutup halaman rekomendasi
            }
        });

        //Button jasa
        btnPilih2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataAkun(nama,email,telvon,"Jasa");
                setDataProfil(email);
                Intent intent = new Intent(Rekomendasi.this, HalamanUtama.class); //Membuka halaman utama
                intent.putExtra("Nama", nama);
                intent.putExtra("Email", email);
                startActivity(intent); //Membuka halaman utama
                finish(); //Menutup halaman rekomendasi
            }
        });
    }

    public void setDataAkun(String nama,String email,String telvon,String kategori) {
        //Membuat kolom user
        Map<String, Object> akun = new HashMap<>();
        akun.put("Nama", nama);
        akun.put("Email", email);
        akun.put("Nomor Televon", telvon);
        akun.put("Rekomendasi", kategori);

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Akun").document(email)
                .set(akun)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    //Memasukkan data ke database
    public void setDataProfil(String email) {

        //Membuat kolom user
        Map<String, Object> profil = new HashMap<>();
        profil.put("Nama", "");
        profil.put("Jenis Kelamin", "");
        profil.put("Tanggal Lahir", "");
        profil.put("Umur","");
        profil.put("Pendidikan Terakhir", "");
        profil.put("Alamat", "");
        profil.put("Keahlian", "");
        profil.put("Pengalaman Kerja", "");
        profil.put("Kewarganegaraan", "");

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Profil").document(email)
                .set(profil)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}