package game.entity;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Arrays;

public abstract class Entity {
    // screen
    protected int worldX, worldY;
    protected int width=Game.TILE_SIZE;
    protected int height=Game.TILE_SIZE;

    // physics
    public Rectangle rect;
    protected boolean solid;

    // logic
    protected Game context;
    protected Image[] sprites;
    protected int sprite_idx =0;

    public Entity (Game ctx) {
        context=ctx;
    }

    abstract public void update();
    abstract public void setDefaultValues();

    public void render(Graphics g){
        g.drawImage(sprites[sprite_idx], worldX, worldY, width, height, null);
    }

    public boolean collides (Entity other) {
        return rect.intersects(other.rect)&&
                solid&&other.solid;
    }

    public int distance (Entity other) {
        Rectangle otherR=other.rect;
        double diffX=Math.abs(otherR.x-rect.x);
        double diffY=Math.abs(otherR.y-rect.y);
        double distance=Math.sqrt(diffX*diffX+diffY*diffY);
        return (int)distance;
    }

    @Override
    public boolean equals (Object o) {
        if (o==this)
            return true;
        return o instanceof Entity oe &&
                oe.worldX == this.worldX &&
                oe.worldY == this.worldY &&
                oe.rect.equals(this.rect) &&
                oe.height == this.height &&
                oe.width == this.height &&
                Arrays.equals(oe.sprites, this.sprites);
    }

    protected final void loadTextures(String[] textures, String texturesRoot)
            throws Exception {

        final int n=textures.length;
        sprites=new Image[n];
        for(int i=0; i<n; i++) {
            sprites[i]= ImageIO.read(getClass().getResource(texturesRoot+textures[i]));
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
}
