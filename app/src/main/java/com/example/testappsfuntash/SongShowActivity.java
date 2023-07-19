package com.example.testappsfuntash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SongShowActivity extends AppCompatActivity {
    ImageView exit_screen,image_pic,backskipbutton,playbutton,forwardbutton;
    TextView starttime,endtime,title;
    SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    String receivedsongurl;
    private int progress = 0;
    private Handler handler= new Handler();

    private Runnable runnable;
    private boolean isRepeating = false;
    private Timer timer;
    private InterstitialAd mInterstitialAd;
    LinearLayout songshowContainer;
    SharedPreferences sharedPreferences;
    String banner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_show);
        init();
        MobileAds.initialize(this);
        songshowContainer =findViewById(R.id.songshowContainer);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        AdsSongShowdata();

        mediaPlayer = new MediaPlayer();
        seekBar.setMax(100);



        receivedsongurl = getIntent().getStringExtra("song_url");
        String receivedtitle = getIntent().getStringExtra("song");


        exit_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SongShowActivity.this, SongActivity.class);
                startActivity(i);
            }
        });


        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.setDataSource(receivedsongurl);
//            mediaPlayer.prepareAsync();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopSong();;
                   // playbutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);

                } else {
                    playSong();
                }
                isPlaying = !isPlaying;


                // isRepeating = !isRepeating;


//                Toast.makeText(SongShowActivity.this, "Audio playing", Toast.LENGTH_SHORT).show();
//                if (mediaPlayer.isPlaying()) {
//
//                    mediaPlayer.pause();
//                    playbutton.setImageResource(R.drawable.baseline_pause_circle_24);
//                    isplay = false;
//
//                } else
//                {
//
//                    mediaPlayer.start();
//                    playbutton.setImageResource(R.drawable.baseline_play_circle_24);
//                    isplay = true;
//                }
//
//                    try {
//                        mediaPlayer.setDataSource(receivedsongurl);
//
//                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mp) {
//                                //mp.start();
//                                isplay = true;
//                                int duration = mediaPlayer.getDuration(); // Length of the audio in milliseconds
//                                // Convert duration to a more readable format (e.g., minutes and seconds)
//                                int minutes = (duration / 1000) / 60;
//                                int seconds = (duration / 1000) % 60;
//
//                                String lengthString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                                endtime.setText(lengthString);
//                            }
//                        });
//                        mediaPlayer.prepareAsync();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
            }

        });
        title.setText(receivedtitle);
        Glide.with(getApplicationContext()).load(R.drawable.baseline_music_note_24).into(image_pic);
    }

    public void init(){
        title = findViewById(R.id.title);
        exit_screen = findViewById(R.id.exit_screen);
        image_pic = findViewById(R.id.image_pic);
        backskipbutton = findViewById(R.id.backskipbutton);
        playbutton = findViewById(R.id.playbutton);
        forwardbutton = findViewById(R.id.forwardbutton);
        starttime = findViewById(R.id.starttime);
        endtime = findViewById(R.id.endtime);
        seekBar = findViewById(R.id.seekBar);
    }
    private void playSong() {

        //String songUrl = "https://www.example.com/your_song.mp3"; // Replace with your actual mp3 URL
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            startTimer();
            playbutton.setBackgroundResource(R.drawable.baseline_pause_circle_24);

        }else
        // Update the play button icon to the pause icon


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressvalue, boolean fromUser) {

                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    starttime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                }
                updateTimeTextView(progress);
                //progress = progressvalue;
                //starttime.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseSong();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                // Resume the media player when the SeekBar dragging is completed
                playSong();
            }
        });
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(receivedsongurl);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isRepeating) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    } else {
                        isPlaying = false;
                        //playButton.setText("Play");
                    }
                }
            });



            // Set a listener for when the MediaPlayer is prepared
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                   isPlaying = true;
                   updateTimeTextView(mediaPlayer.getCurrentPosition());
                   // playbutton.setText("Pause");
                    seekBar.setMax(mediaPlayer.getDuration());
                    int duration = mediaPlayer.getDuration(); // Length of the audio in milliseconds

                    // Convert duration to a more readable format (e.g., minutes and seconds)
                    int minutes = (duration / 1000) / 60;
                    int seconds = (duration / 1000) % 60;

                    String lengthString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    endtime.setText(lengthString);



                    // Start a thread to update the seek bar progress
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isPlaying) {
                                try {
                                    Thread.sleep(1000); // Update the seek bar every second
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mediaPlayer != null) {
                                            int currentPosition = mediaPlayer.getCurrentPosition();
                                            seekBar.setProgress(currentPosition);
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseSong() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            stopTimer();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void updateTimeTextView(int progress) {
        int minutes = progress / 1000 / 60;
        int seconds = (progress / 1000) % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        starttime.setText(time);
    }
    private void startSeekBarUpdate() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    startSeekBarUpdate();
                    int progress = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(progress);
                    updateTimeTextView(progress);
                }
                handler.postDelayed(this, 1000); // Update every second
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int totalDuration = mediaPlayer.getDuration();
                    updateSeekBar(currentPosition, totalDuration);
                }
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateSeekBar(final int currentPosition, final int totalDuration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setMax(totalDuration);
                seekBar.setProgress(currentPosition);

                starttime.setText(formatTime(currentPosition));
                endtime.setText(formatTime(totalDuration));
            }
        });
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
    private void playAudio() {
        //String audioUrl = "https://example.com/audio.mp3"; // Replace with your audio URL
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(receivedsongurl);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                     //playbutton.setText("Pause");
                    updateSeekBar();
                    int duration = mediaPlayer.getDuration(); // Length of the audio in milliseconds
                                // Convert duration to a more readable format (e.g., minutes and seconds)
                               int minutes = (duration / 1000) / 60;
                             int seconds = (duration / 1000) % 60;

                                String lengthString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                              endtime.setText(lengthString);

                }
            });

        } catch (IOException e) {
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
        }
    }
    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
          //  playButton.setText("Play");
        }
    }
    private void updateSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    updateSeekBar();
                }
            }
        }, 1000); // Update every second
    }
    public void AdsSongShowdata(){
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        songshowContainer.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(SongShowActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitialAd = null;

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mInterstitialAd!= null)
                            mInterstitialAd.show(SongShowActivity.this);
                        else
                            Toast.makeText(SongShowActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
    private void playSongs() {
        String songUrl = receivedsongurl;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
           // playbutton.setBackgroundResource(R.drawable.baseline_pause_24_bl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        playbutton.setBackgroundResource(R.drawable.baseline_play_arrow_24);
    }

}
