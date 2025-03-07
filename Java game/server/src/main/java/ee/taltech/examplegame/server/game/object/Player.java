package ee.taltech.examplegame.server.game.object;

import com.esotericsoftware.kryonet.Connection;
import constant.Constants;
import ee.taltech.examplegame.server.game.GameInstance;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.PlayerState;

import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_LIVES_COUNT;
import static constant.Constants.PLAYER_SPEED;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

/**
 * Server-side representation of a player in the game. This class listens for player movements or shooting actions
 * and changes the player's server-side state accordingly. Lives management.
 */
@Getter
@Setter
public class Player {
    private final Connection connection;
    // Keep track of listener objects for each player connection, so they can be disposed when the game ends
    private final PlayerMovementListener movementListener = new PlayerMovementListener(this);
    private final PlayerShootingListener shootingListener = new PlayerShootingListener(this);

    private final int id;
    private final GameInstance game;
    private float x = 300f, y = 0f;
    private int lives = PLAYER_LIVES_COUNT;
    private float velocityY = 0f;
    private final float gravity = 0.5f;
    private final float jumpStrength = 10f;

    private boolean isGrounded = true; // True if the player can jump

    /**
     * Initializes a new server-side representation of a Player with a game reference and connection to client-side.
     *
     * @param connection Connection to client-side.
     * @param game Game instance that this player is a part of.
     */
    public Player(Connection connection, GameInstance game) {
        this.connection = connection;
        this.id = connection.getID();
        this.game = game;
        this.connection.addListener(movementListener);
        this.connection.addListener(shootingListener);
    }



    /**
     * Moves the player in the specified direction within the arena bounds.
     *
     * @param direction The direction in which the player moves.
     */
    public void move(Direction direction) {
        if (direction == null) return;

        switch (direction) {
            case UP -> jump();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
        }

        // enforce arena bounds
        x = Math.max(ARENA_LOWER_BOUND_X, Math.min(x, ARENA_UPPER_BOUND_X - PLAYER_WIDTH_IN_PIXELS));
        y = Math.max(ARENA_LOWER_BOUND_Y, Math.min(y, ARENA_UPPER_BOUND_Y - PLAYER_HEIGHT_IN_PIXELS));

    }

    /**
     * Move left.
     */
    public void moveLeft() {
        if (isGrounded) {
            x -= 1 * PLAYER_SPEED;
        } else {
            x -= 2 * PLAYER_SPEED;
        }
    }

    /**
     * Move right.
     */
    public void moveRight() {
        if (isGrounded) {
            x += 1 * PLAYER_SPEED;
        } else {
            x += 2 * PLAYER_SPEED;
        }
    }


    /**
     * Jump.
     */
    public void jump() {
        if (isGrounded) {
            isGrounded = false;
            velocityY = jumpStrength;
        }
    }

    /**
     * Gravitation on player.
     */
    public void applyGravitation() {
        // Apply gravity
        if (!isGrounded) {
            velocityY -= gravity;
            y += velocityY;
        }

        if (y == 0f) {
            velocityY = 0;    // Reset vertical velocity
            isGrounded = true; // Allow jumping again
        }

        // Set bounds
        x = Math.max(ARENA_LOWER_BOUND_X, Math.min(x, ARENA_UPPER_BOUND_X - PLAYER_WIDTH_IN_PIXELS));
        y = Math.max(ARENA_LOWER_BOUND_Y, Math.min(y, ARENA_UPPER_BOUND_Y - PLAYER_HEIGHT_IN_PIXELS));
    }

    /**
     * Returns the current state of the player, consisting of their position and remaining lives.
     */
    public PlayerState getState() {
        PlayerState playerState = new PlayerState();
        playerState.setId(connection.getID());
        playerState.setX(x);
        playerState.setY(y);
        playerState.setLives(lives);
        return playerState;
    }

    public void shoot(Direction direction) {
        // adjust bullet spawn position to be in the center of player
        game.addBullet(
            new Bullet(x + PLAYER_WIDTH_IN_PIXELS / 2, y + PLAYER_HEIGHT_IN_PIXELS / 2, direction, id)
        );
    }

    public void decreaseLives() {
        if (lives > 0) {
            setLives(getLives() - 1);
        }
    }

    /**
     * Removes the movement and shooting listeners from the player's connection.
     * This should be called when the player disconnects or the game ends.
     * Disposing of the listeners prevents potential thread exceptions when reusing
     * same connections for future game instances.
     */
    public void dispose() {
        connection.removeListener(movementListener);
        connection.removeListener(shootingListener);
    }

}
