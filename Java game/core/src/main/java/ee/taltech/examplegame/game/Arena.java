package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;
import message.dto.BulletState;
import message.GameStateMessage;
import message.dto.MonsterState;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.BULLET_HEIGHT_IN_PIXELS;
import static constant.Constants.BULLET_WIDTH_IN_PIXELS;
import static ee.taltech.examplegame.util.Sprites.taltechMapTexture;
import static ee.taltech.examplegame.util.Sprites.monster;


/**
 * Initialize a new Arena, which is responsible for updating and rendering the following: players, bullets, map.
 * Updating - modifying the inner state of objects (e.g. Player) based on game state messages received from the server.
 * Rendering - making the (updated) objects visible on the screen.
 */
public class Arena {

    private final List<Player> players = new ArrayList<>();
    private List<BulletState> bullets = new ArrayList<>();
    private Monster monsterRender = new Monster();
    private Monster monster = new Monster();

    /**
     * Update players and bullets, so they are later rendered in the correct position.
     *
     * @param gameStateMessage latest game state received from the server
     */
    public void update(GameStateMessage gameStateMessage) {
        gameStateMessage.getPlayerStates().forEach(playerState -> {
            var player = players
                .stream()
                .filter(p -> p.getId() == playerState.getId())
                .findFirst()
                .orElseGet(() -> {
                    var newPlayer = new Player(playerState.getId());
                    players.add(newPlayer);
                    return newPlayer;
                });

            player.setX(playerState.getX());
            player.setY(playerState.getY());
        });
        // Client-side: receiving the monster state and updating the position
        var monsterState = gameStateMessage.getMonsterState();  // assuming this is the method to get the state from the message
        monster.setX(monsterState.getX());
        monster.setY(monsterState.getY());



        this.bullets = gameStateMessage.getBulletStates();
    }

    /**
     * Render map, players and bullets. This makes them visible on the screen.
     *
     * @param spriteBatch used for rendering (and scaling/resizing) all visual elements
     */
    public void render(SpriteBatch spriteBatch) {
        renderMap(spriteBatch);
        renderPlayers(spriteBatch);
        renderBullets(spriteBatch);
        renderMonster(spriteBatch);
    }

    private void renderMap(SpriteBatch spriteBatch) {
        //spriteBatch.draw(taltechMapTexture, 0, 0, ARENA_UPPER_BOUND_X, ARENA_UPPER_BOUND_Y);
    }

    private void renderMonster(SpriteBatch spriteBatch) {
        monsterRender.render(spriteBatch);
    }

    private void renderPlayers(SpriteBatch spriteBatch) {
        players.forEach(player -> player.render(spriteBatch));
    }

    private void renderBullets(SpriteBatch spriteBatch) {
        bullets.forEach(bullet -> {
            spriteBatch.draw(
                Sprites.bulletTexture,
                bullet.getX(),
                bullet.getY(),
                BULLET_WIDTH_IN_PIXELS,
                BULLET_HEIGHT_IN_PIXELS
            );
        });
    }

}
