package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HubungiKami extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubungi_kami);

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}