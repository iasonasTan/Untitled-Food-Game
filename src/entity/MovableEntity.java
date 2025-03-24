package entity;

import main.Game;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class MovableEntity extends Entity {
    protected int currentSpeed;
    private int defaultSpeed;
    private long dashTimeout=0;
    private boolean onDash=false;

    public MovableEntity(Game game) {
        super(game);
    }

    @Override
    public void update () {
        if (System.currentTimeMillis()>=dashTimeout&&onDash) {
            currentSpeed=defaultSpeed;
        }
    }

    public void setDefaultSpeed (int speed) {
        defaultSpeed=speed;
        currentSpeed=speed;
    }

    public void jump (int diff, long millis) {
        currentSpeed+=diff;
        onDash=true;
        dashTimeout=System.currentTimeMillis()+millis;
    }

    public void move(int stepsX, int stepsY) {
        worldX += stepsX;
        worldY += stepsY;
    }

    public boolean isOnDash () {
        return onDash;
    }
}
