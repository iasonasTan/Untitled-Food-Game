package game.entity;

import main.Game;

public abstract class MovableEntity extends Entity {
    protected int currentSpeed;
    private int defaultSpeed;
    private Direction direction;
    protected int spriteCounter =0;

    private long dashTimeout=0;
    private boolean onDash=false;

    private boolean falling=true;
    private int velocityDown;

    public MovableEntity(Game game) {
        super(game);
    }

    public Direction getDirection () {
        return this.direction;
    }

    public void fall () {
        if (context.tileM.collideD(this, Game.TILE_SIZE/3)) {
            if (!falling) {
                velocityDown=2;
                falling=true;
            }
        }
    }

    @Override
    public void update () {
        if (falling) {
            velocityDown+=2;
            falling=move(0, velocityDown);
        }
        fall();
        if (System.currentTimeMillis()>=dashTimeout&&onDash) {
            currentSpeed=defaultSpeed;
            onDash=false;
        }
    }

    public enum Direction {
        LEFT,
        RIGHT;
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

    private boolean move_private(int stepsX, int stepsY) {
        worldX += stepsX;
        worldY += stepsY;
        direction=stepsX<0?Direction.LEFT:Direction.RIGHT;
        updateRect();
        return true;
    }

    @Override
    public boolean move(int stepsX, int stepsY) {
        move_private(stepsX, stepsY);
        if (context.tileM.collider(this).isPresent()) {
            move_private(-stepsX, -stepsY);
            while (context.tileM.collider(this).isPresent()&&stepsX==0) {
                worldY--;
                rect.y=worldY;
            }
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
