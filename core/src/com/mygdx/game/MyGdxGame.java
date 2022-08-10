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
    int score;
    boolean pause = false;
    boolean isPlaying = true;
    boolean finished = false;

    public MyGdxGame(final Start game) {
        this.game = game;
		//set initial state
		//state = State.RUN;

        // load the images for the droplet and the bucket, 64x64 pixels each
        //dropImage = new Texture(Gdx.files.internal("droplet.png"));
        //bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        //
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        //
        background = new Texture("background.jpg");
        textureMage = new Texture("mage.png");
        textureMonster = new Texture("monster.png");
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

        //create the Camera and the SpriteBatch
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
        clean();
    }


    @Override
    public void render(float delta) {
        execute();
		/*if (Gdx.input.isKeyPressed(Input.Keys.P))
			pause();
		if (Gdx.input.isKeyPressed(Input.Keys.R))
			resume();*/

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		/*batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
        font.draw(batch, String.valueOf(count_raindrops), 10, 470);*/
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

		/*switch (state) {
            case RUN:
            	//check mouse input
                if (Gdx.input.isTouched()) {
                    Vector3 touchPos = new Vector3();
                    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(touchPos);
                    bucket.x = touchPos.x - 64 / 2;
                }
                //check keyboard input
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    bucket.x -= 200 * Gdx.graphics.getDeltaTime();
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    bucket.x += 200 * Gdx.graphics.getDeltaTime();
                //check screen limits
                if (bucket.x < 0)
                    bucket.x = 0;
                if (bucket.x > 800 - 64)
                    bucket.x = 800 - 64;
                //check time to create another raindrop
                if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
                    spawnRaindrop();
                //move raindrops created
                for (Iterator<Rectangle> it = raindrops.iterator(); it.hasNext(); ) {
                    Rectangle raindrop = it.next();
                    raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
                    //check if it is beyond the screen
                    if (raindrop.y + 64 < 0)
                        it.remove();
                    //check collision between bucket and raindrops
                    if (raindrop.overlaps(bucket)) {
                        count_raindrops++;
                        dropSound.play();
                        it.remove();
                    }
                }
                break;
            case PAUSE:
            	batch.begin();
				font.draw(batch, "PAUSED", 380, 250);
                batch.end();
				break;
        }*/


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
        // start the playback of the background music
        // when the screen is shown
        //rainMusic.play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        //this.state = State.PAUSE;
    }

    @Override
    public void resume() {
        //this.state = State.RUN;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        /*dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();*/
        background.dispose();
        batch.dispose();
    }

    /*public enum State {
        PAUSE,
        RUN,
    }*/
}