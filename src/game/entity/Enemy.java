package game.entity;

import main.Game;

import java.awt.*;

public final class Enemy extends MovableEntity {
    private boolean activated=false;

    public Enemy(Game ctx, Point pos) {
        super(ctx);
        this.worldX=pos.x;
        this.worldY=pos.y;
        setDefaultValues();
        try {
            loadTextures(new String[]{"1.png", "2.png", "3.png"},
                    "/game/entity/enemy");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    public void render (Graphics g) {
        activated=true;
        super.render(g);
    }

    @Override
    public void update() {
        if (!activated||!context.tileM.collideD(this, 20))
            return;

        super.update();
        updateRect();
        if (context.player.worldX<this.worldX) {
            this.worldX-=currentSpeed;
        } else if (context.player.worldX>this.worldX) {
            this.worldX+=currentSpeed;
        }
    }

    @Override
    public void setDefaultValues() {
        rect =new Rectangle(worldX, worldY, width, height);
        solid=true;
        setDefaultSpeed(4);
    }
}
