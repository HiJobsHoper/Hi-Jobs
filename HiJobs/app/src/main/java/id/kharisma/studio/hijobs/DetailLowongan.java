package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailLowongan extends AppCompatActivity {

    private TextView txtNama, txtDesk, txtKategori, txtAlamat, txtKota, txtSyarat, txtWaktu, txtGaji;
    private FirebaseFirestore db;
    private String email, lowongan;

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

        email = getIntent().getStringExtra("Email");
        lowongan = getIntent().getStringExtra("Nama_Low");

        //Query
        CollectionReference query = db.collection("Lowongan");
        query.document(email+"_"+lowongan).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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