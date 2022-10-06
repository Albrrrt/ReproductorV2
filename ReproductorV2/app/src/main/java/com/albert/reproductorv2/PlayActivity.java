package com.albert.reproductorv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.util.ArrayList;
import java.util.Random;

import static com.albert.reproductorv2.AlbumDetailAdapter.album_Archivos;
import static com.albert.reproductorv2.MainActivity.archivosMusicas;
import static com.albert.reproductorv2.MainActivity.repeatBoolean;
import static com.albert.reproductorv2.MainActivity.shuffleBoolean;

public class PlayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView titulo_cancion,nombre_artista,duracion_encurso,duracion_total;
    ImageView portada_album,btn_siguiente,btn_previo,btn_atras,btn_aleatorio,btn_repetir;
    FloatingActionButton btn_play;
    SeekBar seekBar;

    int position=-1;
    static ArrayList<ArchivosMusica> listaCanciones = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread,previoThread,siguienteThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initViews();
        getIntenMethod();
        titulo_cancion.setText(listaCanciones.get(position).getTitulo());
        nombre_artista.setText(listaCanciones.get(position).getArtista());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser)
                {
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null)
                {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duracion_encurso.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed( this,1000);
            }
        });

        btn_aleatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean)
                {
                    shuffleBoolean = false;
                    btn_aleatorio.setImageResource(R.drawable.ic_shuffle_off);
                }
                else
                {
                    shuffleBoolean = true;
                    btn_aleatorio.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });

        btn_repetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatBoolean)
                {
                    repeatBoolean = false;
                    btn_repetir.setImageResource(R.drawable.ic_repeat_off);
                }
                else
                {
                    repeatBoolean = true;
                    btn_repetir.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });



    }

    @Override
    protected void onResume() {
        Btn_playThread();
        Btn_siguienteThread();
        Btn_previoThread();
        super.onResume();
    }

    private void Btn_previoThread() {
        previoThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                btn_previo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_prevClicked();
                    }
                });

            }
        };
        previoThread.start();
    }

    private void btn_prevClicked()  {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listaCanciones.size() - 1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {
                position = ((position + 1)% listaCanciones.size());
            }
            uri = Uri.parse(listaCanciones.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            titulo_cancion.setText(listaCanciones.get(position).getTitulo());
            nombre_artista.setText(listaCanciones.get(position).getArtista());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed( this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_play.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listaCanciones.size() - 1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {
                position = ((position + 1)% listaCanciones.size());
            }
            uri = Uri.parse(listaCanciones.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            titulo_cancion.setText(listaCanciones.get(position).getTitulo());
            nombre_artista.setText(listaCanciones.get(position).getArtista());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed( this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_play.setImageResource(R.drawable.ic_play);
        }
    }

    private void Btn_siguienteThread() {
        siguienteThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                btn_siguiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Btn_nextClicked();
                    }
                });

            }
        };
        siguienteThread.start();
    }

    private void Btn_nextClicked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listaCanciones.size() - 1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {
                position = ((position + 1)% listaCanciones.size());
            }

            uri = Uri.parse(listaCanciones.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            titulo_cancion.setText(listaCanciones.get(position).getTitulo());
            nombre_artista.setText(listaCanciones.get(position).getArtista());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed( this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_play.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listaCanciones.size() -1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {
                position = ((position + 1)% listaCanciones.size());
            }
            uri = Uri.parse(listaCanciones.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            titulo_cancion.setText(listaCanciones.get(position).getTitulo());
            nombre_artista.setText(listaCanciones.get(position).getArtista());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed( this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            btn_play.setImageResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random =new Random();
        return random.nextInt(i+1);
    }

    private void Btn_playThread() {
        playThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                btn_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_playClicked();
                    }
                });

            }
        };
        playThread.start();
    }

    private void btn_playClicked()  {
        if(mediaPlayer.isPlaying())
        {
            btn_play.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed( this,1000);
                }
            });
        }
        else
        {
            btn_play.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed( this,1000);
                }
            });

        }
    }

    private String formattedTime(int mCurrentPosition) {

        String totalout= "";
        String totalnew= "";
        String segundos=String.valueOf(mCurrentPosition % 60);
        String minutos=String.valueOf(mCurrentPosition / 60);
        totalout=minutos + ":"+segundos;
        totalnew=minutos + ":"+"0"+segundos;
        if(segundos.length()==1)
        {
            return totalnew;
        }
        else {
             return totalout;
        }
    }

    private void getIntenMethod() {
        position = getIntent().getIntExtra("position",-1);
        String sender=getIntent().getStringExtra("sender");
        if(sender !=null && sender.equals("albumDetallesActivity"))
        {
            listaCanciones =album_Archivos;
        }
        else
        {
            listaCanciones=archivosMusicas;
        }

        if (listaCanciones != null) {
            btn_play.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listaCanciones.get(position).getPath());
        }
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metadata(uri);
    }

    private void initViews()  {
        titulo_cancion = findViewById(R.id.nombre_cancion);
        nombre_artista = findViewById(R.id.nombre_artista);
        duracion_encurso = findViewById(R.id.duracion_encurso);
        duracion_total = findViewById(R.id.duracion_total);

        btn_atras=findViewById(R.id.btn_atras);
        portada_album = findViewById(R.id.portada_album);
        btn_siguiente = findViewById(R.id.btn_siguiente);
        btn_previo = findViewById(R.id.btn_previo);
        btn_aleatorio = findViewById(R.id.btn_aleatorio);
        btn_repetir = findViewById(R.id.btn_repetir);

        btn_play = findViewById(R.id.btn_pausa);

        seekBar = findViewById(R.id.seek_bar);

    }

    private void metadata(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duracionTotal = Integer.parseInt(listaCanciones.get(position).getDuracion()) / 1000;
        duracion_total.setText(formattedTime(duracionTotal));
        byte[] portada=retriever.getEmbeddedPicture();
        Bitmap bitmap ;
        if(portada!=null)
        {
            bitmap= BitmapFactory.decodeByteArray(portada,0,portada.length);
            ImagenAnimtiontrans(this,portada_album,bitmap);
        }
        else
        {
            Glide.with(this).asBitmap().load(R.drawable.ic_portada).into(portada_album);
        }
    }

    public void ImagenAnimtiontrans(Context context, ImageView imageView, Bitmap bitmap)
    {
        Animation animationOut = AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
        Animation animationIn = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);

        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animationIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animationIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animationOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Btn_nextClicked();
        if(mediaPlayer != null)
        {
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}