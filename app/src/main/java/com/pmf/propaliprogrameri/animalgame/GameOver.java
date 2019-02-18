package com.pmf.propaliprogrameri.animalgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameOver extends Activity {

    ImageButton restart;
    ImageButton menu;

    String playerShape;

    TextView score;
    TextView highscore;

    MediaPlayer mp,mp2;

    int getScore;

    protected void savePreferences(){
        //stvorimo shared preference
        int mode=MODE_PRIVATE;
        SharedPreferences mySharedPreferences=getSharedPreferences("com.pmf.propaliprogrameri.animalgame",mode);

        //editor za modificiranje shared preference
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putInt("highScore",getScore);


        //commit promjene
        editor.commit();
    }

    public int loadPreferences(){
        // dohvatimo preference
        int mode = MODE_PRIVATE;
        SharedPreferences mySharedPreferences = getSharedPreferences("com.pmf.propaliprogrameri.animalgame",mode);

        int highScore = mySharedPreferences.getInt("highScore", 0);

        return highScore;

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        mp2.seekTo(mp2.getCurrentPosition());
        mp2.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        updateUI();
        mp2.pause();
    }

    public void updateUI() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | decorView.SYSTEM_UI_FLAG_FULLSCREEN | decorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mp = MediaPlayer.create(this,R.raw.button);
        mp2 = MediaPlayer.create(this,R.raw.menumusic);
        mp2.setLooping(true);
        mp2.setVolume(0,0.8f);
        mp2.start();


        if(getIntent()!= null)
        {
            if(getIntent().getStringExtra("SESSION_ID")!=null )
            {
                playerShape=getIntent().getStringExtra("SESSION_ID");
            }
            if(getIntent().getIntExtra("SCORE",0) > -1)
            {
                getScore = getIntent().getIntExtra("SCORE",0);
            }
        }

        restart= findViewById(R.id.restart);
        score = findViewById(R.id.score);
        highscore = findViewById(R.id.highscore);
        menu = findViewById(R.id.menu);

        Drawable menuIcon = getResources().getDrawable(getResources().getIdentifier(playerShape,"drawable",getPackageName()));
        menu.setBackground(menuIcon);

        score.setText("Score:\n" + getScore);

        //mp = MediaPlayer.create(this, R.raw.gameover);
        //mp.start();

        if(getScore > loadPreferences()){
            savePreferences();
            highscore.setText("New High Score!\n");
        }

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                mp2.stop();

                Intent intent=new Intent(getApplicationContext(),AktivnostIgre.class);
                intent.putExtra("SESSION_ID",playerShape);
                startActivity(intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                mp2.stop();

                Intent intent=new Intent(getApplicationContext(),StartActivity.class);
                intent.putExtra("SESSION_ID",playerShape);
                intent.putExtra("MUZIKA",mp2.getCurrentPosition());
                startActivity(intent);
                finish();
            }
        });
    }
}
