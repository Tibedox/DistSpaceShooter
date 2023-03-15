package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int type;
    float angle;
    float speedRotation;
    TextureRegion img;

    public Fragment(TextureRegion[] imgs, float x, float y) {
        super(x, y, MathUtils.random(5, 40), MathUtils.random(5, 40));
        float v = MathUtils.random(0.1f, 8);
        float a = MathUtils.random(0f, 360);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
        img = imgs[MathUtils.random(0, imgs.length-1)];
        speedRotation = MathUtils.random(-5f, 5);
    }

    @Override
    public void move() {
        super.move();
        angle += speedRotation;
    }

    boolean outOfScreen(){
        return x<-width/2 || x>SCR_WIDTH+width/2 || y<-height/2 || y>SCR_HEIGHT+height/2;
    }
}
