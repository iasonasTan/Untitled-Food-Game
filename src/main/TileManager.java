package main;

import entity.EntityManager;
import entity.Tile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Random;

public class TileManager extends EntityManager<Tile> {
    private Tile[][] map;

    public TileManager(Game ctx) {
        super(ctx);
    }

    public void loadMap (int mapID) throws Exception {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        final InputStream res_stream=getClass()
                .getResourceAsStream("/map/map"+mapID+".xml");
        Document document = builder.parse(res_stream);
        document.getDocumentElement().normalize();

        int len=0;
        NodeList list=document.getElementsByTagName("property");
        for (int i=0; i<list.getLength(); i++) {
            Element el=(Element)list.item(i);
            if (el.getAttribute("name").equals("len")) {
                len=Integer.parseInt(el.getTextContent());
            }
        }

        final int HEIGHT= (Main.FRAME_SIZE.height/Game.TILE_SIZE)+5;
        map=new Tile[HEIGHT][len]; // height // width
        int tx=0;
        int ty=0;
        for (int y=0; y<HEIGHT; y++) {
            for (int x=0; x<len; x++) {

                if (y<10) {
                    // add blank tile
                    map[y][x]=new Tile(context, "/tiles/green.png", Tile.Type.NON_SOLID, tx, ty);
                } else if (y>10&&y<13) {
                    // add random tile
                    map[y][x]=((int)(Math.random()*100)>50)?
                            new Tile(context, "/tiles/red.png", Tile.Type.SOLID, tx, ty):
                            new Tile(context, "/tiles/green.png", Tile.Type.NON_SOLID, tx, ty);
                } else {
                    map[y][x]=new Tile(context, "/tiles/red.png", Tile.Type.SOLID, tx, ty);
                }

                tx+=Game.TILE_SIZE;
            }
            ty+=Game.TILE_SIZE;
        }

    }

}
