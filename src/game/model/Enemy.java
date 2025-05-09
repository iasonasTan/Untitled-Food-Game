package game.model;

import game.handler.MapHandler;
import main.Game;

import java.awt.*;

public final class Enemy extends MovableEntity {
    private boolean sleeping =true;
    @SuppressWarnings("all")
    private final long THROW_DELAY_MS=2400; // ms
    private long previousThrowTime_ms=System.currentTimeMillis();

    public Enemy(Game ctx, Point pos) {
        super(ctx);
        this.worldX=pos.x;
        this.worldY=pos.y;
        setDefaultValues();
        try {
            loadTextures(new String[]{"1.png", "2.png", "3.png"},
                    "/game/model/enemy");
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
        sleeping =false;
        super.render(g);
    }

    public void attackPlayer () {
        Projectile projectile=new Projectile(context, this, context.player);
        context.projectilesMan.addEntity(projectile);
    }

    @Override
    public void update() {
        if (sleeping)
            return;

        if (MapHandler.FLOOR * Game.DEFAULT_SIZE < this.worldY+height) {
            jump(height);
        }

        if (previousThrowTime_ms+THROW_DELAY_MS<=System.currentTimeMillis()) {
            previousThrowTime_ms=System.currentTimeMillis();
            attackPlayer();
        }
        super.update();
        countSprites();
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
