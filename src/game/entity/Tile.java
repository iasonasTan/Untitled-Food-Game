package game.entity;

import main.Game;

import javax.swing.*;
import java.awt.*;

public final class Tile extends Entity {
    private TileSource source;

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
        this.source=sources;
        this.worldX=x;
        this.worldY=y;
        this.solid=sources.solid;
        loadImage(sources.res);
        setDefaultValues();
    }

    public enum TileSource {
        DIRT("/game/tiles/dirt.jpeg", true, false),
        AIR("/game/tiles/sky.png", false, true),
        GRASS("/game/tiles/grass.jpeg", true, true),
        STONE("/game/tiles/stone.jpg", true, false),
        WATER("/game/tiles/water.jpeg", false, true);

        public static TileSource random (boolean solid, boolean topLayer) {
            int random_idx=(int)(Math.random()*TileSource.values().length);
            TileSource out;
            do {
                out=TileSource.values()[random_idx];
                random_idx++;
                if (random_idx>=TileSource.values().length)
                    random_idx=0;

            } while (out.solid!=solid||
                    out.topLayer!=topLayer);
            return out;
        }

        public final String res;
        public final boolean topLayer;
        public final boolean solid;

        TileSource(String res, boolean solid, boolean topLayer) {
            this.res=res;
            this.solid =solid;
            this.topLayer=topLayer;
        }
    }

    @Override
    public void update() {
        rect.x=worldX;
        rect.y=worldY;
    }

    @Override
    public void setDefaultValues() {
        rect =new Rectangle(worldX, worldY, width, height);
    }

    public void loadImage (String res) {
        sprites=new Image[1];
        sprites[0]=new ImageIcon(getClass().getResource(res)).getImage();
    }

    public TileSource getSource () {
        return source;
    }

}
