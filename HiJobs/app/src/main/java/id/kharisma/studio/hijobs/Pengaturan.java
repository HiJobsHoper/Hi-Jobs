package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pengaturan extends AppCompatActivity {

    private EditText etSyarat, etHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        //Inisialisasi material desain
        etSyarat = findViewById(R.id.txtPengaturan_SK);
        etHapus = findViewById(R.id.txtPengaturan_HapusAkun);

        //Membuat tombol kembali pada Navigasi Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Edit text syarat dan ketentuan
        etSyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pengaturan.this, SyaratKetentuan.class)); //Membuka halaman syarat dan ketentuan
                finish(); //Menutup halaman pengaturan
            }
        });

        //Edit text hapus akun
        etHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

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

    //Peringatan sebelum menghapus akun
    public void showDialog() {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        AlertDialog.Builder alertDialogBuilder_1 = new AlertDialog.Builder(this);
        FirebaseUser user = firebaseauth.getCurrentUser();

        //Isi alert dialog
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Apakah anda yakin ingin menghapus akun ?")
            .setCancelable(false)
            //Jika memilih ya
            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Isi alert dialog setelah memilih ya
                    alertDialogBuilder_1.setTitle("");
                    alertDialogBuilder_1.setMessage("Akun anda telah berhasil dihapus !")
                        .setCancelable(false)
                        //Jika memilih ya
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (user != null) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(
                                                        Pengaturan.this, Login.class)); //Membuka halaman login
                                                finish(); //Menutup halaman pengaturan
                                            } else {
                                                Toast.makeText(Pengaturan.this,
                                                        "Terjadi kesalahan, silahkan coba lagi",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        //Jika memilih tidak
                        }).setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); //Membatalkan alert dialog
                            }
                        });
                    AlertDialog alertDialog_1 = alertDialogBuilder_1.create(); //Membuat alert dialog dari builder
                    alertDialog_1.show(); //Menampilkan alert dialog
                }
            //Jika memilih tidak
            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel(); //Membatalkan alert dialog
                }
            });
        AlertDialog alertDialog = alertDialogBuilder.create(); //Membuat alert dialog dari builder
        alertDialog.show(); //Menampilkan alert dialog
    }
}