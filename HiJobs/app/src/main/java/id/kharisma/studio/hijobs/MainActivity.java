package id.kharisma.studio.hijobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        //Berpindah ke halaman login
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1000); //Lama waktu splash screen
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    try{
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        db = FirebaseFirestore.getInstance();
                        CollectionReference query = db.collection("Akun");
                        query.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                String nama = snapshot.getString("Nama");
                                Intent intent = new Intent(MainActivity.this, HalamanUtama.class);
                                intent.putExtra("Nama", nama);
                                intent.putExtra("Email", email);
                                startActivity(intent); //Membuka halaman utama
                                finish(); //Menutup halaman login
                            }
                        });
                    }catch(Exception e){
                        startActivity(new Intent(MainActivity.this, Login.class)); //Membuka halaman login
                    }
                }
            }
        };
        thread.start();
    }
}