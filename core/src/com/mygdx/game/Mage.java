package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Mage extends Actor {
    Mage(float x, float y, Texture texture, MyGdxGame game) {
        super(x, y, texture, game);
    }

    @Override
    void execute() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.translateX(-10);
        } else if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
            sprite.translateX(10);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.shot(
                    sprite.getX() + (sprite.getWidth() / 2f),
                    sprite.getY() + sprite.getHeight() + 2);
        }
        for (Actor a : game.actors) {
            if (collide(a)) {
                dead = true;
                a.dead = true;
                game.explosion(sprite.getX(), sprite.getY());
                game.explosion(a.sprite.getX(), a.sprite.getY());
                game.finished = true;
            }
        }
    }
}