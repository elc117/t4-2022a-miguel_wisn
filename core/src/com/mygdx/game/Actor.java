package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;

public class Actor {
    MyGdxGame game;
    Sprite sprite;
    boolean dead = false;

    Actor(float x, float y, Texture texture, MyGdxGame game) {
        this.game = game;
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
    }

    void execute() {
        
    }

    void run() {
        if (game.pause) return;
        execute();
        sprite.setX(clamp(sprite.getX(), 0, game.w - sprite.getWidth()));
    }

    void draw() {
        sprite.draw(game.batch);
    }

    static float clamp(float value, float min, float max) {
        //check screen limits
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    boolean collide(Actor other) {
        if (this == other) return false;
        return Intersector.overlaps(
                sprite.getBoundingRectangle(),
                other.sprite.getBoundingRectangle());
    }
}