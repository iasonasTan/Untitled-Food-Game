package game.model;

import main.Game;

import java.awt.*;

public final class Player extends MovableEntity {
    /*
     * fields:
     *  int worldX;
     *  int worldY;
     * used as:
     *  int screenX;
     *  int screenY;
     */

    // TODO Player is NOT a MovableEntity
    // Change superclass of Player

    public Player(Game ctx) {
        super(ctx);
        setDefaultValues();
        try {
            loadTextures(new String[]{"1.png", "2.png", "3.png"},
                    "/game/model/player");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRect () {
        final int GAP=10;
        rect.x=worldX+GAP;
        rect.width=width-GAP*2;

        rect.y=worldY+GAP;
        rect.height=height-GAP;
    }

    @Override
    public void update() {
        if (context.keyHandler.left||
                context.keyHandler.right&&!isOnJump()) {

            countSprites();
        }

        updateRect();

        if (context.keyHandler.jump) {
            context.keyHandler.jump=false;
            jump(height/2);
        }

        super.update();
    }

    @Override
    public boolean move(int stepsX, int stepsY) {
        if (stepsX!=0)
            throw new IllegalArgumentException("move map instead");
        return super.move(stepsX, stepsY);
    }

    @Override
    public void setDefaultValues() {
        worldX=Game.SCREEN.width/2-width;
        worldY=100;
        rect =new Rectangle(worldX, worldY, width, height);
        solid=true;
    }
}
