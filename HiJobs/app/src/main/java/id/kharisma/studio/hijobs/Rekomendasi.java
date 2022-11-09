package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Rekomendasi extends AppCompatActivity {

    private Button btnLewati, btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        btnLewati = findViewById(R.id.btnRekomendasi_Lewati);
        btnLanjut = findViewById(R.id.btnRekomendasi_Lanjut);

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

                startActivity(new Intent(Rekomendasi.this, HalamanUtama.class)); //Membuka halaman utama
                finish(); //Menutup halaman rekomendasi
            }
        });
    }
}