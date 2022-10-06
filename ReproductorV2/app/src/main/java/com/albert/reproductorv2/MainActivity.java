package com.albert.reproductorv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUESET_CODE=1;
    static ArrayList<ArchivosMusica> archivosMusicas;
    static boolean  shuffleBoolean = false,repeatBoolean=false;
    static ArrayList<ArchivosMusica> albums = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESET_CODE);
        }
        else
        {
            archivosMusicas = getAllAudio(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUESET_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                archivosMusicas = getAllAudio(this);
                initViewPager();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESET_CODE);
            }
        }
    }

    private void initViewPager() {
        ViewPager viewPager =findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapater viewPagerAdapater=new ViewPagerAdapater(getSupportFragmentManager());
        viewPagerAdapater.addFragments(new Canciones_Fragment(), "Canciones");
        viewPagerAdapater.addFragments(new Artista_Fragment(), "Artistas");
        viewPagerAdapater.addFragments(new Albums_Fragment(), "Albums");
        viewPagerAdapater.addFragments(new Listas_Fragment(), "Listas");
        viewPager.setAdapter(viewPagerAdapater);

        tabLayout.setupWithViewPager(viewPager);
    }

    public static class  ViewPagerAdapater extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String>   titulos;
        public ViewPagerAdapater(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titulos=new ArrayList<>();

        }

        void addFragments(Fragment fragment,String titulo)
        {
            fragments.add(fragment);
            titulos.add(titulo);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titulos.get(position);
        }
    }

    public static ArrayList<ArchivosMusica> getAllAudio(Context context)
    {
        ArrayList<String> duplicado = new ArrayList<>();
        ArrayList<ArchivosMusica> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor!= null)
        {
            while (cursor.moveToNext())
            {

                String album = cursor.getString(0);
                String titulo = cursor.getString(1);
                String duracion = cursor.getString(2);
                String path = cursor.getString(3);
                String artista = cursor.getString(4);
                String id = cursor.getString(5);

                ArchivosMusica archivosMusica = new ArchivosMusica(path,titulo,artista,album,duracion,id);
                tempAudioList.add(archivosMusica);
                if (!duplicado.contains(album))
                {
                    albums.add(archivosMusica);
                    duplicado.add(album);
                }

            }
            cursor.close();
        }
        return tempAudioList;
    }



}