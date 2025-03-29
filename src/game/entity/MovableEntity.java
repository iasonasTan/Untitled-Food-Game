package game.entity;

import main.Game;

import java.util.Optional;

public abstract class MovableEntity extends Entity {
    protected int currentSpeed;
    private int defaultSpeed;
    private long dashTimeout=0;
    private boolean onDash=false;
    private boolean falling=true;
    private int velocityDown=0;

    public MovableEntity(Game game) {
        super(game);
    }

    public void fall () {
        velocityDown=0;
        falling=true;
    }

    @Override
    public void update () {
        if (falling) {
            velocityDown+=1;
            falling=move(0, velocityDown);
//            if (!falling) {
//                move(0, -5);
//            }
        }
        if (System.currentTimeMillis()>=dashTimeout&&onDash) {
            currentSpeed=defaultSpeed;
            onDash=false;
        }
    }

    public void setDefaultSpeed (int speed) {
        defaultSpeed=speed;
        currentSpeed=speed;
    }

    public void dash(int diff, long millis) {
        currentSpeed+=diff;
        onDash=true;
        dashTimeout=System.currentTimeMillis()+millis;
    }

    @Override
    public boolean move(int stepsX, int stepsY) {
        worldX += stepsX;
        worldY += stepsY;
        Optional<Tile> tile=context.tileM.collider(this);
        if (tile.isPresent()) {
            worldX-=stepsX*2;
            worldY-=stepsY*2;
            return false;
        }
        return true;
    }

    public boolean isOnDash () {
        return onDash;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }
}
