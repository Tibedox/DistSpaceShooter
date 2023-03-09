package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int type;
    float a, v;

    public Fragment(float x, float y) {
        super(x, y, MathUtils.random(5, 10), MathUtils.random(5, 10));
        v = MathUtils.random(0.1f, 8);
        a = MathUtils.random(0f, 360);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
    }

    boolean outOfScreen(){
        return x<-width/2 || x>SCR_WIDTH+width/2 || y<-height/2 || y>SCR_HEIGHT+height/2;
    }
}
