package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class Usaha extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usaha);

        //Inisialisasi material desain
        fab = findViewById(R.id.fabUsaha_Add);

        //Membuat floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Usaha.this, TambahUsaha.class));
            }
        });

        //Membuat tombol back pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman profil
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