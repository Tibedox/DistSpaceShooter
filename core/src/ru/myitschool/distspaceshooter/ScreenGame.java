package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
    Texture imgFragmentEnemy, imgFragmentShip;
    Sound sndShot;
    Sound sndExplosion;

    Stars[] stars = new Stars[2];
    Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1100;
    long timeShotLastSpawn, timeShotSpawnInterval = 500;

    public ScreenGame(MyGG myGG){
        gg = myGG;

        isAccelerometerPresent = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        //isGyroscopePresent = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);

        imgStars = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgShot = new Texture("shot.png");
        imgFragmentEnemy = new Texture("fragmentenemy.png");
        imgFragmentShip = new Texture("fragmentship.png");
        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        stars[0] = new Stars(SCR_WIDTH/2f, SCR_HEIGHT/2f, SCR_WIDTH, SCR_HEIGHT);
        stars[1] = new Stars(SCR_WIDTH/2f, SCR_HEIGHT*3f/2, SCR_WIDTH, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2f, 100, 100, 100);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.isTouched()) {
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            ship.vx = (gg.touch.x-ship.x)/20;
        } else if(isAccelerometerPresent) {
            ship.vx = -Gdx.input.getAccelerometerX()*10;
        } /*else if(isGyroscopePresent) {
            ship.vx = Gdx.input.getGyroscopeY()*10;
        }*/

        // события игры
        for (Stars s: stars) s.move();
        ship.move();
        spawnEnemyes();
        for (int i = enemies.size()-1; i >= 0; i--) {
            enemies.get(i).move();
            if(enemies.get(i).outOfScreen()) {
                enemies.remove(i);
            }
        }
        spawnShots();
        for (int i = shots.size()-1; i >= 0; i--) {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) {
                shots.remove(i);
                break;
            }
            for (int j = enemies.size()-1; j >= 0; j--) {
                if(shots.get(i).overlap(enemies.get(j))){
                    spawnFragments(enemies.get(j).x, enemies.get(j).y);
                    shots.remove(i);
                    enemies.remove(j);
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
        for (Fragment f: fragments) gg.batch.draw(imgFragmentEnemy, f.scrX(), f.scrY(), f.width, f.height);
        for (Enemy e: enemies) gg.batch.draw(imgEnemy, e.scrX(), e.scrY(), e.width, e.height);
        for (Shot s: shots) gg.batch.draw(imgShot, s.scrX(), s.scrY(), s.width, s.height);
        gg.batch.draw(imgShip, ship.scrX(), ship.scrY(), ship.width, ship.height);
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
        imgFragmentEnemy.dispose();
        imgFragmentShip.dispose();
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

    void spawnFragments(float x, float y){
        for (int i = 0; i < 1250; i++) {
            fragments.add(new Fragment(x, y));
        }
    }
}
