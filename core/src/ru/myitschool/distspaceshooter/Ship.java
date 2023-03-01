package ru.myitschool.distspaceshooter;

import static ru.myitschool.distspaceshooter.MyGG.SCR_WIDTH;

public class Ship extends SpaceObject{
    public Ship(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void move() {
        super.move();
        outOfScreen();
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
}
