package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenGame implements Screen {
    MyGG gg;

    Texture imgStars;

    Stars[] stars = new Stars[2];

    public ScreenGame(MyGG myGG){
        gg = myGG;

        imgStars = new Texture("stars.png");

        stars[0] = new Stars(SCR_WIDTH/2f, SCR_HEIGHT/2f, SCR_WIDTH, SCR_HEIGHT);
        stars[1] = new Stars(SCR_WIDTH/2f, SCR_HEIGHT*3f/2, SCR_WIDTH, SCR_HEIGHT);
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
        }

        // события игры
        for (Stars s: stars) s.move();

        // отрисовка графики
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        for (Stars s: stars) gg.batch.draw(imgStars, s.scrX(), s.scrY(), s.width, s.height);

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
    }
}
