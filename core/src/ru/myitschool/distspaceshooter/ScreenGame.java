package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    MyGG gg;

    boolean isAccelerometerPresent;
    //boolean isGyroscopePresent;

    Texture imgStars;
    Texture imgShip;
    Texture imgEnemy;
    Texture imgShot;
    Texture imgAtlasFragment;
    TextureRegion[] imgFragmentEnemy = new TextureRegion[4];
    TextureRegion[] imgFragmentShip = new TextureRegion[4];
    Sound sndShot;
    Sound sndExplosion;
    private final int TYPE_ENEMY = 0, TYPE_SHIP = 1;

    Stars[] stars = new Stars[2];
    Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();
    Player[] players = new Player[10];

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1100;
    long timeShotLastSpawn, timeShotSpawnInterval = 500;

    int kills;

    TextButton btnRestart, btnBack;

    // состояние игры
    public static final int PLAY_GAME = 0, SHOW_TABLE = 2;
    int state = PLAY_GAME;

    public ScreenGame(MyGG myGG){
        gg = myGG;

        isAccelerometerPresent = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        //isGyroscopePresent = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);

        imgStars = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgShot = new Texture("shot.png");
        imgAtlasFragment = new Texture("atlasfragment.png");

        btnRestart = new TextButton(gg.fontSmall, "RESTART", 10, 50);
        btnBack = new TextButton(gg.fontSmall, "BACK", 0, 50);
        btnBack.x = SCR_WIDTH-10-btnBack.width;

        for (int i = 0; i < imgFragmentEnemy.length; i++) {
            imgFragmentEnemy[i] = new TextureRegion(imgAtlasFragment, i*200, 0, 200, 200);
            imgFragmentShip[i] = new TextureRegion(imgAtlasFragment, i*200, 200, 200, 200);
        }
        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname", 0);
        }
        Player.loadTableOfRecords(players);

        stars[0] = new Stars(SCR_WIDTH/2f, SCR_HEIGHT/2f, SCR_WIDTH, SCR_HEIGHT);
        stars[1] = new Stars(SCR_WIDTH/2f, SCR_HEIGHT*3f/2, SCR_WIDTH, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2f, 100, 100, 100);
    }

    @Override
    public void show() {
        gameStart();
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.isTouched()) {
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            ship.vx = (gg.touch.x-ship.x)/20;
            if(state == SHOW_TABLE){
                if(btnRestart.hit(gg.touch.x, gg.touch.y)) gameStart();
                if(btnBack.hit(gg.touch.x, gg.touch.y)) {
                    gg.setScreen(gg.screenIntro);
                }
            }
        } else if(isAccelerometerPresent) {
            ship.vx = -Gdx.input.getAccelerometerX()*10;
        } /*else if(isGyroscopePresent) {
            ship.vx = Gdx.input.getGyroscopeY()*10;
        }*/

        // события игры
        for (Stars s: stars) s.move();
        if(state == PLAY_GAME) ship.move();
        spawnEnemyes();
        for (int i = enemies.size()-1; i >= 0; i--) {
            enemies.get(i).move();
            if(enemies.get(i).outOfScreen()) {
                enemies.remove(i);
                if(ship.visible) {
                    killShip();
                }
            }
        }
        if(ship.visible) spawnShots();
        for (int i = shots.size()-1; i >= 0; i--) {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) {
                shots.remove(i);
                break;
            }
            for (int j = enemies.size()-1; j >= 0; j--) {
                if(shots.get(i).overlap(enemies.get(j))){
                    spawnFragments(enemies.get(j).x, enemies.get(j).y, TYPE_ENEMY);
                    shots.remove(i);
                    enemies.remove(j);
                    kills++;
                    if(gg.soundOn) sndExplosion.play();
                    break;
                }
            }
        }
        for (int i = fragments.size()-1; i >= 0; i--) {
            fragments.get(i).move();
            if(fragments.get(i).outOfScreen()) {
                fragments.remove(i);
            }
        }

        // отрисовка графики
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        for (Stars s: stars) gg.batch.draw(imgStars, s.scrX(), s.scrY(), s.width, s.height);
        for (Fragment f: fragments) gg.batch.draw(f.img, f.scrX(), f.scrY(), f.width/2, f.height/2, f.width, f.height, 1, 1, f.angle);
        for (Enemy e: enemies) gg.batch.draw(imgEnemy, e.scrX(), e.scrY(), e.width, e.height);
        for (Shot s: shots) gg.batch.draw(imgShot, s.scrX(), s.scrY(), s.width, s.height);
        if(ship.visible) gg.batch.draw(imgShip, ship.scrX(), ship.scrY(), ship.width, ship.height);
        gg.fontSmall.draw(gg.batch, "KILLS: "+kills, 10, SCR_HEIGHT-10);
        for (int i = 1; i <= ship.lives; i++) {
            gg.batch.draw(imgShip, SCR_WIDTH - 50*i, SCR_HEIGHT - 50, 40, 40);
        }

        if(state == SHOW_TABLE){
            gg.fontLarge.draw(gg.batch, Player.tableOfRecordsToString(players), SCR_WIDTH/4f, SCR_HEIGHT/4f*3);
            btnRestart.font.draw(gg.batch, btnRestart.text, btnRestart.x, btnRestart.y);
            btnRestart.font.draw(gg.batch, btnBack.text, btnBack.x, btnBack.y);
        }
        gg.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgStars.dispose();
        imgShip.dispose();
        imgShot.dispose();
        imgAtlasFragment.dispose();
        sndShot.dispose();
        sndExplosion.dispose();
    }

    void spawnEnemyes(){
        if(timeEnemyLastSpawn + timeEnemySpawnInterval < TimeUtils.millis()){
            enemies.add(new Enemy(100, 100));
            timeEnemyLastSpawn = TimeUtils.millis();
        }
    }

    void spawnShots(){
        if(timeShotLastSpawn + timeShotSpawnInterval < TimeUtils.millis()){
            shots.add(new Shot(ship.x, ship.y, 100, 100));
            timeShotLastSpawn = TimeUtils.millis();
            if(gg.soundOn) sndShot.play();
        }
    }

    void spawnFragments(float x, float y, int type){
        for (int i = 0; i < 50; i++) {
            if(type == TYPE_ENEMY) fragments.add(new Fragment(imgFragmentEnemy, x, y));
            if(type == TYPE_SHIP) fragments.add(new Fragment(imgFragmentShip, x, y));
        }
    }

    void killShip() {
        spawnFragments(ship.x, ship.y, TYPE_SHIP);
        ship.kill();
        if (gg.soundOn) sndExplosion.play();
        if (ship.lives == 0) gameOver();
    }

    void gameOver() {
        state = SHOW_TABLE;
        players[players.length-1].kills = kills;
        players[players.length-1].name = gg.screenSettings.playerName;
        Player.sortTableOfRecords(players);
        Player.saveTableOfRecords(players);
    }

    void gameStart(){
        state = PLAY_GAME;
        fragments.clear();
        enemies.clear();
        ship = new Ship(SCR_WIDTH/2f, 100, 100, 100);
        kills = 0;
        //if(g.musicOn) music.play();
    }
}
