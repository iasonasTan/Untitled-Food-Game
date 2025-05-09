package game.handler;

import game.model.Enemy;
import game.model.Entity;
import game.model.Tile;
import main.Game;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public final class MapHandler extends EntityManager<Tile> {
    public static final int FLOOR=11;
    public static final int MAPS;

    static {
        try {
            File file=new File(MapHandler.class.getResource("/game/map").toURI());
            MAPS= Objects.requireNonNull(file.listFiles()).length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MapHandler(Game ctx) {
        super(ctx, new ArrayList<>());
    }

    @Override
    public void move(int stepsX, int stepsY) {
        forEach(e -> e.move(stepsX, stepsY));
        Optional<Tile> c_opt=collider(context.player);
        if (c_opt.isPresent()) {
            Entity collider=c_opt.get();
            Entity.Side sd=context.player.getCollideSide(collider);
            switch(sd) {
                case RIGHT -> {
                    int d=Math.abs(collider.getWorldX()-context.player.getWidth()-context.player.getWorldX())-8;
                    forEach(e -> e.move(d, 0));
                }
                case LEFT -> {
                    int d=Math.abs(collider.getWorldX()+collider.getWidth()-context.player.getWorldX())+8;
                    forEach(e -> e.move(-d, 0));
                }
            }
        }
    }

    public Tile getLeftTopMapTile () {
        Tile out=entities.getFirst();
        for (Tile e : entities) {
            if (e.getWorldX()<out.getWorldX()&&e.getWorldY()
                    >=FLOOR*Game.DEFAULT_SIZE -Game.DEFAULT_SIZE *2) {

                out=e;
            }
        }
        return out;
    }

    public Tile getRightTopMapTile () {
        Tile out=entities.getFirst();
        for (Tile e : entities) {
            if (e.getWorldX()>out.getWorldX()&&e.getWorldY()
                    >=FLOOR*Game.DEFAULT_SIZE -Game.DEFAULT_SIZE *2) {

                out=e;
            }
        }
        return out;
    }

    public void loadMap (int mapID) throws Exception {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        final InputStream res_stream=getClass()
                .getResourceAsStream("/game/map/map"+mapID+".xml");
        if (res_stream==null)
            throw new IllegalArgumentException("cannot found map with given id "+mapID);
        Document document = builder.parse(res_stream);
        document.getDocumentElement().normalize();

        int MAP_LEN_ORIGIN = 0;
        NodeList properties_doc=document.getElementsByTagName("property");
        for (int i=0; i<properties_doc.getLength(); i++) {
            Element el=(Element)properties_doc.item(i);
            final String attr_name=el.getAttribute("name");
            if (attr_name.equals("len")) {
                MAP_LEN_ORIGIN =Integer.parseInt(el.getTextContent());
            }
        }
        if (MAP_LEN_ORIGIN <=0)
            throw new IllegalMapException("map length cannot be less or equal to zero");

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
                        final int entity_x=Integer.parseInt(property.getTextContent())*Game.DEFAULT_SIZE;
                        context.enemiesMan.addEntity(new Enemy(context,
                                new Point(entity_x, 120)));
                    }
                }
            }
        }
        context.player.setDefaultSpeed(speed);
        if (speed<=0)
            throw new IllegalMapException("player speed can't be equal to or less than zero");

        final int HEIGHT=20;
        int len= MAP_LEN_ORIGIN;
        int tx=0;
        int ty=0;
        do {
            Tile tile=null;
            if (tx> MAP_LEN_ORIGIN *Game.DEFAULT_SIZE -Game.DEFAULT_SIZE *4&&
                tx< MAP_LEN_ORIGIN *Game.DEFAULT_SIZE -Game.DEFAULT_SIZE &&
                ty>FLOOR*Game.DEFAULT_SIZE -Game.DEFAULT_SIZE *2) {

                // add portal
                tile=new Tile(context, Tile.TileSource.PORTAL, tx, ty);

            }  else if (ty > FLOOR * Game.DEFAULT_SIZE) {
                // add random solid tile
                tile=new Tile(context, Tile.TileSource.random(true, false), tx, ty);

            } else if (ty>9*Game.DEFAULT_SIZE) {
                // add random tile
                tile = Math.random()*100>40?
                        new Tile(context,
                                ty==10*Game.DEFAULT_SIZE ?
                                        Tile.TileSource.GRASS:
                                        Tile.TileSource.DIRT
                                , tx, ty):
                        new Tile(context, Tile.TileSource.WATER, tx, ty);
            }
            addEntity(tile);

            tx += Game.DEFAULT_SIZE;
            len--;
            if (len <= 0) {
                tx = 0;
                ty += Game.DEFAULT_SIZE;
                len = MAP_LEN_ORIGIN;
            }

        } while (ty <= HEIGHT * Game.DEFAULT_SIZE);

        NodeList entities=document.getElementsByTagName("entity");
        for (int i=0; i< entities.getLength(); i++) {
            Element element=(Element)entities.item(i);
            if (element.getAttribute("type").equals("structure")) {
                NodeList structureProperties=element.getElementsByTagName("property");
                int pos=0;
                int width=0, height=0;
                for (int j=0; j<structureProperties.getLength(); j++) {
                    // read each property
                    Element prop= (Element) structureProperties.item(j);
                    final int VAL=Integer.parseInt(prop.getTextContent());
                    final String name=prop.getAttribute("name");
                    if (name.equals("pos"))
                        pos=VAL;
                    if (name.equals("width"))
                        width=VAL;
                    if (name.equals("height"))
                        height=VAL;
                }
                addStructure(pos, width, height);
            }
        }

    }

    private void addStructure(int pos, int width, int height) {
        int spaces=width/2;
        final int ABS_POS=pos*Game.DEFAULT_SIZE;
        for (int i=0; i<height; i++) {
            final int ABS_WIDTH=i*Game.DEFAULT_SIZE;
            for (int j=0; j<width-spaces*2; j++) {
                final int ABS_HEIGHT=j*Game.DEFAULT_SIZE;
                addEntity(new Tile(context, Tile.TileSource.STONE,
                        ABS_POS+ABS_WIDTH, ABS_HEIGHT));
            }
            spaces--;
        }
    }

    public boolean above(Entity ent) {
        int originalWorldY=ent.getWorldY();
        ent.move(0, -ent.getHeight());
        boolean collides=collider(ent).isPresent();
        ent.setWorldY(originalWorldY);
        return collides;
    }

    public static class IllegalMapException extends RuntimeException {
        public IllegalMapException(String msg) {
            super(msg);
        }

    }

}
