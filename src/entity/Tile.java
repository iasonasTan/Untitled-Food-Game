package entity;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile extends Entity {
    public Tile(Game ctx, String res, Type type, int worldX, int worldY) {
        super(ctx);
        this.worldX=worldX;
        this.worldY=worldY;
        solid=type==Type.SOLID;
        loadImage(res);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprites[0], worldX, worldY, width, height, null);
    }

    @Override
    public void setDefaultValues() {

    }

    public void loadImage (String res) {
        sprites=new BufferedImage[1];
        try {
            sprites[0]= ImageIO.read(getClass().getResource(res));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static enum Type {
        SOLID,
        NON_SOLID
    }
}
