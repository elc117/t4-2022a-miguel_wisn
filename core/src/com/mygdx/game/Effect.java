package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.List;

public class Effect extends Actor {
    int position = 0;
    int counter = 0;
    int max = 3;
    List<Texture> textures;
    Sprite sprite;

    Effect(float x, float y, List<Texture> textures, MyGdxGame game) {
        super(x, y, textures.get(0), game);
        this.textures = textures;
        sprite = new Sprite(textures.get(position));
        sprite.setPosition(x, y);
        game.soundEffect.play();
    }

    @Override
    void execute() {
        counter++;
        if (counter > max) {
            position++;
            counter = 0;
        }
        if (position >= textures.size()) {
            dead = true;
        }
    }

    @Override
    void draw() {
        if (dead) return;
        sprite.setTexture(textures.get(position));
        sprite.draw(game.batch);
    }
}