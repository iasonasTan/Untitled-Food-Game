package game.entity;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends MovableEntity {
    public Player(Game ctx) {
        super(ctx);
        setDefaultValues();
    }

    @Override
    public void update() {
        collision.x=worldX;
        collision.y=worldY;
        if (context.keyHandler.jump) {
            context.keyHandler.jump=false;
            worldY-=Game.TILE_SIZE*3;
            fall();
        }
        super.update();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprites[0], worldX, worldY, width, height, null);
    }

    @Override
    public void setDefaultValues() {
        worldX=250; // WORLD_X IS SCREEN_X
        worldY=100;
        collision=new Rectangle(worldX, worldY, width, height);
        solid=true;
        sprites=new BufferedImage[1];
        try {
            sprites[0]= ImageIO.read(getClass().getResource("/game/entity/red.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
