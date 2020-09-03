package com.ventulabs.admining;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ventulabs.admining.Models.Usuario;

public class MainActivity extends AppCompatActivity {

    private Context context = MainActivity.this;

    private FirebaseAuth      mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference usuarioRef;
    private Usuario           usuario;

    private TextView    txtUsuario;
    private TextView    txtEmail;
    private FrameLayout frameAnuncio;
    private AdView      adView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();
        inicializarAutenticacion();
        inicializarDatabase();
        inicializarAdmob();

        autenticacionFirebase();
    }

    private void inicializarAdmob() {
        MobileAds.initialize(context, "ca-app-pub-4893280618615792~1268015951");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView.loadAd(adRequest);

//        AdView adView = new AdView(context);
//
//        adView.setAdSize(AdSize.BANNER);
//
//        adView.setAdUnitId("ca-app-pub-4893280618615792/7640840854");

        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-4893280618615792/5749158776")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Assumes that your ad layout is in a file call ad_unified.xml
                        // in the res/layout folder
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                .inflate(R.layout.anuncio_bottom, null);
                        // This method sets the text, images and the native ad, etc into the ad
                        // view.
//                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameAnuncio.removeAllViews();
                        frameAnuncio.addView(adView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void obtencionDatosUsuario() {
        usuarioRef = mDatabase.child("usuarios").child(mAuth.getUid()).getRef();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                if (usuario == null) usuarioRef.setValue(new Usuario(mAuth.getUid()));
                updateUI(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpView() {
        txtUsuario   = findViewById(R.id.txtUsuario);
        txtEmail     = findViewById(R.id.txtEmail);
        frameAnuncio = findViewById(R.id.frameAnuncio);
        adView       = findViewById(R.id.adView);
    }

    private void autenticacionFirebase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            obtencionDatosUsuario();
        } else iniciarSesionAnonima();
    }

    private void iniciarSesionAnonima() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        obtencionDatosUsuario();
                    } else {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(Usuario usuario) {
        if (usuario == null) {
            txtUsuario.setText("Usuario: ?????");
        } else {
            txtUsuario.setText("Usuario: " + usuario.getIdUsuario());
            txtEmail.setText(usuario.getEmail());
        }
    }

    private void inicializarAutenticacion() {
        mAuth = FirebaseAuth.getInstance();
    }
}
