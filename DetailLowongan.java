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

    private TextView txtdeskusaha, txtlokasi, txtdesklow, txtwaktu, txtgaji, txtsyarat;
    private FirebaseFirestore db;
    private String email, lowongan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lowongan);

        //Inisialisasi material desain
        db = FirebaseFirestore.getInstance(); //Menghubungkan dengan cloud firestore
        txtdeskusaha = findViewById(R.id.txtDetLow_DeskUsaha);
        txtlokasi = findViewById(R.id.txtDetLow_AlamatUsaha);
        txtdesklow = findViewById(R.id.txtDetLow_LowDetLow);
        txtwaktu = findViewById(R.id.txtDetLow_WaktuDetLow);
        txtgaji = findViewById(R.id.txtDetLow_Gaji);
        txtsyarat = findViewById(R.id.txtDetLow_Syarat);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("HiJobs",0);
        email = sharedPreferences.getString("Email",null);
        lowongan = sharedPreferences.getString("NamaLow",null);

        //Query
        CollectionReference query = db.collection("Lowongan");
        query.document(email+"_"+lowongan).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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