package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;

import static constant.Constants.MONSTER_WIDTH_IN_PIXELS;
import static constant.Constants.MONSTER_HEIGHT_IN_PIXELS;

public class Monster {
    private float x = 100;
    private float y = 100;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Sprites.monster, x, y, MONSTER_WIDTH_IN_PIXELS, MONSTER_HEIGHT_IN_PIXELS);
    }
}
