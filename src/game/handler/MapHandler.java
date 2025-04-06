package game.handler;

import game.entity.Enemy;
import game.entity.Tile;
import main.Game;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

public final class MapHandler extends EntityManager<Tile> {
    private final KeyHandler keyH;

    public MapHandler(Game ctx) {
        super(ctx, new ArrayList<>());
        keyH= ctx.keyHandler;
    }

    public void move (int stepsX, int stepsY) {
        forEach(tile -> {
            if (keyH.left)
                tile.move(stepsX, stepsY);
            if (keyH.right)
                tile.move(-stepsX, stepsY);
        });
    }

    @Override
    public void update () {
        super.update();
        final int SPEED=context.player.getCurrentSpeed();
        move(SPEED, 0);

        if (collider(context.player).isPresent()) {
            move(-SPEED, 0);
        }
    }

    public void loadMap (int mapID) throws Exception {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        final InputStream res_stream=getClass()
                .getResourceAsStream("/game/map/map"+mapID+".xml");
        if (res_stream==null)
            throw new IllegalArgumentException("cannot found map with given id");
        Document document = builder.parse(res_stream);
        document.getDocumentElement().normalize();

        int LEN_ORIGIN=0;
        NodeList properties_doc=document.getElementsByTagName("property");
        for (int i=0; i<properties_doc.getLength(); i++) {
            Element el=(Element)properties_doc.item(i);
            final String attr_name=el.getAttribute("name");
            if (attr_name.equals("len")) {
                LEN_ORIGIN=Integer.parseInt(el.getTextContent());
            }
        }
        if (LEN_ORIGIN<=0)
            throw new IllegalPropertiesException("map length cannot be less or equal to zero");

        int speed=-9;
        NodeList entities_doc=document.getElementsByTagName("entity");
        for (int i=0; i<entities_doc.getLength(); i++) {
            Element element=(Element)entities_doc.item(i);
            if (element.getAttribute("type").equals("player")) {
                NodeList properties_nl=element.getElementsByTagName("property");
                for (int j=0; j<properties_nl.getLength(); j++) {
                    Element property_xml=(Element)properties_nl.item(j);
                    if (property_xml.getAttribute("name").equals("speed")) {
                        speed=Integer.parseInt(property_xml.getTextContent());
                    }
                }
            }
            if (element.getAttribute("type").equals("enemy")) {
                NodeList enemy_properties_doc=element.getElementsByTagName("property");
                for (int j=0; j<enemy_properties_doc.getLength(); j++) {
                    final Element property=(Element)enemy_properties_doc.item(j);
                    if (property.getAttribute("name").equals("pos")) {
                        final int entity_x=Integer.parseInt(property.getTextContent())*Game.TILE_SIZE;
                        context.enemyM.addEntity(new Enemy(context,
                                new Point(entity_x, 120)));
                    }
                }
            }
        }
        context.player.setDefaultSpeed(speed);
        if (speed<=0)
            throw new IllegalPropertiesException("player speed can't be less or equal to zero");

        final int HEIGHT=20;
        int len=LEN_ORIGIN;
        int tx=0;
        int ty=0;
        do {
            Tile tile;
            if (ty > 11 * Game.TILE_SIZE) {
                // add solid tile
                tile=new Tile(context, Tile.TileSource.random(true, false), tx, ty);
                addEntity(tile);
            } else if (ty>9*Game.TILE_SIZE) {
                // add random tile
                tile = Math.random()*100>18?
                        new Tile(context,
                                ty==10*Game.TILE_SIZE?
                                        Tile.TileSource.GRASS:
                                        Tile.TileSource.DIRT
                                ,tx,ty):
                        new Tile(context, Tile.TileSource.WATER, tx, ty);
                addEntity(tile);
            }

            tx += Game.TILE_SIZE;
            len--;
            if (len <= 0) {
                tx = 0;
                ty += Game.TILE_SIZE;
                len = LEN_ORIGIN;
            }
        } while (ty <= HEIGHT * Game.TILE_SIZE);

    }

    public static class IllegalPropertiesException extends RuntimeException {
        public IllegalPropertiesException (String msg) {
            super(msg);
        }

    };

}
