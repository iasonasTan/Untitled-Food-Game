package game.model;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public abstract class Entity {
    // screen
    protected int worldX, worldY;
    protected int width=Game.DEFAULT_SIZE;
    protected int height=Game.DEFAULT_SIZE;

    // physics
    protected Rectangle rect;
    protected boolean solid;

    // logic
    protected Game context;
    protected Image[] sprites;
    protected int sprite_idx =0;

    public Entity (Game ctx) {
        context=ctx;
    }

    public Rectangle getRect () { return rect; }
    public int getWorldX () { return worldX; }
    public int getWorldY () { return worldY; }
    public int getWidth () { return width; }
    public int getHeight () { return height; }
    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }

    abstract public void update();
    abstract public void setDefaultValues();

    public void render(Graphics g){
        g.drawImage(sprites[sprite_idx], worldX, worldY, width, height, null);
    }

    public Side getCollideSide(Entity other) {
        Rectangle ore=other.rect;
        if (!rect.intersects(ore)||!solid||!other.solid) return Side.NONE;

        int dx1 = rect.x+rect.width-ore.x;
        int dx2 = ore.x+ore.width-rect.x;
        int dy1 = rect.y+rect.height-ore.y;
        int dy2 = ore.y+ore.height-rect.y;

        int overlapX = Math.min(dx1, dx2);
        int overlapY = Math.min(dy1, dy2);

        Side out;
        if (overlapX < overlapY)
            out = (rect.x<ore.x)?Side.RIGHT:Side.LEFT;
        else
            out = (rect.y<ore.y)?Side.DOWN:Side.UP;

        return out;
    }

    public boolean collides(Entity other) {
        return solid&&other.solid&&other.rect.intersects(rect);
    }

    public int distance (Entity other) {
        Rectangle otherR=other.rect;
        double diffX=Math.abs(otherR.x-rect.x);
        double diffY=Math.abs(otherR.y-rect.y);
        double distance=Math.sqrt(diffX*diffX+diffY*diffY);
        return (int)distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, worldX, worldY);
    }

    @Override
    public boolean equals (Object o) {
        if (o==this) return true;
        if (o==null) return false;
        if (o.getClass()!=this.getClass()) return false;

        Entity oe = (Entity) o;
        return oe.worldX == worldX && oe.worldY == worldY &&
                oe.height == height && oe.width == height;
    }

    @SuppressWarnings("all")
    protected final void loadTextures(String[] textures, String texturesRoot)
            throws Exception {

        final int n=textures.length;
        sprites=new Image[n];
        for(int i=0; i<n; i++) {
            sprites[i]= ImageIO.read(getClass().getResource(
                    texturesRoot+textures[i]));
        }
    }

    public Point getCenter () {
        Point out=new Point(worldX, worldY);
        out.x+=width/2;
        out.y+=height/2;
        return out;
    }

    public static Point follow (Point target, MovableEntity follower) {
        final float diffX=target.x-follower.worldX;
        final float diffY=target.y-follower.worldY;
        final float distance=(float)Math.sqrt(diffX*diffX+diffY*diffY);
        float directionX=0;
        float directionY=0;
        if (distance!=0) {
            directionX=diffX/distance;
            directionY=diffY/distance;
        }
        final float stepsX= follower.currentSpeed*directionX;
        final float stepsY=follower.currentSpeed*directionY;
        return new Point((int)stepsX, (int)stepsY);
    }

    public void updateRect () {
        rect.x=worldX;
        rect.y=worldY;
        rect.width=width;
        rect.height=height;
    }

    public boolean move(int stepsX, int stepsY) {
        worldX += stepsX;
        worldY += stepsY;
        return true;
    }

    public enum Side {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN;

        public static Side opposite(Side d) {
            return switch(d) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
                default -> NONE;
            };
        }
    }
}
