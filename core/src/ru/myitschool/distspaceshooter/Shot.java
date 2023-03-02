package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;
import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Shot extends SpaceObject{
    public Shot(float x, float y, float width, float height) {
        super(x, y, width, height);
        vy = 10;
    }

    boolean outOfScreen(){
        return y > SCR_HEIGHT + height/2;
    }
}
