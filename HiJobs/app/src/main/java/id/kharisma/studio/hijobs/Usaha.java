package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class Usaha extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usaha);

        //Membuat Floating Action Button
        fab = findViewById(R.id.fabUsaha_Add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Usaha.this, "You have click on floating button", Toast.LENGTH_SHORT).show();
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman login
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(Usaha.this, TambahLowongan.class)); //Membuka halaman login
        finish(); //Menutup halaman tambah lowongan
        return true;
    }

    //Mengaktifkan tombol kembali
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}