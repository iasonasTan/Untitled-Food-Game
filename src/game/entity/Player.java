package game.entity;

import main.Game;

import java.awt.*;

public final class Player extends MovableEntity {
    /**
     variables:
        int worldX;
        int worldY;
     are:
        int screenX;
        int screenY;
     */

    private boolean onJump=false;
    private int velocityUp;

    public Player(Game ctx) {
        super(ctx);
        setDefaultValues();
        try {
            loadTextures(new String[]{"1.png", "2.png", "3.png"},
                    "/game/entity/player");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// return true if did jump ;]
    public boolean jump (int height) {
        if (onJump)
            return false;

        onJump=true;
        velocityUp=height;
        return true;
    }

    @Override
    public void updateRect () {
        final int GAP=15;
        rect.x=worldX+GAP;
        rect.y=worldY+GAP;
        rect.width=width-GAP*2;
        rect.height=height-GAP;
    }

    @Override
    public void update() {
        if (context.keyHandler.left||
                context.keyHandler.right&&!onJump) {

            countSprites();
        }

        if (onJump) {
            worldY-=velocityUp;
            velocityUp-=1;

            if (velocityUp<=0)
                onJump=false;
        }

        updateRect();

        if (context.keyHandler.jump) {
            context.keyHandler.jump=false;
            jump(height/5*4);
        }

        super.update();
    }

    @Override
    public void setDefaultValues() {
        worldX=250; // WORLD_X IS SCREEN_X
        worldY=100;
        rect =new Rectangle(worldX, worldY, width, height);
        solid=true;
    }
}
