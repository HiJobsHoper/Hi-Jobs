package id.kharisma.studio.hijobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private TextView tvReg,tvPass;
    private Button btnMasuk;
    private Button btnGoogle;
    private EditText etEmail,etPass;
    private CheckBox chkPass;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;
    private String email, nama;
    private static final String TAG = "Google Sign In";
    private static final int RC_SIGN_IN = 0704;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        //Inisialisasi material desain
        tvReg = findViewById(R.id.textView9);
        tvPass = findViewById(R.id.textView18);
        btnMasuk = findViewById(R.id.btnLog_Masuk);
        etEmail = findViewById(R.id.txtLog_Email);
        etPass = findViewById(R.id.txtLog_KataSandi);
        chkPass = findViewById(R.id.chkLog_TampilSandi);
        btnGoogle = findViewById(R.id.btnLog_MasukGoogle);
        db = FirebaseFirestore.getInstance();

//        skipLogin(); //Langsung ke halaman utama

        //Membuka halaman registrasi
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registrasi.class)); //Membuka halaman registrasi
                finish(); //Menutup halaman login
            }
        });

        //Mereset kata sandi
        tvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showRecoverPasswordDialog();
                startActivity(new Intent(Login.this, LupaPassword.class)); //Membuka halaman lupa password
                finish(); //Menutup halaman login
            }
        });

        //Menampilkan/menyembunyikan kata sandi
        chkPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkPass.isChecked()) {
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Button masuk
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inisialisasi data ke dalam variabel
                String Email = etEmail.getText().toString().trim();
                String Pass = etPass.getText().toString().trim();

                if (cek_Log(Email,Pass) == true) {
                    loginAccound(Email,Pass); //Masuk menggunakan akun pada database
                }
            }
        });

        //Button sign in
        btnGoogle.setOnClickListener(v -> {
            signIn();
        });

        //Mengaktifkan Login Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("940426453472-fultj1n8qlj8v6dfrqemn9jf39l5713s.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Memastikan pengisian data sesuai ketentuan
    public boolean cek_Log(String Email,String Pass) {

        //Memberikan tanda dan mengarahkan pada data yang belum di isi
        if (Pass.isEmpty()) {
            etPass.setError("Password required");
            etPass.requestFocus();
        }
        if (Email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
        }

        //Mengecek apakah ada data yang belum di isi
        if (Email.isEmpty() || Pass.isEmpty()) {
            return false;
        } else {
            //Memastikan email berformat email
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                etEmail.setError("Email is invalid");
                Toast.makeText(Login.this,
                        "Email tidak sesuai", Toast.LENGTH_LONG).show();
                return false;
            }
            //Memastikan kata sandi tidak kurang dari 6 karakter
            if (Pass.length() < 6) {
                etPass.setError("Password is invalid");
                Toast.makeText(Login.this,
                        "Kata sandi kurang dari 6 karakter", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
    }

    //Masuk pada akun database
    public void loginAccound(String Email,String Pass) {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!= null) {
                    //Berhasil login
                    login();
                } else {
                    //Gagal login
                    Toast.makeText(Login.this,
                            "Email atau kata sandi tidak sesuai", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Login dengan Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Menambah akun pada database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned form launching the Intent form GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                //Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            Log.d(TAG,"signInWithCredential:success");
                            login();
                        } else {
                            //If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //Langsung masuk kalau sudah pernah login
//    public void skipLogin() {
//        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseauth.getCurrentUser();
//
//        if (currentUser != null) {
//            login();
//        }
//    }
//
    public void login() {
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        CollectionReference query = db.collection("Akun");
        query.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                nama = snapshot.getString("Nama");
//                setData(); //Menyiapkan data akun dan profil kosong
                Intent intent = new Intent(Login.this, HalamanUtama.class);
                intent.putExtra("Nama", nama);
                intent.putExtra("Email", email);
                startActivity(intent); //Membuka halaman utama
                finish(); //Menutup halaman login
            }
        });
    }

    public void setData() {
        Map<String, Object> akun = new HashMap<>();
        akun.put("Nama", "");
        akun.put("Email", "");
        akun.put("Nomor Televon", "");
        akun.put("Rekomendasi", "");

        db.collection("Akun").document(email)
                .set(akun)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        Map<String, Object> profil = new HashMap<>();
        profil.put("Nama", "");
        profil.put("Jenis Kelamin", "");
        profil.put("Tanggal Lahir", "");
        profil.put("Pendidikan Terakhir", "");
        profil.put("Alamat", "");
        profil.put("Keahlian", "");
        profil.put("Pengalaman Kerja", "");
        profil.put("Kewarganegaraan", "");

        //Menyimpan referensi data pada database berdasarkan user id
        db.collection("Profil").document(email)
                .set(profil)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}