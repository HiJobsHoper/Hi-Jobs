package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SearchView;

public class DaftarChat extends AppCompatActivity {

    private SearchView srchChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_chat);

        srchChat = findViewById(R.id.DaftarChat_Cari);
        srchChat.setQueryHint("Cari Kontak Toko & Pelamar");
        srchChat.setIconified(false);
        srchChat.clearFocus();

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Kembali ke halaman utama
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Mengaktifkan tombol chat
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}