package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Rekomendasi extends AppCompatActivity {

    private Button btnLewati, btnLanjut;
    private CardView cvBarang, cvJasa;
    private String Barang, Jasa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_rekomendasi);

        //Inisialisasi material desain
        btnLewati = findViewById(R.id.btnRekomendasi_Lewati);
        btnLanjut = findViewById(R.id.btnRekomendasi_Lanjut);
        cvBarang = findViewById(R.id.cvRekomendasi_Barang);
        cvJasa = findViewById(R.id.cvRekomendasi_Jasa);

        //Button lewati
        btnLewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rekomendasi.this, HalamanUtama.class)); //Membuka halaman utama
                finish(); //Menutup halaman rekomendasi
            }
        });

        //Button lanjut
        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Syntax...
                startActivity(new Intent(Rekomendasi.this, HalamanUtama.class)); //Membuka halaman utama
                finish(); //Menutup halaman rekomendasi
            }
        });

        //Card view barang
        cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Barang = "Barang";
            }
        });

        //Card view jasa
        cvJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jasa = "Jasa";
            }
        });
    }
}