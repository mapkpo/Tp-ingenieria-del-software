import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * @brief The `Game` class represents the main game logic and functionality.
 *
 * It extends the `Canvas` class and implements the `Runnable` and `KeyListener` interfaces.
 * The game is responsible for rendering the game objects, handling user input, and updating the game state.
 */
public class Game extends Canvas implements Runnable, KeyListener, ScoreObserver {

    boolean isRunning = false;                                  /**< @brief Indicates if the game is currently running. */
    boolean isPaused = false;                                   /**< @brief Indicates if the game is currently paused. */

    private BufferStrategy bs;                                  /**< @brief Buffer strategy for rendering. */
    private BufferedImage image;                                /**< @brief Image buffer for rendering. */

    public static Player player;                                /**< @brief Player object. */
    public static Enemy enemy;                                  /**< @brief Enemy object. */
    public static Ball ball;                                    /**< @brief Ball object. */

    public static final int WIDTH = 240;                        /**< @brief Width of the game window. */
    public static final int HEIGHT = 160;                       /**< @brief Height of the game window. */
    private final int SCALE = 4;                                /**< @brief Scale factor for the game window. */

    private final int SPEED = 4;                                /**< @brief Speed of the game. */
    private final int FPS = 60;                                 /**< @brief Frames per second. */
    private final long ns = 1000000000 / FPS;                   /**< @brief Time per frame in nanoseconds. */
    private long lastTime = System.nanoTime();                  /**< @brief Last time the game loop was updated. */

    public boolean rightPressedPlayer = false;                  /**< @brief Indicates if the right arrow key is pressed. */
    public boolean leftPressedPlayer = false;                   /**< @brief Indicates if the left arrow key is pressed. */

    private int playerScore = 0;                                /**< @brief Score of the player. */
    private int enemyScore = 0;                                 /**< @brief Score of the enemy. */

    private String[] menuOptions = {"Resume", "Reset", "Exit"}; /**< @brief Options in the pause menu. */
    private int selectedOption = 0;                             /**< @brief Index of the currently selected menu option. */

    /**
     * @brief The main entry point of the game.
     * 
     * Creates a new instance of the Game class, starts a new thread for the game loop,
     * and initializes the game frame.
     * 
     * @param args The command-line arguments.
     * @return void
     */
    public static void main(String[] args) {
        Game game = new Game();
        Thread thread = new Thread(game);
        JFrame frame = new JFrame();

        game.start();
        game.startFrame(frame);
        thread.start();
    }

    /**
     * @brief Constructs a new instance of the Game class.
     * 
     * Initializes the game window and creates a new image buffer for rendering.
     * The game window size is set to the specified width and height multiplied by the scale factor.
     */
    Game() {
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.addKeyListener(this);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * @brief Sets up the game.
     * 
     * Starts the game by initializing the player, enemy, and ball objects.
     * This method should be called before any game logic is executed.
     * 
     * @return void
     */
    public synchronized void start() {
        player = new Player(100, 155);
        enemy = new Enemy(100, 0);
        ball = new Ball();
        ball.addObserver(this);
        isRunning = true;
    }

    /**
     * @brief Sets the game panel.
     * 
     * Adds the game panel to the specified JFrame, configures the frame, and makes it visible.
     * 
     * @param frame the JFrame to start the game in.
     * @return void
     */
    public void startFrame(JFrame frame) {
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * @brief Updates the game state for each frame.
     * 
     * If the right arrow key is pressed, the player moves to the right.
     * If the left arrow key is pressed, the player moves to the left.
     * If the game is paused, the game state is not updated.
     * 
     * @return void
     */
    public void tick() {
        if (isPaused) {
            return;
        }

        if (rightPressedPlayer) {
            player.rightPressed();
        } else if (leftPressedPlayer) {
            player.leftPressed();
        }
    }

    /**
     * @brief Renders the game by drawing the game elements on the screen.
     * 
     * If the game is paused, the pause menu is rendered.
     * Otherwise, the player, enemy, ball, and game score are rendered on the screen.
     * 
     * @return void
     */
    public void render() {
        bs = this.getBufferStrategy();

        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = image.getGraphics();

        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (isPaused) {
            renderPauseMenu(g);
        } else {
            player.render(g);
            ball.render(g);
            enemy.render(g);
            
            g.drawString(String.valueOf(playerScore), WIDTH / 2 - 5, HEIGHT / 2 + 15);
            g.drawString(String.valueOf(enemyScore), WIDTH / 2 - 5, HEIGHT / 2 - 5);
            
            g.setColor(Color.WHITE);
            for (int x = 0; x < WIDTH; x += 10) {
                g.fillRect(x, HEIGHT / 2, 5, 1);
            }
        }

        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        bs.show();
    }

    /**
     * @brief Renders the pause menu on the screen.
     * 
     * Draws the pause menu with the available options on the screen.
     * The selected option is highlighted in red.
     * 
     * @param g The graphics context to render the menu on.
     * @return void
     */
    private void renderPauseMenu(Graphics g) {
        int menuWidth = 180;
        int menuHeight = 90;
        int menuX = (WIDTH - menuWidth) / 2;
        int menuY = (HEIGHT - menuHeight) / 2;

        g.setColor(Color.BLACK);
        g.fillRect(menuX, menuY, menuWidth, menuHeight);

        g.setColor(Color.WHITE);
        g.drawRect(menuX, menuY, menuWidth, menuHeight); // Add white border
        g.setFont(new Font("Arial", Font.BOLD, 16));

        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getAscent();

        int verticalSpacing = 5; // Adjust spacing between options

        for (int i = 0; i < menuOptions.length; i++) {
            int textWidth = fm.stringWidth(menuOptions[i]);
            int textX = menuX + (menuWidth - textWidth) / 2;
            int textY = menuY + menuHeight / 2 - ((menuOptions.length - 1) * textHeight / 2) + i * (textHeight + verticalSpacing);

            if (i == selectedOption) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }

            g.drawString(menuOptions[i], textX, textY);
        }
    }

    /**
     * @brief Main game loop.
     * 
     * Runs the game loop, continuously updating and rendering the game.
     * 
     * @return void
     */
    public void run() {
        while (isRunning) {
            this.requestFocus();
            long now = System.nanoTime();

            if (now - lastTime >= ns / SPEED) {
                tick();
                render();
                lastTime = now;
            }
        }
    }

    /**
     * @brief Handles the game logic when the ball collides with the player or enemy.
     * 
     * If the ball collides with the player, the ball's direction is updated based on the collision angle.
     * If the ball collides with the enemy, the ball's direction is updated based on the collision angle.
     * If the ball goes out of bounds, the score is updated, and the ball is reset.
     * 
     * @param ball The ball object that collided with the player or enemy.
     * @return void
     */
    private void handleMenuSelection() {
        switch (selectedOption) {
            case 0:
                isPaused = false;
                break;
            case 1:
                resetGame();
                isPaused = false;
                break;
            case 2:
                System.exit(0);
                break;
        }
    }

    /**
     * @brief Resets the game state to the initial state.
     * 
     * Resets the player, enemy, ball, and score to their initial values.
     * 
     * @return void
     */
    private void resetGame() {
        player = new Player(100, 155);
        enemy = new Enemy(100, 0);
        ball = new Ball();
        ball.addObserver(this);
        playerScore = 0;
        enemyScore = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isPaused) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                selectedOption = (selectedOption + 1) % menuOptions.length;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                handleMenuSelection();
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressedPlayer = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressedPlayer = true;
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            isPaused = !isPaused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressedPlayer = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressedPlayer = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // This method is not used but must be implemented as part of KeyListener interface
    }

    /**
     * @brief Updates the game score.
     * 
     * Updates the player and enemy scores.
     * 
     * @param playerScore The updated score of the player.
     * @param enemyScore The updated score of the enemy.
     * @return void
     */
    @Override
    public void updateScore(int playerScore, int enemyScore) {
        this.playerScore = playerScore;
        this.enemyScore = enemyScore;
    }
}
