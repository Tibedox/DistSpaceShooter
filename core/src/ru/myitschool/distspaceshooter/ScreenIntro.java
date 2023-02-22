package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    MyGG gg;
    Texture imgBG;
    TextButton btnPlay, btnSettings, btnAbout, btnExit;
    String titleOfGame = "Space Shooter";

    public ScreenIntro(MyGG myGG){
        gg = myGG;
        btnPlay = new TextButton(gg.fontLarge, "PLAY", 50, 400);
        btnSettings = new TextButton(gg.fontLarge, "SETTINGS", 50, 300);
        btnAbout = new TextButton(gg.fontLarge, "ABOUT", 50, 200);
        btnExit = new TextButton(gg.fontLarge, "EXIT", 50, 100);
        imgBG = new Texture("bg/space00.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()) {
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            if(btnPlay.hit(gg.touch.x, gg.touch.y)){
                gg.setScreen(gg.screenGame);
            }
            if(btnSettings.hit(gg.touch.x, gg.touch.y)){
                gg.setScreen(gg.screenSettings);
            }
            if(btnAbout.hit(gg.touch.x, gg.touch.y)){
                gg.setScreen(gg.screenAbout);
            }
            if(btnExit.hit(gg.touch.x, gg.touch.y)){
                Gdx.app.exit();
            }
        }

        // игровые события

        // отрисовка графики
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gg.fontTitle.draw(gg.batch, titleOfGame, 20, 860);
        btnPlay.font.draw(gg.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(gg.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(gg.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(gg.batch, btnExit.text, btnExit.x, btnExit.y);
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
        imgBG.dispose();
    }
}
