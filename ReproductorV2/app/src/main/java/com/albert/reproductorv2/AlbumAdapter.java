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

public class  AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {

    private Context mContext;
    private ArrayList<ArchivosMusica> album_Archivos;
    View view;
    public AlbumAdapter(Context mContext, ArrayList<ArchivosMusica> album_Archivos) {
        this.mContext = mContext;
        this.album_Archivos = album_Archivos;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(mContext).inflate(R.layout.album_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_nombre.setText(album_Archivos.get(position).getAlbum());
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
                Intent intent = new Intent(mContext,AlbumDetallesActivity.class);
                intent.putExtra("albumnombre",album_Archivos.get(position).getAlbum());
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
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_img = itemView.findViewById(R.id.album_img);
            album_nombre = itemView.findViewById(R.id.album_nombre);
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
