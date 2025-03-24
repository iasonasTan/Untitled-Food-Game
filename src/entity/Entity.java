package entity;

import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    protected int worldX, worldY;
    protected int width=Game.TILE_SIZE;
    protected int height=Game.TILE_SIZE;
    protected Rectangle collision;
    protected boolean solid;
    protected Game context;
    protected BufferedImage[] sprites;

    public Entity (Game ctx) {
        this.context=ctx;
    }

    abstract public void update();
    abstract public void render(Graphics g);
    abstract public void setDefaultValues();

    public boolean collides (Entity other) {
        return collision.intersects(other.collision)&& solid;
    }
}
