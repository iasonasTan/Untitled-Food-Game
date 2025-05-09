package game.model;

import main.Game;

import java.awt.*;
import java.util.Optional;

public final class Player extends MovableEntity {
    /*
     * fields:
     *  int worldX;
     *  int worldY;
     * used as:
     *  int screenX;
     *  int screenY;
     */

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

        context.map.isCollider(this, Side.DOWN, Side.UP).ifPresent(e -> {
            move(0, Math.abs(worldY-e.worldY));
        });

        context.map.collider(this).ifPresent(collider -> {
            Entity.Side sd=context.player.getCollideSide(collider);
            switch(sd) {
                case RIGHT -> {
                    int d=Math.abs(collider.getWorldX()-context.player.getWidth()-context.player.getWorldX());
                    context.map.move(d, 0);
                }
                case LEFT -> {
                    int d=Math.abs(collider.getWorldX()+collider.getWidth()-context.player.getWorldX());
                    context.map.move(-d, 0);
                }
            }
        });

//        context.map.collider(this).ifPresent(c -> {
//            Side cs=c.getCollideSide(this);
//            int diff=0;
//            switch(cs) {
//                case LEFT -> diff=worldX-c.worldX-width;
//                case RIGHT -> diff=worldX-c.worldX+c.width;
//                case UP -> worldY=c.worldY-height;
//                case DOWN -> worldY=c.worldY+c.height;
//            }
//            context.map.move(diff, diff);
//            updateRect();
//        });

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
