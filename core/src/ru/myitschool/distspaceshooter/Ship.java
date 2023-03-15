package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    int lives = 3;
    boolean visible = true;
    long timeStartInvisible, timeDurationInvisible = 1500;

    public Ship(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void move() {
        super.move();
        outOfScreen();
        if(!visible) {
            if(timeStartInvisible+timeDurationInvisible<TimeUtils.millis()) {
                reBorn();
            }
        }
    }

    void outOfScreen(){
        if(x<width/2){
            vx = 0;
            x = width/2;
        }
        if(x>SCR_WIDTH-width/2){
            vx = 0;
            x = SCR_WIDTH-width/2;
        }
    }

    void kill() {
        visible = false;
        lives--;
        timeStartInvisible = TimeUtils.millis();
    }

    void reBorn() {
        x = SCR_WIDTH/2f;
        vx = 0;
        visible = true;
    }
}
