package com.albert.reproductorv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import static com.albert.reproductorv2.MainActivity.archivosMusicas;

public class AlbumDetallesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView albumfoto;
    String album_nombre;
    ArrayList<ArchivosMusica> album_canciones= new ArrayList<>();
    AlbumDetailAdapter albumDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detalles);
        recyclerView = findViewById(R.id.recyclerview);
        albumfoto = findViewById(R.id.album_portada);
        album_nombre = getIntent().getStringExtra("albumnombre");
        int j = 0;
        for (int i=0; i< archivosMusicas.size();i++ )
        {
            if(album_nombre.equals(archivosMusicas.get(i).getAlbum()))
            {
                album_canciones.add(j,archivosMusicas.get(i));
                j++;
            }
        }
        byte[] imagen = getAlbumPortada(album_canciones.get(0).getPath());
        if(imagen != null)
        {
            Glide.with(this).load(imagen).into(albumfoto);
        }
        else
        {
            Glide.with(this).load(R.drawable.ic_portada).into(albumfoto);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(album_canciones.size()<1))
        {
            albumDetailAdapter = new AlbumDetailAdapter(this,album_canciones);
            recyclerView.setAdapter(albumDetailAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,recyclerView.VERTICAL,false));
        }
    }

    private byte[] getAlbumPortada(String uri)  {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] portada= retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return portada;
    }
}