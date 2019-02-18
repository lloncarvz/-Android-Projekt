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
import android.widget.ImageView;
import android.widget.TextView;

public class StartActivity extends Activity {

    ImageButton  pig,dog,duck,elephant,gorilla,buffalo,chick,chicken,owl,snake,zebra,cow;

    String playerShape="pig";

    ImageView imageView;

    TextView highscore;

    MediaPlayer button, charSound, mp;

    protected void savePreferences(){
        //stvorimo shared preference
        SharedPreferences mySharedPreferences=getSharedPreferences("com.pmf.propaliprogrameri.animalgame", MODE_PRIVATE);

        //editor za modificiranje shared preference
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        if(!mySharedPreferences.contains("highScore"))
            editor.putInt("highScore", 0);

        //commit promjene
        editor.apply();
    }

    public int loadPreferences(){
        // dohvatimo preference
        SharedPreferences mySharedPreferences = getSharedPreferences("com.pmf.propaliprogrameri.animalgame", MODE_PRIVATE);

        return mySharedPreferences.getInt("highScore", 0);

    }

    public void newSound(int raw){
        try{
            charSound.release();
            charSound = MediaPlayer.create(getApplicationContext(), raw);
            charSound.start();
        }
        catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        mp.seekTo(mp.getCurrentPosition());
        mp.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        updateUI();
        mp.pause();
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
        setContentView(R.layout.activity_start);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mp = MediaPlayer.create(this,R.raw.menumusic);
        mp.setLooping(true);
        mp.setVolume(0,0.8f);
        if(getIntent()!= null) {
            if (getIntent().getIntExtra("MUZIKA",0) != -1) {
                mp.seekTo(getIntent().getIntExtra("MUZIKA",0));
            }
        }
        mp.start();

        imageView = findViewById(R.id.selected);
        highscore =  findViewById(R.id.highscore);
        savePreferences();
        String a = "High Score:" + Integer.toString(loadPreferences());
        highscore.setText(a);

        button = MediaPlayer.create(this,R.raw.tittle);
        button.start();
        button = MediaPlayer.create(this,R.raw.button);
        charSound = MediaPlayer.create(this,R.raw.duck);

        if(getIntent()!= null) {
            if (getIntent().getStringExtra("SESSION_ID") != null) {
                playerShape = getIntent().getStringExtra("SESSION_ID");
                Drawable menuIcon = getResources().getDrawable(getResources().getIdentifier(playerShape,"drawable",getPackageName()));
                imageView.setImageDrawable(menuIcon);
            }
        }


        pig = findViewById(R.id.pig);
        pig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="crocodile";
                imageView.setImageResource(R.drawable.pig);
                newSound(R.raw.pig);
            }
        });
        dog = findViewById(R.id.dog);
        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="dog";
                imageView.setImageResource(R.drawable.dog);
            }
        });
        cow = findViewById(R.id.cow);
        cow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="cow";
                imageView.setImageResource(R.drawable.cow);
            }
        });
        duck = findViewById(R.id.duck);
        duck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="duck";
                imageView.setImageResource(R.drawable.duck);
                newSound(R.raw.duck);
            }
        });
        elephant = findViewById(R.id.frog);
        elephant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="frog";
                imageView.setImageResource(R.drawable.frog);
                newSound(R.raw.frog);
            }
        });
        gorilla = findViewById(R.id.gorilla);
        gorilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="gorilla";
                imageView.setImageResource(R.drawable.gorilla);
                newSound(R.raw.gorilla);
            }
        });
        buffalo = findViewById(R.id.buffalo);
        buffalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="buffalo";
                imageView.setImageResource(R.drawable.buffalo);
                newSound(R.raw.hellicopter);
            }
        });
        chick = findViewById(R.id.chick);
        chick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="chick";
                imageView.setImageResource(R.drawable.chick);
            }
        });
        chicken = findViewById(R.id.chicken);
        chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="chicken";
                imageView.setImageResource(R.drawable.chicken);
                newSound(R.raw.chicken);
            }
        });

        owl = findViewById(R.id.owl);
        owl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="owl";
                imageView.setImageResource(R.drawable.owl);
                newSound(R.raw.owl);
            }
        });

        snake = findViewById(R.id.snake);
        snake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="snake";
                imageView.setImageResource(R.drawable.snake);
                newSound(R.raw.snake);
            }
        });
        zebra = findViewById(R.id.zebra);
        zebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerShape="zebra";
                imageView.setImageResource(R.drawable.zebra);
                newSound(R.raw.zebra);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                button.start();
                mp.stop();

                Intent intent = new Intent(getApplicationContext(),AktivnostIgre.class);
                intent.putExtra("SESSION_ID",playerShape);
                startActivity(intent);
                finish();
            }
        });
    }

}
