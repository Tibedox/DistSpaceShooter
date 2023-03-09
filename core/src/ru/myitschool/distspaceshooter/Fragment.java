package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int type;

    public Fragment(float x, float y) {
        super(x, y, MathUtils.random(30, 50), MathUtils.random(30, 50));
        vx = MathUtils.random(-8f, 8);
        vy = MathUtils.random(-8f, 8);
    }

    boolean outOfScreen(){
        return x<-width/2 || x>SCR_WIDTH+width/2 || y<-height/2 || y>SCR_HEIGHT+height/2;
    }
}
