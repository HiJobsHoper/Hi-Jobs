package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailRiwayat extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText etNama, etDesk, etKat, etAlamat, etKota, etSyarat, etWaktu, etGaji;
    private Button btnBatal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat);

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtDetRiw_NamaLow);
        etDesk = findViewById(R.id.txtDetRiw_DeskLow);
        etKat = findViewById(R.id.txtDetRiw_KatLow);
        etAlamat = findViewById(R.id.txtDetRiw_AlamatLow);
        etKota = findViewById(R.id.txtDetRiw_KotaLow);
        etSyarat = findViewById(R.id.txtDetRiw_SyaratLow);
        etWaktu = findViewById(R.id.txtDetRiw_WaktuLow);
        etGaji = findViewById(R.id.txtDetRiw_GajiLow);
        btnBatal = findViewById(R.id.btnRiw_Batal);
        db = FirebaseFirestore.getInstance();

        String idRiw = getIntent().getStringExtra("Id_Riw");

        CollectionReference query = db.collection("RiwayatLamaran");
        query.document(idRiw).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                etNama.setText(snapshot.getString("Lowongan"));
                etDesk.setText(snapshot.getString("Deskripsi"));
                etKat.setText(snapshot.getString("Kategori"));
                etAlamat.setText(snapshot.getString("Alamat"));
                etKota.setText(snapshot.getString("Kota"));
                etSyarat.setText(snapshot.getString("Syarat"));
                etWaktu.setText(snapshot.getString("Waktu"));
                etGaji.setText(snapshot.getString("Gaji"));
            }
        });

        //Tombol batalkan lamaran
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailRiwayat.this);

                //Isi alert dialog
                alertDialogBuilder.setTitle("");
                alertDialogBuilder.setMessage("Yakin ingin membatalkan lamaran ?")
                        .setCancelable(false)
                        //Jika memilih ya
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("DaftarPelamar").document(idRiw).delete(); //Menghapus daftar pelamar dari database
                                db.collection("RiwayatLamaran").document(idRiw).delete(); //Menghapus riwayat lamaran dari database
                                onBackPressed(); //Kembali ke halaman riwayat lamaran
                            }
                            //Jika memilih tidak
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); //Membatalkan alert dialog
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create(); //Membuat alert dialog dari builder
                alertDialog.show(); //Menampilkan alert dialog
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
}