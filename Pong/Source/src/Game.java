import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * The `Game` class represents the main game logic and functionality.
 * It extends the `Canvas` class and implements the `Runnable` and `KeyListener` interfaces.
 * The game is responsible for rendering the game objects, handling user input, and updating the game state.
 */
public class Game extends Canvas implements Runnable, KeyListener {
	
	boolean isRunning = false;
	
	private BufferStrategy bs;
	private BufferedImage image;
	
	public static Player player;
	public static Enemy enemy;
	public static Ball ball;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	private final int SCALE = 4;
	
	private final int SPEED = 4;
	private final int FPS = 60;
	private final long ns = 1000000000/FPS;
	private long lastTime = System.nanoTime();
	
	public boolean rightPressedPlayer = false;
	public boolean leftPressedPlayer = false;
	

	/**
	 * @brief The main entry point for the Pong game.
	 * 
	 * Creates a new instance of the Game class, starts a new thread for the game loop,
	 * and initializes the game frame.
	 *
	 * @param args The command line arguments.
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
	 * @brief The Game class represents the main game window.
	 * 
	 * It extends the JPanel class and implements the KeyListener interface.
	 */
	Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		this.addKeyListener(this);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	

	/**
	 * @brief Sets up the game
	 * 
	 * Starts the game by initializing the player, enemy, and ball objects.
	 * 
	 * This method should be called before any game logic is executed.
	 */
	public synchronized void start() {
		player = new Player(100, 155);
		enemy = new Enemy(100, 0);
		ball = new Ball();
		isRunning = true;
	}
	

	/**
	 * @brief Sets the game panel 
	 * 
	 * Adds the game panel to the specified JFrame, configures the frame, and makes it visible.
	 * 
	 * @param frame the JFrame to start the game in
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
	 */
	public void tick() {
		if (rightPressedPlayer) {		
			player.rightPressed();	
		} else if (leftPressedPlayer) {
			player.leftPressed();
		}
	}
	

	/**
	 * @brief Renders the game by drawing the game elements on the screen.
	 */
	public void render() {
		bs = this.getBufferStrategy();
		
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		
		// Background
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		player.render(g);
		ball.render(g);
		enemy.render(g);
		
		g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		bs.show();
	}
	
	
	/**
	 * @brief Starts the buffer strategy for rendering the game.
	 * 
	 * Buffer strategy is used to avoid flickering in the game rendering.
	 * 
	 * @param bs the buffer strategy to start
	 */
	public void startBufferStrategy(BufferStrategy bs) {
		if (bs == null) {
			this.createBufferStrategy(3);	
			return;
		}
		
		bs.show();
	}
	

	/**
	 * @brief main game loop
	 * 
	 * Runs the game loop, continuously updating and rendering the game.
	 */
	public void run() {
		while (isRunning) {
			this.requestFocus();
			long now = System.nanoTime();
			
			if (now - lastTime >= ns/SPEED) {
				tick();
				render();				
				lastTime = now;
			}			
		}	
	}


	/**
	 * @brief Invoked when a key is pressed.
	 * 
	 * @param e the KeyEvent object representing the key event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressedPlayer = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressedPlayer = true;
		}	
	}


	/**
	 * @brief Invoked when a key is released.
	 * 
	 * @param e the KeyEvent object containing information about the key event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressedPlayer = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressedPlayer = false;
		}
	}


	/**
	 * @brief Invoked when a key is typed. 
	 * 
	 * This method is not used in this implementation.
	 *
	 * @param e the KeyEvent object containing information about the key event
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}
