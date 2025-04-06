package game.handler;

import game.entity.Entity;
import main.Game;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EntityManager <E extends Entity> {
    protected final List<E> entities;
    protected final Game context;
    protected final Rectangle SCREEN_EXTENDED=new Rectangle(Game.SCREEN);

    public EntityManager (Game ctx, List<E> list) {
        context=ctx;
        entities=list;
        final int GAP=Game.TILE_SIZE*2;
        SCREEN_EXTENDED.x-=GAP;
        SCREEN_EXTENDED.y-=GAP;
        SCREEN_EXTENDED.width+=2*GAP;
        SCREEN_EXTENDED.height+=2*GAP;
    }

    public final void forEach (Consumer<E> consumer) {
        entities.forEach(consumer);
    }

    public int getNearestDist(Entity other) {
        class ConsumerImpl implements Consumer<E> {
            public int minDistance=Integer.MAX_VALUE;
            @Override
            public void accept(E e) {
                int dist=other.distance(e);
                if (dist<minDistance){
                    minDistance=dist;
                }
            }
        };
        ConsumerImpl impl=new ConsumerImpl();
        forEach(impl);
        return impl.minDistance;
    }

    public final void addEntity (E e) {
        entities.add(e);
    }

    public void update () {

        entities.forEach(E::update);
    }

    public void render (Graphics g) {

        entities.forEach(e -> {
            if (SCREEN_EXTENDED.intersects(e.rect)) {
                e.render(g);
            }
        });
    }

    public Optional<E> collider(Entity ent) {
        for (E e: entities)
            if (e.collides(ent))
                return Optional.of(e);
        return Optional.empty();
    }

    public boolean collideD (Entity ent, int dist) {
        return getNearestDist(ent)>dist;
    }

    public void display() {
        System.out.println(entities.toString());
    }

    public void move(int stepsX, int stepsY) {
        forEach(e -> {
            e.move(stepsX, stepsY);
        });
    }
}
