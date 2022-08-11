package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Soldier extends Actor {
    static final int RIGHT = 0;
    static final int LEFT = 1;
    int direction;

    Soldier(float x, float y, Texture texture, MyGdxGame game) {
        super(x, y, texture, game);
        direction = game.rand.nextInt(2);
    }

    @Override
    void execute() {
        sprite.translateY(-15);
       
        if (sprite.getY() + sprite.getHeight() < 0) {
            dead = true;
        }
    }
}