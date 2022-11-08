package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import id.kharisma.studio.hijobs.ui.main.BerandaFragment;
import id.kharisma.studio.hijobs.ui.main.ProfilFragment;

public class TambahUsaha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_usaha);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();


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