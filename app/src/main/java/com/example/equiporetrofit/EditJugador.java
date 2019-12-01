package com.example.equiporetrofit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.View.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static com.example.equiporetrofit.MainActivity.URL;

public class EditJugador extends AppCompatActivity {
    private static final int PHOTO_SELECTED = 1;
    private EditText etNom,etApelli;
    private ImageView ivFoto;
    private Jugador jugadorActual;
    private Button btEditJ;
    private MainViewModel viewModel;
    private Uri imageUri;
    private Boolean newPhoto=false;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jugador);
        jugadorActual=getIntent().getParcelableExtra("jugador");
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        init();
    }

    private void init() {
        etNom=findViewById(R.id.etNombreEditJ);
        etApelli=findViewById(R.id.etApelliEditJ);
        ivFoto=findViewById(R.id.ivFotoEdit);
        btEditJ=findViewById(R.id.btEditJ);

        etNom.setText(jugadorActual.getNombre());
        etApelli.setText(jugadorActual.getApellidos());
        ivFoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                selectImage();
                newPhoto=true;
            }
        });
        btEditJ.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //Editar ViewModel
                if(!etNom.getText().toString().equals("")&&!etApelli.getText().toString().equals("")){
                    jugadorActual.setNombre(etNom.getText().toString());
                    jugadorActual.setApellidos(etApelli.getText().toString());
                    //Guardar imagen en servidor
                    if (newPhoto){
                        saveSelectedImageInFile(imageUri);
                        jugadorActual.setFoto("/web/equipo/public/img/img"+nombre);
                    }
                    //actualizar jugador PUT
                    viewModel.updateJugador(jugadorActual);
                    finish();
                }else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(view, "No puede haber campos vacios", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        Glide.with(this)
                .load(Uri.parse("http://"+URL+jugadorActual.getFoto()))
                .override(300, 300)// prueba de escalado
                .into(ivFoto);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        String[] types = {"image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        startActivityForResult(intent, PHOTO_SELECTED);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SELECTED && resultCode == Activity.RESULT_OK && null != data) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .override(300, 300)// prueba de escalado
                    .into(ivFoto);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveSelectedImageInFile(Uri uri) {
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT < 28) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                bitmap = null;
            }
        } else {
            try {
                final InputStream in = this.getContentResolver().openInputStream(uri);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                //ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                //bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                bitmap = null;
            }
        }
        if(bitmap != null) {
            File file = saveBitmapInFile(bitmap);
            if(file != null) {
               viewModel.uploadPhoto(file);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private File saveBitmapInFile(Bitmap bitmap) {
        nombre="img"+ LocalDateTime.now()+"Jugador.jpg";
        File file = new File(getFilesDir(), "img"+jugadorActual.getId()+".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            file = null;
        }
        return file;
    }
}
