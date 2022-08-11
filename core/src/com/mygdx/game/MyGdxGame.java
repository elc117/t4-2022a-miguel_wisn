package com.mygdx.game;

//import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.graphics.Color;

//import java.util.Iterator;

public class MyGdxGame implements Screen {

    private final Start game;
    private OrthographicCamera camera;
    SpriteBatch batch;
    //private State state;
	private BitmapFont font;
    Sound soundMagic;
    Sound soundEffect;
    Music music;
    Texture background;
    Texture textureMage;
    Texture textureMonster;
    Texture textureSoldier;
    Texture textureMagic;
    Texture texturePause;
    Texture textureGameOver;
    List<Texture> textureEffects = new ArrayList<>();
    Background bg;
    List<Actor> actors = new ArrayList<>();
    List<Actor> news = new ArrayList<>();
    int w, h;
    Random rand = new Random();
    int monsterCount = 0;
    int monsterMax = 50;
    int soldierCount = 0;
    int soldierMax = 50;
    int score;
    boolean pause = false;
    boolean isPlaying = true;
    boolean finished = false;

    public MyGdxGame(final Start game) {
        this.game = game;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        background = new Texture("background.jpg");
        textureMage = new Texture("mage.png");
        textureMonster = new Texture("monster.png");
        textureSoldier = new Texture("soldier.png");
        textureMagic = new Texture("magic.png");
        texturePause = new Texture("paused.png");
        textureGameOver = new Texture("gameover.png");
        textureEffects.add(new Texture("effect_0.png"));
        textureEffects.add(new Texture("effect_1.png"));
        textureEffects.add(new Texture("effect_2.png"));
        textureEffects.add(new Texture("effect_1.png"));
        textureEffects.add(new Texture("effect_0.png"));
        bg = new Background(background, this);
        actors.add(new Mage(
                w / 2f - textureMage.getWidth() / 2f, 10,
                textureMage, this));
        font = new BitmapFont(
                Gdx.files.internal("verdana.fnt"),
                Gdx.files.internal("verdana.png"), false);
        font.setColor(Color.BLACK);
        soundMagic = Gdx.audio.newSound(Gdx.files.internal("magic.wav"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("effect.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
        music.setLooping(true);
        music.play();
        //create the Camera and the SpriteBatch (comentário próprio do código extendend-drop-game, disponibilziado pela professora)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();
    }

    public void execute() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (music.isPlaying()) {
                music.stop();
                isPlaying = false;
            } else {
                music.play();
                isPlaying = true;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = !pause;
            if (pause) {
                music.stop();
            } else if (isPlaying) {
                music.play();
            }
        }
        if (finished) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                finished = false;
                actors.clear();
                actors.add(new Mage(
                        w / 2f - textureMage.getWidth() / 2f, 10,
                        textureMage, this));
                score = 0;            }
        }
        bg.run();
        for (Actor a : actors) a.run();
        monsters();
        soldiers();
        clean();
    }


    @Override
    public void render(float delta) {
        execute();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // tell the camera to update its matrices. (comentário próprio do código extendend-drop-game, disponibilziado pela professora)
        camera.update();
        // tell the SpriteBatch to render in then (comentário próprio do código extendend-drop-game, disponibilziado pela professora)
        // coordinate system specified by the camera. (comentário próprio do código extendend-drop-game, disponibilziado pela professora)
        game.batch.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
        bg.draw(batch);
        for (Actor a : actors) a.draw();
        font.draw(batch, "SCORE: " + score, 1, h);
        if (pause) {
            batch.draw(texturePause,
                    w / 2f - texturePause.getWidth() / 2f, h / 3f);
        }
        if (finished) {
            batch.draw(textureGameOver,
                    w / 2f - textureGameOver.getWidth() / 2f, h / 3f);
        }
        batch.end();


    }
    void monsters() {
        if (pause) return;
        monsterCount++;
        if (monsterCount > monsterMax) {
            actors.add(new Monster(
                    rand.nextInt(w - textureMonster.getWidth()), h + 50,
                    textureMonster, this));
            monsterCount = 0;
            monsterMax = 30 + rand.nextInt(50);
        }
    }

    void soldiers() {
        if (pause) return;
        soldierCount++;
        if (soldierCount > soldierMax) {
            actors.add(new Soldier(
                    rand.nextInt(w - textureSoldier.getWidth()), h + 50,
                    textureSoldier, this));
            soldierCount = 0;
            soldierMax = 30 + rand.nextInt(50);
        }
    }

    void shot(float x, float y) {
        news.add(new Magic(x - (textureMagic.getWidth() / 2f), y, textureMagic, this));
    }

    void explosion(float x, float y) {
        news.add(new Effect(x, y, textureEffects, this));
    }

    void clean() {
        List<Actor> aux = actors;
        actors = new ArrayList<>();
        for (Actor a : aux) if (!a.dead) actors.add(a);
        actors.addAll(news);
        news.clear();
    }


    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
    }

}