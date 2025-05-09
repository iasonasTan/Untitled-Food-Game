package game.handler;

import game.model.Entity;
import main.Game;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EntityManager <E extends Entity> implements Iterable<E> {
    protected final List<E> entities;
    protected final Game context;
    protected final Rectangle SCREEN_EXTENDED=new Rectangle(Game.SCREEN);

    public EntityManager (Game ctx, List<E> list_impl) {
        context=ctx;
        entities=list_impl;
        final int GAP=Game.DEFAULT_SIZE *2;
        SCREEN_EXTENDED.x-=GAP;
        SCREEN_EXTENDED.y-=GAP;
        SCREEN_EXTENDED.width+=2*GAP;
        SCREEN_EXTENDED.height+=2*GAP;
    }

    public final void addEntity (E e) {
        if (e!=null)
            entities.add(e);
    }

    public void update () {
        // move entities
        final int playerSpeed=context.player.getCurrentSpeed();
        if (context.keyHandler.left) {
            move(playerSpeed, 0);
        } else if (context.keyHandler.right) {
            move(-playerSpeed, 0);
        }

        entities.forEach(E::update);
    }

    public void render (Graphics g) {
        entities.forEach(e -> {
            if (SCREEN_EXTENDED.intersects(e.getRect()))
                e.render(g);
        });
    }

    public boolean isCollider(Entity entity, Entity.Side requestedSide) {
        for (E e: entities) {
            final Entity.Side side= Entity.Side.opposite(e.getCollideSide(entity));
            if (side==requestedSide)
                return true;
        }
        return false;
    }

    public Entity.Side getCollideSide(Entity entity) {
        for (E e: entities) {
            final Entity.Side side=e.getCollideSide(entity);
            if (side!=Entity.Side.NONE)
                return side;
        }
        return Entity.Side.NONE;
    }

    public Optional<E> collider(Entity ent) {
        for (E e: entities)
            if (e.getCollideSide(ent)!=Entity.Side.NONE)
                return Optional.of(e);
        return Optional.empty();
    }

    public void move(int stepsX, int stepsY) {
        forEach(e -> e.move(stepsX, stepsY));
    }

    @Override
    public Iterator<E> iterator() {
        return entities.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        entities.forEach(action);
    }
}
