package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import id.kharisma.studio.hijobs.ui.main.FavoritFragment;

public class Pekerjaan extends AppCompatActivity {

    private FirebaseFirestore db;
    private String email, lowongan;
    private TextView txtdeskusaha, txtlokasi, txtdesklow, txtwaktu, txtgaji, txtsyarat;
    private Button btnlamar;
    private ImageView imgchat, imgfav;
    private String nama, jenis, tanggal, umur, pendidikan, alamat, keahlian, pengalaman, kewarganegaraan;
    private final String TAG = "Pekerjaan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_pekerjaan);

        //Inisialisasi material desain
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
        txtdeskusaha = findViewById(R.id.txtKerja_DeskUsaha);
        txtlokasi = findViewById(R.id.txtKerja_AlamatUsaha);
        txtdesklow = findViewById(R.id.txtKerja_LowKerja);
        txtwaktu = findViewById(R.id.txtKerja_WaktuKerja);
        txtgaji = findViewById(R.id.txtKerja_Gaji);
        txtsyarat = findViewById(R.id.txtKerja_Syarat);
        btnlamar = findViewById(R.id.btnKerja_Lamar);
        imgchat = findViewById(R.id.imgKerja_Chat);
        imgfav = findViewById(R.id.imgKerja_Favorite);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);
        lowongan = sharedPreferences.getString("NamaLow",null);
        String idLow = getIntent().getStringExtra("Id_Low");
        setTitle(lowongan); //Mengubah nama label pada activity

        //Query
        CollectionReference query = db.collection("Lowongan");
        query.document(idLow).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                txtdeskusaha.setText(snapshot.getString("Deskripsi_Usaha"));
                txtlokasi.setText(snapshot.getString("Lokasi_Usaha"));
                txtdesklow.setText(snapshot.getString("Deskripsi"));
                txtwaktu.setText(snapshot.getString("Waktu"));
                txtgaji.setText("Rp." + snapshot.getString("Gaji"));
                txtsyarat.setText(snapshot.getString("Syarat"));
            }
        });

        db.collection("Profil").document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        nama = snapshot.getString("Nama");
                        jenis = snapshot.getString("Jenis Kelamin");
                        tanggal = snapshot.getString("Tanggal Lahir");
                        umur = snapshot.getString("Umur");
                        pendidikan = snapshot.getString("Pendidikan Terakhir");
                        alamat = snapshot.getString("Alamat");
                        keahlian = snapshot.getString("Keahlian");
                        pengalaman = snapshot.getString("Pengalaman Kerja");
                        kewarganegaraan = snapshot.getString("Kewarganegaraan");

                        String idLow = getIntent().getStringExtra("Id_Low");

                        Map<String, String> pelamar = new HashMap<>();
                        pelamar.put("Nama", nama);
                        pelamar.put("Jenis Kelamin", jenis);
                        pelamar.put("Tanggal Lahir", tanggal);
                        pelamar.put("Umur", umur);
                        pelamar.put("Pendidikan Terakhir", pendidikan);
                        pelamar.put("Alamat", alamat);
                        pelamar.put("Keahlian", keahlian);
                        pelamar.put("Pengalaman Kerja", pengalaman);
                        pelamar.put("Kewarganegaraan",kewarganegaraan);
                        pelamar.put("Id_Low", idLow);

                        BtnLamar(pelamar);
                    }
                });

        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pekerjaan.this, Chat.class));
            }
        });

        imgfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void BtnLamar(Map<String, String> pelamar) {
        btnlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idLow = getIntent().getStringExtra("Id_Low");

                //Menyimpan referensi data pada database berdasarkan user id
                db.collection("DaftarPelamar").document(idLow)
                        .set(pelamar)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Pekerjaan.this, "Lamaran anda telah terkirim", Toast.LENGTH_SHORT).show();
                                //Log
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Pekerjaan.this, "Lamaran anda gagal dikirim", Toast.LENGTH_SHORT).show();
                                //Log
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });
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
}