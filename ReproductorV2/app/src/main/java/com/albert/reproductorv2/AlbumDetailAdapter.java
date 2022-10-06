package com.albert.reproductorv2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter .MyHolder> {

    private Context mContext;
    static ArrayList<ArchivosMusica> album_Archivos;
    View view;
    public AlbumDetailAdapter(Context mContext, ArrayList<ArchivosMusica> album_Archivos) {
        this.mContext = mContext;
        this.album_Archivos = album_Archivos;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(mContext).inflate(R.layout.musica_items,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_nombre.setText(album_Archivos.get(position).getTitulo());
        holder.album_artista.setText(album_Archivos.get(position).getArtista());
        byte[] imagen= getAlbumPortada(album_Archivos.get(position).getPath());
        if(imagen!= null)
        {
            Glide.with(mContext).asBitmap().load(imagen).into(holder.album_img);
        }
        else
        {
            Glide.with(mContext).load(R.drawable.ic_portada).into(holder.album_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PlayActivity.class);
                intent.putExtra("sender","albumDetallesActivity");
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return album_Archivos.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_img;
        TextView album_nombre;
        TextView album_artista;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            album_img = itemView.findViewById(R.id.musica_img);
            album_nombre = itemView.findViewById(R.id.nombre_archivo_musica);
            album_artista = itemView.findViewById(R.id.Artista_archivo_musica);
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
