package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_HEIGHT;

public class Stars extends SpaceObject{
    public Stars(float x, float y, float width, float height) {
        super(x, y, width, height);
        vy = -2;
    }

    @Override
    public void move() {
        super.move();
        if(outOfScreen()) y = SCR_HEIGHT*3/2;
    }

    boolean outOfScreen(){
        return y<-SCR_HEIGHT/2;
    }
}
