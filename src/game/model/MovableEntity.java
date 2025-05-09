package game.model;

import main.Game;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public abstract class MovableEntity extends Entity {
    protected int currentSpeed;
    private int defaultSpeed;
    private final Set<Side> directions =new LinkedHashSet<>();
    protected int spriteCounter =0;

    // dash
    private long dashTimeout=0;
    private boolean onDash=false;

    // fall
    private boolean falling;
    private int velocityDown;

    // jump
    private boolean onJump=false;
    private int velocityUp;

    public MovableEntity(Game game) {
        super(game);
        falling=true;
    }

    public boolean isOnJump () {
        return onJump;
    }

    public void tryFall() {
        rect.y+=Game.DEFAULT_SIZE;
        boolean cond = falling||onJump||context.map.isCollider(this, Side.DOWN);
        rect.y-=Game.DEFAULT_SIZE;

        if (!cond) {
            velocityDown=2;
            addDirection(Side.DOWN);
            falling=true;
        }
    }

    protected void countSprites() {
        spriteCounter++;

        if (spriteCounter %4==0){
            sprite_idx++;

            if (sprite_idx >=sprites.length)
                sprite_idx =0;
        }
    }

    @Override
    public void update() {
        if (falling) {
            velocityDown+=3;
            move(0, velocityDown);
            Optional<Tile> collider_opt=context.map.collider(this);
            if (collider_opt.isPresent()&&
                    context.map.isCollider(this, Side.DOWN)) {

                removeDirection(Side.DOWN);
                falling = false;
                Tile tile=collider_opt.get();
                worldY=tile.worldY-tile.height;
                updateRect();
            }
        } else tryFall();

        if (System.currentTimeMillis()>=dashTimeout&&onDash) {
            currentSpeed=defaultSpeed;
            onDash=false;
        }
        if (onJump) {
            worldY-=velocityUp;
            velocityUp-=3;
            addDirection(Side.UP);

            Optional<Tile> coll_opt=context.map.collider(this);
            if (velocityUp<=0) {
                onJump = false;
                removeDirection(Side.UP);
            }
            if (coll_opt.isPresent()&&
                    context.map.isCollider(this, Side.DOWN)) {

                Entity coll=coll_opt.get();
                worldY=coll.worldY+coll.height;

                onJump = false;
                removeDirection(Side.UP);
            }
        }
    }

    public void removeDirection(Side s) {
        directions.remove(s);
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

    /// return true if did jump ;)
    public boolean jump (int height) {
        if (onJump||context.map.above(this))
            return false;

        onJump=true;
        velocityUp=height;
        return true;
    }

    public void addDirection(Side d) {
        if (d==null)
            return;

        directions.add(d);
        try {
            directions.remove(Side.opposite(d));
        } catch (NoSuchElementException nsee) {
            // ignore
        }
    }

    protected final void move_unsafe(int sx, int sy) {
        worldX+=sx;
        worldY+=sy;
        updateRect();
    }

    @Override
    public boolean move(int stepsX, int stepsY) {
        int dirs_count=directions.size();
        Side collideSide = context.map.getCollideSide(this);
        if (stepsX>0&&collideSide!=Side.RIGHT||
                stepsX<0&&collideSide!=Side.LEFT) {

            onJump=false;
            addDirection(stepsX>0?Side.RIGHT:Side.LEFT);
        } else if (stepsY>0&&collideSide!=Side.DOWN||
                stepsY<0&&collideSide!=Side.UP) {

            worldY += stepsY;
            addDirection(stepsY>0?Side.DOWN:Side.UP);
        }
        updateRect();
        return dirs_count != directions.size();
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

}
