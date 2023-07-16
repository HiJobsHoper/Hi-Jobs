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

public class Pekerjaan extends AppCompatActivity {

    private FirebaseFirestore db;
    private String email, lowongan, telvon, idLow;
    private TextView txtDesk, txtAlamat, txtKota, txtSyarat, txtWaktu, txtGaji;
    private Button btnlamar;
    private ImageView imgchat, imgfav;
    private String nama, jenis, tanggal, umur, pendidikan, alamat, keahlian, pengalaman, kewarganegaraan = "";
    private final String TAG = "Pekerjaan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_pekerjaan);

        //Inisialisasi material desain
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
        txtDesk = findViewById(R.id.txtKerja_DeskLow);
        txtAlamat = findViewById(R.id.txtKerja_Alamat);
        txtKota = findViewById(R.id.txtKerja_Kota);
        txtSyarat = findViewById(R.id.txtKerja_Syarat);
        txtWaktu = findViewById(R.id.txtKerja_WaktuDetLow);
        txtGaji = findViewById(R.id.txtKerja_Gaji);
        btnlamar = findViewById(R.id.btnKerja_Lamar);
        imgchat = findViewById(R.id.imgKerja_Chat);
        imgfav = findViewById(R.id.imgKerja_Favorite);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);
        idLow = getIntent().getStringExtra("Id_Low");
        lowongan = getIntent().getStringExtra("Nama_Low");
        setTitle(lowongan); //Mengubah nama label pada activity

        //Query
        CollectionReference query = db.collection("Lowongan");
        query.document(idLow).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                txtDesk.setText(snapshot.getString("Deskripsi"));
                txtAlamat.setText(snapshot.getString("Alamat"));
                txtKota.setText(snapshot.getString("Kota"));
                txtSyarat.setText(snapshot.getString("Syarat"));
                txtWaktu.setText(snapshot.getString("Waktu"));
                txtGaji.setText("Rp." + snapshot.getString("Gaji"));
            }
        });

        //Mengisi database Daftar Pelamar
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

                        db.collection("Akun").document(email)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        telvon = snapshot.getString("Nomor Televon");

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
                                        pelamar.put("Nomor Televon", telvon);
                                        pelamar.put("Id_Low", idLow);

                                        BtnLamar(pelamar);
                                    }
                                });
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
                Toast.makeText(Pekerjaan.this, "Fitur ini masih belum tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Mengisi database Riwayar Lamaran
    public void BtnLamar1() {
        //Mengisi database Daftar Pelamar
        CollectionReference query = db.collection("Lowongan");
        query.document(idLow).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                String alamat = snapshot.getString("Alamat");
                String desk = snapshot.getString("Deskripsi");
                String gaji = snapshot.getString("Gaji");
                String kategori = snapshot.getString("Kategori");
                String kota = snapshot.getString("Kota");
                String link = snapshot.getString("Link_Map");
                String syarat = snapshot.getString("Syarat");
                String waktu = snapshot.getString("Waktu");

                Map<String, String> riwayat = new HashMap<>();
                riwayat.put("Email", email);
                riwayat.put("Id_Low", idLow);
                riwayat.put("Lowongan", lowongan);
                riwayat.put("Alamat", alamat);
                riwayat.put("Deskripsi",desk);
                riwayat.put("Gaji",gaji);
                riwayat.put("Kategori",kategori);
                riwayat.put("Kota", kota);
                riwayat.put("Link_Map",link);
                riwayat.put("Syarat",syarat);
                riwayat.put("Waktu",waktu);

                db.collection("RiwayatLamaran").document(idLow+"_"+email)
                        .set(riwayat).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Log
                                Log.d(TAG, "Riwayat Lamaran successfully written!");
                            }
                        });
            }
        });

    }

    public void BtnLamar(Map<String, String> pelamar) {
        btnlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pekerjaan.this);

                alertDialogBuilder.setTitle("");
                alertDialogBuilder.setMessage("Apakah anda yakin ingin melamar pekerjaan ini?")
                        .setCancelable(false)
                        .setPositiveButton("Lamar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!nama.equals("") && !jenis.equals("") && !tanggal.equals("") &&
                                        !umur.equals("") && !pendidikan.equals("") &&
                                        !alamat.equals("") && !kewarganegaraan.equals("")) {
                                    //Menyimpan referensi data pada database berdasarkan user id
                                    db.collection("DaftarPelamar").document(idLow+"_"+email)
                                            .set(pelamar)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(Pekerjaan.this);

                                                    BtnLamar1(); //Menambahkan Riwayat Lamaran pada database
                                                    alertDialogBuilder1.setTitle("");
                                                    alertDialogBuilder1.setMessage("Lamaran anda telah terkirim!")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });
                                                    AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                                    alertDialog1.show();
                                                    //Log
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(Pekerjaan.this);

                                                    alertDialogBuilder2.setTitle("");
                                                    alertDialogBuilder2.setMessage("Lamaran anda gagal dikirim!")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });
                                                    AlertDialog alertDialog2 = alertDialogBuilder2.create();
                                                    alertDialog2.show();
                                                    //Log
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });
                                } else {
                                    AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(Pekerjaan.this);

                                    alertDialogBuilder3.setTitle("");
                                    alertDialogBuilder3.setMessage("Data diri anda belum lengkap, " +
                                                    "silahkan isi data diri profil anda terlebih dahulu.")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    AlertDialog alertDialog3 = alertDialogBuilder3.create();
                                    alertDialog3.show();
                                }
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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