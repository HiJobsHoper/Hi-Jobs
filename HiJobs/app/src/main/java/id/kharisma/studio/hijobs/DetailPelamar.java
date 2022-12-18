package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailPelamar extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText etNama, etJenis, etTanggal, etUmur, etPendidikan, etAlamat, etKeahlian,
            etPengalaman, etKewarganegaraan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pelamar);

        //Inisialisasi material desain
        etNama = findViewById(R.id.txtDetPel_NamaLengkap);
        etJenis = findViewById(R.id.txtDetPel_JenisKelamin);
        etTanggal = findViewById(R.id.txtDetPel_TglLahir);
        etUmur = findViewById(R.id.txtDetPel_Umur);
        etPendidikan = findViewById(R.id.txtDetPel_Pendidikan);
        etAlamat = findViewById(R.id.txtDetPel_Alamat);
        etKeahlian = findViewById(R.id.txtDetPel_Keahlian);
        etPengalaman = findViewById(R.id.txtDetPel_PengalamanKerja);
        etKewarganegaraan = findViewById(R.id.txtDetPel_Kewarganegaraan);
        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        String idLow = sharedPreferences.getString("Id_Low",null);

        CollectionReference query = db.collection("DaftarPelamar");
        query.document(getIntent().getStringExtra("Id_Low")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                etNama.setText(snapshot.getString("Nama"));
                etJenis.setText(snapshot.getString("Jenis Kelamin"));
                etTanggal.setText(snapshot.getString("Tanggal Lahir"));
                etUmur.setText(snapshot.getString("Umur"));
                etPendidikan.setText(snapshot.getString("Pendidikan Terakhir"));
                etAlamat.setText(snapshot.getString("Alamat"));
                etKeahlian.setText(snapshot.getString("Keahlian"));
                etPengalaman.setText(snapshot.getString("Pengalaman Kerja"));
                etKewarganegaraan.setText(snapshot.getString("Kewarganegaraan"));
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