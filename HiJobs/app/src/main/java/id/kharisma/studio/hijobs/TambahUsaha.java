package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.kharisma.studio.hijobs.ui.main.BerandaFragment;
import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class TambahUsaha extends AppCompatActivity {

    private EditText etNama, etDeskripsi, etLokasi, etLinkMaps;
    private Button btnTambah;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String email;
    private static final String TAG = "Tambah Usaha";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_tambah_usaha);

        firebaseAuth = FirebaseAuth.getInstance(); //Menghubungkan dengan firebase authentifikasi
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengar cloud firestore

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtTbhUsh_Usaha);
        etDeskripsi = findViewById(R.id.txtTbhUsh_DeskUsaha);
        etLokasi = findViewById(R.id.txtTbhUsh_AlamatUsaha);
        etLinkMaps = findViewById(R.id.txtTbhUsh_LinkMaps);
        btnTambah = findViewById(R.id.btnTbhUsh_Tambah);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);

        //Button Tambah
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inisialisasi data ke dalam variabel
                String Nama = etNama.getText().toString();
                String Desk = etDeskripsi.getText().toString();
                String Lokasi = etLokasi.getText().toString();
                String Link_Maps = etLinkMaps.getText().toString();

                if (cek_Thb_Ush(Nama,Desk,Lokasi,Link_Maps) == true) {
                    setData(Nama,Desk,Lokasi,Link_Maps,email);
                    finish();
                }
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman usaha
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

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Thb_Ush(String Nama,String Desk,String Lokasi,String Link_Maps) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Link_Maps.isEmpty()) {
            etLinkMaps.setError("Link google maps required");
            etLinkMaps.requestFocus();
        }
        if (Lokasi.isEmpty()) {
            etLokasi.setError("Address required");
            etLokasi.requestFocus();
        }
        if (Desk.isEmpty()) {
            etDeskripsi.setError("Deskripsi required");
            etDeskripsi.requestFocus();
        }
        if (Nama.isEmpty()) {
            etNama.setError("Name required");
            etNama.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Nama.equals("") || Desk.equals("") || Lokasi.equals("") || Link_Maps.equals("")) {
            //Peringatan data tidak lengkap
            Toast.makeText(TambahUsaha.this,
                    "Data anda belum lengkap", Toast.LENGTH_LONG).show();
            return false; //Pengisian tidak sesuai ketentuan
        } else {
            return true;
        }
    }

    //Memasukkan data ke database
    public void setData(String Nama,String Desk,String Lokasi,String Link_Map,String email) {

        //Membuat kolom user
        Map<String, Object> usaha = new HashMap<>();
        usaha.put("Nama", Nama);
        usaha.put("Deskripsi", Desk);
        usaha.put("Lokasi", Lokasi);
        usaha.put("Link_Map", Link_Map);
        usaha.put("Owner", email);

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Usaha").document(email+"_"+Nama)
                .set(usaha).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        etNama.setText("");
                        etDeskripsi.setText("");
                        etLokasi.setText("");
                        etLinkMaps.setText("");
                        Snackbar.make(findViewById(R.id.btnTbhUsh_Tambah),
                                "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.btnTbhUsh_Tambah),
                                "Data gagal ditambahkan", Snackbar.LENGTH_LONG).show();
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}