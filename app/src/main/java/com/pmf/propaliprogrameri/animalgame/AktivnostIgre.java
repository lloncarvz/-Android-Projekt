package com.pmf.propaliprogrameri.animalgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class AktivnostIgre extends Activity {

    String test;
    PrikazIgre prikazIgre;

    MediaPlayer mp;

    int sirinaEkrana, visinaEkrana;

    Igrac igrac;
    Point trenutnaPozicijaIgraca;

    OrientationData orientationData;

    ArrayList<Avion> avioni = new ArrayList<>();
    ArrayList<Dragulj> dragulji = new ArrayList<>();
    ArrayList<Dragulj> dragulji2 = new ArrayList<>();

    Drawable izgled;
    String izgledIgraca;

    long pocetakVremena;
    long pocetakIgre;
    int protekloVrijeme;

    long pocetakStvaranjaDragulja;

    int igracX,igracY;
    int velicinaIgraca;

    int score;

    int br;
    float brzina = 0.6f;
    float brzinaStvaranja = 3000;

    Vibrator v;

    class Dragulj{
        int draguljY,draguljX;
        Drawable izgledDragulja;
        int bodovi;
        int velicinaDragulja;
        int brzinaDragulja;

        public Dragulj(){
            velicinaDragulja = visinaEkrana/10;
            Random r = new Random();
            int randIzgled = r.nextInt(100) + 1;
            if(randIzgled >94){
                bodovi = 10;
                izgledDragulja = getResources().getDrawable(R.drawable.green);
                brzinaDragulja = 15;
            }else if(randIzgled > 69){
                bodovi = 5;
                izgledDragulja = getResources().getDrawable(R.drawable.pink);
                brzinaDragulja = 10;
            }else{
                bodovi = 1;
                izgledDragulja = getResources().getDrawable(R.drawable.yellow);
                brzinaDragulja = 5;
            }
            int randPoz = r.nextInt(sirinaEkrana - velicinaDragulja) + velicinaDragulja;
            izgledDragulja.setBounds(randPoz,-velicinaDragulja, randPoz + velicinaDragulja, 0);
            draguljY = -visinaEkrana/12;
            draguljX = randPoz;
        }
        public void update(){
            draguljY += brzinaDragulja;
            izgledDragulja.setBounds(draguljX,draguljY,draguljX + velicinaDragulja,draguljY+velicinaDragulja);
        }
        public void draw(Canvas platno){izgledDragulja.draw(platno);}
    }

    class Igrac{

        public Igrac(){
            izgled = getResources().getDrawable(getResources().getIdentifier(izgledIgraca,"drawable",getPackageName()));;
            velicinaIgraca = visinaEkrana/12;
            izgled.setBounds(0,0,velicinaIgraca,velicinaIgraca);
            update(new Point(sirinaEkrana/8,visinaEkrana/2));
        }

        public void update(Point p){
            izgled.setBounds(p.x-velicinaIgraca/2,p.y-velicinaIgraca/2,p.x+velicinaIgraca/2,p.y+velicinaIgraca/2);
            igracX = p.x - velicinaIgraca/2;
            igracY = p.y - velicinaIgraca/2;
        }

        public void draw(Canvas platno){
            izgled.draw(platno);
        }
    }

    class Avion{
        Drawable izgled;
        int visinaAviona,sirinaAviona;
        int x,y;
        boolean flag;
        float brzina;

        public Avion(float b){
            izgled = getApplicationContext().getResources().getDrawable(R.drawable.airship);
            visinaAviona = visinaEkrana/4;
            sirinaAviona = (int)Math.round(visinaAviona*1.15);

            Random r = new Random();

            y = r.nextInt(visinaEkrana - visinaAviona);
            x = sirinaEkrana;
            izgled.setBounds(x,y,x+sirinaAviona,y+visinaAviona);
            flag = true;
            brzina = b;
        }

        public void update(Point p){
            izgled.setBounds(p.x,y,p.x + sirinaAviona,y+visinaAviona);
            x = p.x;
        }

        public void draw(Canvas platno){
            izgled.draw(platno);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Prikazivanje ekrana bez naslova i puni ekran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        orientationData = new OrientationData(getApplicationContext());
        orientationData.register();

        mp = MediaPlayer.create(this, R.raw.gamemusic);
        mp.setLooping(true);
        mp.start();

        int sirinaStatusBar = getApplicationContext().getResources().getDimensionPixelSize(getApplicationContext().getResources().getIdentifier("status_bar_height","dimen","android"));
        int sirinaNavigationBar = getApplicationContext().getResources().getDimensionPixelSize(getApplicationContext().getResources().getIdentifier("navigation_bar_height","dimen","android"));

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        sirinaEkrana=dm.widthPixels + sirinaStatusBar + sirinaNavigationBar;
        visinaEkrana=dm.heightPixels;

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(getIntent()!= null) {
            if (getIntent().getStringExtra("SESSION_ID") != null) {
                izgledIgraca = getIntent().getStringExtra("SESSION_ID");
            }
        }
        else
            izgledIgraca = "pig";

        score=0;
        br=0;

        igrac=new Igrac();
        trenutnaPozicijaIgraca = new Point(sirinaEkrana/6,visinaEkrana/2);

        pocetakVremena = System.currentTimeMillis();
        pocetakIgre = System.currentTimeMillis();
        pocetakStvaranjaDragulja = System.currentTimeMillis();

        prikazIgre = new PrikazIgre(this);
        setContentView(prikazIgre);
    }

    class PrikazIgre extends SurfaceView implements Runnable {



        Drawable pozadina;

        Thread glavnaDretva = null;

        //drzac podloge kako bi canvas bio uvijek na istom mjestu
        SurfaceHolder drzac;

        //bool varijabla koja nam služi kako bi provjerili je li igra radi
        boolean igra;

        Canvas platno;
        Paint boja;

        //broj okvira po sekundi
        long vrijemeOkvira;

        //Konstruktor prikazaigre koji prima kontekst tj. sve informacije o okruženju aplikacije
        public PrikazIgre(Context context) {
            super(context);
            drzac = getHolder();
            boja = new Paint();
            igra = true;

            pozadina = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.background) ,sirinaEkrana,visinaEkrana,true));
            pozadina.setBounds(0,0,sirinaEkrana,visinaEkrana);
        }


        @Override
        public void run() {
            while(igra){
                long pocetnoVrijeme = System.currentTimeMillis();
                update();
                draw();

                //racuna se broj okvira u sekundi u ovom okviru kako bi mogli tajmirati animacije
                vrijemeOkvira = System.currentTimeMillis() - pocetnoVrijeme;
                long razlikaVremena = pocetnoVrijeme - pocetakIgre;
                protekloVrijeme = (int) razlikaVremena / 1000;

            }
        }

        public void update(){

            if(System.currentTimeMillis() - pocetakVremena > brzinaStvaranja)
            {
                avioni.add(new Avion(brzina));
                pocetakVremena = System.currentTimeMillis();
                if(brzinaStvaranja > 2000)
                    brzinaStvaranja *=0.97;
                else
                    brzinaStvaranja *=0.99;
                br++;
            }
            if(br == 3)
            {
                brzina+=0.05;
                br=0;
            }

            if(System.currentTimeMillis() - pocetakStvaranjaDragulja > 1000){
                Random r = new Random();
                if(r.nextFloat() > 0.7)
                    dragulji.add(new Dragulj());
                pocetakStvaranjaDragulja = System.currentTimeMillis();
            }


            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null){
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float ySpeed = 2 * roll * visinaEkrana / 1000f;
                float xSpeed = pitch * sirinaEkrana / 1000f;

                trenutnaPozicijaIgraca.y -= Math.abs(ySpeed * vrijemeOkvira) > 5 ? ySpeed * vrijemeOkvira : 0;
                trenutnaPozicijaIgraca.x -= Math.abs(xSpeed * vrijemeOkvira) > 5 ? xSpeed * vrijemeOkvira : 0;
            }

            if(trenutnaPozicijaIgraca.x < 0)
                trenutnaPozicijaIgraca.x = 0;
            else if(trenutnaPozicijaIgraca.x > sirinaEkrana)
                trenutnaPozicijaIgraca.x = sirinaEkrana;

            if(trenutnaPozicijaIgraca.y < 0)
                trenutnaPozicijaIgraca.y = 0;
            else if(trenutnaPozicijaIgraca.y > visinaEkrana)
                trenutnaPozicijaIgraca.y = visinaEkrana;

            igrac.update(trenutnaPozicijaIgraca);
            test = Integer.toString(igracX) + " - " +  Integer.toString(igracY);



            for(Avion a:avioni){
                int x = (int)(a.x - a.brzina * vrijemeOkvira);
                a.update(new Point(x,0));
                if(avioni.size()>0){
                    if(Rect.intersects(new Rect(a.x,a.y,a.x+a.sirinaAviona-40,a.y + a.visinaAviona-40),new Rect(igracX - velicinaIgraca/2,igracY - velicinaIgraca/2,igracX + velicinaIgraca/2, igracY + velicinaIgraca/2))){
                        gameOver();
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                        else
                        {
                            v.vibrate(500);
                        }
                    }
                    if((a.x + a.sirinaAviona) < igracX && a.flag){
                        score++;
                        a.flag = false;
                    }
                }
            }
            if(avioni.size()>0){
                if((avioni.get(0).x + avioni.get(0).sirinaAviona + 100) < 0)
                    avioni.remove(0);
            }
            for(Dragulj d:dragulji){
                d.update();
                if(d.draguljY > visinaEkrana + d.velicinaDragulja)
                    dragulji2.add(d);
            }
            dragulji.removeAll(dragulji2);
            dragulji2.clear();


        }

        public void draw(){
            if(drzac.getSurface().isValid()){
                platno = drzac.lockCanvas();

                pozadina.draw(platno);


                igrac.draw(platno);
                for(Avion a:avioni) {
                    a.draw(platno);
                }
                for(Dragulj d:dragulji){
                    d.draw(platno);
                }


                Paint paint = new Paint();
                paint.setTextSize(100);
                paint.setColor(Color.parseColor("#5c86ab"));
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                //platno.drawText("Score: " + brzina + "  -  " + brzinaStvaranja,50,50 + paint.descent() - paint.ascent(),paint);
                platno.drawText("Score: " + score,50,50 + paint.descent() - paint.ascent(),paint);
                //platno.drawText(sirinaEkrana + "  -  " + visinaEkrana,50,50 + paint.descent() - paint.ascent(),paint);
                //platno.drawText(test,50,50 + paint.descent() - paint.ascent(),paint);


                drzac.unlockCanvasAndPost(platno);
            }
        }


        public void pause() {
            igra = false;
            try {
                glavnaDretva.join();
            } catch (InterruptedException e) {
            }

        }
        public void resume() {
            igra = true;
            glavnaDretva = new Thread(this);
            glavnaDretva.start();
            this.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            int x = (int)e.getX();
            int y = (int)e.getY();
            ArrayList<Dragulj> dr = new ArrayList<>();
            if(e.getAction() == MotionEvent.ACTION_DOWN){
                if(dragulji.size()>0){
                    for(Dragulj d: dragulji){
                        if(x >= d.draguljX && y >= d.draguljY && x <= d.draguljX+d.velicinaDragulja && y <= d.draguljY+d.velicinaDragulja){
                            score += d.bodovi;
                            dr.add(d);
                        }
                    }
                    dragulji.removeAll(dr);
                    dr.clear();
                }
            }
            return true;
        }
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

    //metode koje nam reguliraju korištenje glavne dretve kako bi ju ponovo pokretali i spajali nazad s androidom
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        mp.seekTo(mp.getCurrentPosition());
        mp.start();
        prikazIgre.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        updateUI();
        mp.pause();

        // Tell the gameView pause method to execute
        prikazIgre.pause();
    }

    public void gameOver(){
        mp.stop();
        Intent intent=new Intent(getApplicationContext(),GameOver.class);
        intent.putExtra("SCORE",score);
        intent.putExtra("SESSION_ID",izgledIgraca);
        startActivity(intent);
        finish();
    }
}
