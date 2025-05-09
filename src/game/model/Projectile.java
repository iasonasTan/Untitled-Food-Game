package game.model;

import main.Game;
import main.Main;

import java.awt.*;

public class Projectile extends MovableEntity {
    private final Point steps;

    public Projectile(Game game, Entity parent_obj, Entity target_obj) {
        super(game);
        setDefaultValues();
        worldX=parent_obj.worldX+width/2;
        worldY=parent_obj.worldY+height/2;
        try {
            loadTextures(new String[]{"red.png", "green.png"},
                    "/game/model/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        steps=Entity.follow(target_obj.getCenter(), this);
    }

    @Override
    public void update () {
        //super.update();
        countSprites();
        move_unsafe(steps.x, steps.y);
        if (context.player.collides(this)) {
            Main.getInstance().gameOver();
        }
    }

    @Override
    public void setDefaultValues() {
        solid=true;
        width/=2;
        height/=2;
        setDefaultSpeed(10);
        rect=new Rectangle(worldX, worldY, width, height);
    }
}
