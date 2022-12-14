package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        //Berpindah ke halaman login
        Thread thread = new Thread() {
            public void run() {
                try{
                    sleep(1000); //Lama waktu splash screen
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    startActivity(new Intent(MainActivity.this, Login.class)); //Membuka halaman login
                    finish(); //Menutup splash screen
                }
            }
        };
        thread.start();
    }
}