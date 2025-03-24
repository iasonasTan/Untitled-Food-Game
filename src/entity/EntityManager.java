package entity;

import main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class EntityManager <E extends Entity> {
    private final List<E> entities=new ArrayList<>();
    protected final Game context;

    public EntityManager (Game ctx) {
        context=ctx;
    }

    public final void forEach (Consumer<E> consumer) {
        entities.forEach(consumer);
    }

    public final void addEntity (E e) {
        entities.add(e);
    }

    public void update () {
        entities.forEach(E::update);
    }

    public void render (Graphics g) {
        entities.forEach(e -> {
            e.render(g);
        });
    }

    public Optional<E> collider (Entity ent) {
        for (E e: entities)
            if (e.collides(ent))
                return Optional.of(e);
        return Optional.empty();
    }

}
