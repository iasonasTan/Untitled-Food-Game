package game.entity;

import main.Game;

import javax.swing.*;
import java.awt.*;

public final class Tile extends Entity {
    @Deprecated
    public Tile(Game ctx, String res, boolean solid, int worldX, int worldY) {
        super(ctx);
        this.worldX=worldX;
        this.worldY=worldY;
        this.solid=solid;
        loadImage(res);
    }

    public Tile (Game ctx, TileSource sources, int x, int y) {
        super(ctx);
        this.worldX=x;
        this.worldY=y;
        this.solid=sources.solid;
        loadImage(sources.res);
        setDefaultValues();
    }

    public enum TileSource {
        DIRT("/game/tiles/dirt.jpeg", true),
        SKY("/game/tiles/sky.png", false),
        GRASS("/game/tiles/grass.jpeg", true),
        STONE("/game/tiles/stone.jpg", true);

        public final String res;
        public final boolean solid;

        TileSource(String res, boolean solid) {
            this.res=res;
            this.solid =solid;
        }
    }

    @Override
    public void update() {
        collision.x=worldX;
        collision.y=worldY;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprites[0], worldX, worldY, width, height, null);
    }

    @Override
    public void setDefaultValues() {
        collision=new Rectangle(worldX, worldY, width, height);
    }

    public void loadImage (String res) {
        sprites=new Image[1];
        sprites[0]=new ImageIcon(getClass().getResource(res)).getImage();
    }

}
