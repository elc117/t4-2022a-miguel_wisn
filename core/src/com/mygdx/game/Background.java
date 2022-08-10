package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    MyGdxGame game;
    Texture texture;
    int xx, xy;

    Background(Texture texture, MyGdxGame game) {
        this.game = game;
        this.texture = texture;
        xx = 0;
        xy = texture.getHeight();
    }

    void run() {
        if (game.pause) return;
        xx -= 2;
        xy -= 2;
        if (xy <= -texture.getHeight()) {
            xx = xy + texture.getHeight();
        }
        if (xy <= -texture.getHeight()) {
            xy = xx + texture.getHeight();
        }
    }

    void draw(SpriteBatch batch) {
        batch.draw(texture, 0, xx);
        batch.draw(texture, 0, xy);
    }
}