package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailLowongan extends AppCompatActivity {

    private TextView txtNama, txtDesk, txtKategori, txtAlamat, txtKota, txtSyarat, txtWaktu, txtGaji;
    private FirebaseFirestore db;
    private String email, lowongan;
    private Button btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lowongan);

        //Inisialisasi material desain
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
        txtNama = findViewById(R.id.txtDetLow_LowKerja);
        txtDesk = findViewById(R.id.txtDetLow_DeskLow);
        txtKategori = findViewById(R.id.txtDetLow_KateKerja);
        txtAlamat = findViewById(R.id.txtDetLow_Alamat);
        txtKota = findViewById(R.id.txtDetLow_Kota);
        txtSyarat = findViewById(R.id.txtDetLow_Syarat);
        txtWaktu = findViewById(R.id.txtDetLow_WaktuKerja);
        txtGaji = findViewById(R.id.txtDetLow_Gaji);
        btnHapus = findViewById(R.id.btnDetLow_Hapus);

        email = getIntent().getStringExtra("Email");
        lowongan = getIntent().getStringExtra("Nama_Low");
        String idLow = email+"_"+lowongan; //variabel id lowongan pada database

        //Query
        CollectionReference query = db.collection("Lowongan");
        query.document(idLow).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                txtNama.setText(snapshot.getString("Nama"));
                txtDesk.setText(snapshot.getString("Deskripsi"));
                txtKategori.setText(snapshot.getString("Kategori"));
                txtAlamat.setText(snapshot.getString("Alamat"));
                txtKota.setText(snapshot.getString("Kota"));
                txtWaktu.setText(snapshot.getString("Waktu"));
                txtGaji.setText("Rp." + snapshot.getString("Gaji"));
                txtSyarat.setText(snapshot.getString("Syarat"));
            }
        });

        //Tombol hapus lowongan pekerjaan
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailLowongan.this);

                //Isi alert dialog
                alertDialogBuilder.setTitle("");
                alertDialogBuilder.setMessage("Yakin ingin menghapus lowongan ?")
                        .setCancelable(false)
                        //Jika memilih ya
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Lowongan").document(idLow).delete(); //Menghapus daftar lowongan dari database
                                onBackPressed(); //Kembali ke halaman lowongan saya
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