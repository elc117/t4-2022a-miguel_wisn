package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Monster extends Actor {
    static final int RIGHT = 0;
    static final int LEFT = 1;
    int direction;

    Monster(float x, float y, Texture texture, MyGdxGame game) {
        super(x, y, texture, game);
        direction = game.rand.nextInt(2);
    }

    @Override
    void execute() {
        sprite.translateY(-10);
        if (direction == RIGHT) {
            sprite.translateX(10);
            if (sprite.getX() + sprite.getWidth() > game.w) {
                direction = LEFT;
            }
        } else {
            sprite.translateX(-10);
            if (sprite.getX() < 0) {
                direction = RIGHT;
            }
        }
        if (sprite.getY() + sprite.getHeight() < 0) {
            dead = true;
        }
    }
}