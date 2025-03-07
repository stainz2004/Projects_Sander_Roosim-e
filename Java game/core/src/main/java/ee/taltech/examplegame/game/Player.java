package ee.taltech.examplegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.examplegame.util.Sprites;
import message.dto.Direction;

import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

public class Player {
    private final int id;
    private float x;
    private float y;
    private Direction direction = Direction.RIGHT;
    TextureRegion[][] tmp;
    TextureRegion[] frames;
    float stateTime;
    Animation<TextureRegion> animation;
    float prevX;
    float prevY;

    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;
    private static final float FRAME_DURATION = 0.05f;

    private Animation<TextureRegion> walkRightAnimation;
    private Animation<TextureRegion> walkLeftAnimation;

    public Player(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setX(float x) {
        if (x > this.x) {
            direction = Direction.RIGHT;
        } else if (x < this.x) {
            direction = Direction.LEFT;
        }
        this.x = x;
        stateTime++;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void render(SpriteBatch spriteBatch) {
        // Splitting the sprite sheet into columns and rows
        TextureRegion[][] tmp = TextureRegion.split(Sprites.testingPlayer,
            Sprites.testingPlayer.getWidth() / 8,
            Sprites.testingPlayer.getHeight() / 8);

        boolean isMoving = (x != prevX);

        // Update stateTime only if the player is moving
        if (isMoving) {
            stateTime += Gdx.graphics.getDeltaTime();
        }

        // Update previous position
        prevX = x;
        prevY = y;

        // Extract right-walking frames (row 6)
        TextureRegion[] rightFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            rightFrames[i] = tmp[6][i];
        }
        walkRightAnimation = new Animation<>(FRAME_DURATION, rightFrames);

        // Extract left-walking frames (row 2)
        TextureRegion[] leftFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            leftFrames[i] = tmp[2][i];
        }
        walkLeftAnimation = new Animation<>(FRAME_DURATION, leftFrames);

        // Use the appropriate animation based on the direction
        TextureRegion currentFrame;

        if (direction == Direction.RIGHT) {
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkLeftAnimation.getKeyFrame(stateTime, true);
        }

        // Draw the current frame
        spriteBatch.draw(currentFrame, x, y, PLAYER_WIDTH_IN_PIXELS, PLAYER_HEIGHT_IN_PIXELS);

        }
    }


