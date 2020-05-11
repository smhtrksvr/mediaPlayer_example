package com.example.muzik_sizsiniz;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.ProgressBar;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




public class MainActivity extends AppCompatActivity {

    public TextView songName,startTimeField,endTimeField;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private ImageButton playButton,pauseButton;
    public static int oneTimeOnly = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Controller elementlerimizi tanımlıyoruz
        songName = (TextView)findViewById(R.id.metinGoruntuleyici4);
        startTimeField = (TextView)findViewById(R.id.metinGoruntuleyici1) ;
        endTimeField = (TextView)findViewById(R.id.metinGoruntuleyici2);
        seekbar = (SeekBar)findViewById(R.id.yurutucuBar);
        playButton = (ImageButton)findViewById(R.id.imageButonu1);
        pauseButton = (ImageButton)findViewById(R.id.imageButonu2);
        songName.setText("qodes_deep_end.mp3");
        mediaPlayer = MediaPlayer.create(this, R.raw.qodes_deep_end);
        seekbar.setClickable(false);
        pauseButton.setEnabled(false);
    }

    //play butonuna basıldığında çalışması için olan method
    public void oynat(View view)
    {
        //Müziğin başladığına dair Toast ile uyarı verdiriyoruz!!!
        Toast.makeText(getApplicationContext(),"Müzik Çalıyor",Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0)
        {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        endTimeField.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) finalTime)))
        );
        startTimeField.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)))
        );
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);

    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime))));
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
    public void durdur(View view)
    {
        Toast.makeText(getApplicationContext(),"Müzik Durduruldu",Toast.LENGTH_SHORT).show();
        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    //ileri butonuna bastığımızda, müziğin çalış süresini 5 saniye arttırarak müziği ilerlettir
    public void ileri(View view)
    {
        double temp = (int)startTime;
        if((temp+forwardTime)<=finalTime)
        {
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int)startTime);
        }
        else
            {
                Toast.makeText(getApplicationContext(),"Son 5 saniyeyi ileri saramazsınız!",Toast.LENGTH_SHORT).show();
            }
    }
    //geri butonuna bastığımızda, müziğin çalış süresini 5 saniye azaltacak müziği geriletir
    public void geri(View view){
        double temp = (int)startTime;
        if((temp-backwardTime)>0)
        {
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else
            {
                Toast.makeText(getApplicationContext(),"İlk 5 saniyeyi geri saramazsınız!",Toast.LENGTH_SHORT).show();
            }
        }
    }

