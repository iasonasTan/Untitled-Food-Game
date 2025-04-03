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

    protected final void loadTextures(String[] textures, String texturesRoot) throws Exception {
        final int n=textures.length;
        sprites=new Image[n];
        for(int i=0; i<n; i++) {
            sprites[i]= ImageIO.read(getClass().getResource(texturesRoot+textures[i]));
        }
    }
}
