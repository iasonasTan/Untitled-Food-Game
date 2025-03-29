package game.manager;

import game.entity.EntityManager;
import game.entity.Tile;
import main.Game;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.LinkedList;

public class TileManager extends EntityManager<Tile> {
    private final KeyHandler keyH;

    public TileManager(Game ctx) {
        super(ctx, new LinkedList<>());
        keyH= ctx.keyHandler;
    }

    @Override
    public void update () {
        super.update();
        final int SPEED=context.player.getCurrentSpeed();
        forEach(tile -> {
            if (keyH.left) {
                tile.move(SPEED, 0);
            }
            if (keyH.right) {
                tile.move(-SPEED, 0);
            }
        });

        if (collider(context.player).isPresent()) {
            forEach(tile -> {
                if (keyH.left) {
                    tile.move(0, SPEED);
                }
                if (keyH.right) {
                    tile.move(0, -SPEED);
                }
            });
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
        NodeList list=document.getElementsByTagName("property");
        for (int i=0; i<list.getLength(); i++) {
            Element el=(Element)list.item(i);
            final String attr_name=el.getAttribute("name");
            if (attr_name.equals("len")) {
                LEN_ORIGIN=Integer.parseInt(el.getTextContent());
            }
        }

        int speed=-9;
        NodeList entities_nl=document.getElementsByTagName("entity");
        for (int i=0; i<entities_nl.getLength(); i++) {
            Element element=(Element)entities_nl.item(i);
            if (element.getAttribute("type").equals("player")) {
                NodeList properties_nl=element.getElementsByTagName("property");
                for (int j=0; j<properties_nl.getLength(); j++) {
                    Element property_xml=(Element)properties_nl.item(j);
                    if (property_xml.getAttribute("name").equals("speed")) {
                        speed=Integer.parseInt(property_xml.getTextContent());
                    }
                }
            }
        }
        context.player.setDefaultSpeed(speed);

        final int HEIGHT=20;
        int len=LEN_ORIGIN;
        int tx=0;
        int ty=0;
        do {
            Tile tile;
            if (ty < 10 * Game.TILE_SIZE) {
                // add blank tile
                tile = new Tile(context, Tile.TileSource.SKY, tx, ty);
            } else if (ty > 11 * Game.TILE_SIZE) {
                tile = new Tile(context, Tile.TileSource.DIRT, tx, ty);
            } else {
                // add random tile
                tile = ((int) (Math.random() * 100) > 35) ?
                        new Tile(context, Tile.TileSource.DIRT, tx, ty):
                        new Tile(context, Tile.TileSource.SKY, tx, ty);
            }
            addEntity(tile);

            tx += Game.TILE_SIZE;
            len--;
            if (len <= 0) {
                tx = 0;
                ty += Game.TILE_SIZE;
                len = LEN_ORIGIN;
            }
        } while (ty <= HEIGHT * Game.TILE_SIZE);

    }

}
