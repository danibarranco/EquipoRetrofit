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

public class AddJugador extends AppCompatActivity {
    private static final int PHOTO_SELECTED = 1;
    private EditText etNom,etApelli;
    private ImageView ivFoto;
    private Jugador jugadorNuevo;
    private Button btAddJ;
    private MainViewModel viewModel;
    private Uri imageUri;
    private Boolean newPhoto=false;
    private String nombre;
    private long idEquipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jugador);
        jugadorNuevo=new Jugador();
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        idEquipo=getIntent().getLongExtra("idEquipo",0);
        init();
    }

    private void init() {
        etNom=findViewById(R.id.etNombreAddJ);
        etApelli=findViewById(R.id.etApelliAddJ);
        ivFoto=findViewById(R.id.ivFotoAddJ);
        btAddJ =findViewById(R.id.btAddJ);

        ivFoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                selectImage();
                newPhoto=true;
            }
        });
        btAddJ.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //Editar ViewModel
                if(!etNom.getText().toString().equals("")&&!etApelli.getText().toString().equals("")){
                    jugadorNuevo.setIdequipo(idEquipo);
                    jugadorNuevo.setNombre(etNom.getText().toString());
                    jugadorNuevo.setApellidos(etApelli.getText().toString());
                    //Guardar imagen en servidor
                    if (newPhoto){
                        saveSelectedImageInFile(imageUri);
                        jugadorNuevo.setFoto("/web/equipo/public/img/"+nombre);
                    }
                    //actualizar jugador PUT
                    viewModel.addJugador(jugadorNuevo);
                    finish();
                }else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(view, "No puede haber campos vacios", Snackbar.LENGTH_LONG).show();
                }
            }
        });
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
        File file = new File(getFilesDir(), nombre);
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
