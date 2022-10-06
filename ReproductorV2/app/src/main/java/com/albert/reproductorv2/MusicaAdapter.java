package com.albert.reproductorv2;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicaAdapter extends RecyclerView.Adapter<MusicaAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ArchivosMusica> mArchivos;

    MusicaAdapter(Context mContext,ArrayList<ArchivosMusica> mArchivos)
    {
        this.mContext=mContext;
        this.mArchivos=mArchivos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.musica_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nombre.setText(mArchivos.get(position).getTitulo());
        holder.artista.setText(mArchivos.get(position).getArtista());

        byte[] imagen= getAlbumPortada(mArchivos.get(position).getPath());
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
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
        holder.menudot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.borrar:
                                Toast.makeText(mContext,"Eliminando cancioón",Toast.LENGTH_SHORT).show();
                                deleteFile(position,v);
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }

    private void deleteFile(int position,View v)
    {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Long.parseLong(mArchivos.get(position).getId()));
        File file = new File(mArchivos.get(position).getPath());
        boolean borrado=file.delete();
        if(borrado)
        {
            mContext.getContentResolver().delete(contentUri,null,null);
            mArchivos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mArchivos.size());
            Snackbar.make(v,"Canción Borrada ",Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Snackbar.make(v,"No se pudo borrar la canción: ",Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return mArchivos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nombre;
        TextView artista;
        ImageView album_img,menudot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_archivo_musica);
            artista = itemView.findViewById(R.id.Artista_archivo_musica);
            album_img=itemView.findViewById(R.id.musica_img);
            menudot=itemView.findViewById(R.id.menu_more);

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
