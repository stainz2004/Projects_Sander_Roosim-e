package ee.taltech.examplegame.server.game.object;

import ee.taltech.examplegame.server.game.GameInstance;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.dto.MonsterState;

import static constant.Constants.*;

@Getter
@Setter
public class Monster {

    private float x = 100;
    private float y = 100;
    private int lives = MONSTER_LIVES_COUNT;

    public Monster() {
    }


    /**
     * Returns the current state of the monster, consisting of their position and remaining lives.
     */
    public MonsterState getState() {
        MonsterState monsterState = new MonsterState();
        monsterState.setX(x);
        monsterState.setY(y);
        monsterState.setLives(lives);
        return monsterState;
    }

    public void decreaseLives() {
        if (lives > 0) {
            lives--;
        }
    }
}
