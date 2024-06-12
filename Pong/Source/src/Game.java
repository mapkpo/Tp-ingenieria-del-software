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

public class Game extends Canvas implements Runnable, KeyListener, ScoreObserver {

    boolean isRunning = false;
    boolean isPaused = false;

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
    private final long ns = 1000000000 / FPS;
    private long lastTime = System.nanoTime();

    public boolean rightPressedPlayer = false;
    public boolean leftPressedPlayer = false;

    private int playerScore = 0;
    private int enemyScore = 0;

    private String[] menuOptions = {"Resume", "Reset", "Exit"};
    private int selectedOption = 0;

    public static void main(String[] args) {
        Game game = new Game();
        Thread thread = new Thread(game);
        JFrame frame = new JFrame();

        game.start();
        game.startFrame(frame);
        thread.start();
    }

    Game() {
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.addKeyListener(this);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public synchronized void start() {
        player = new Player(100, 155);
        enemy = new Enemy(100, 0);
        ball = new Ball();
        ball.addObserver(this);
        isRunning = true;
    }

    public void startFrame(JFrame frame) {
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

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

    private void resetGame() {
        player = new Player(100, 155);
        enemy = new Enemy(100, 0);
        ball = new Ball();
        ball.addObserver(this);
        playerScore = 0;
        enemyScore = 0;
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
    }

    @Override
    public void updateScore(int playerScore, int enemyScore) {
        this.playerScore = playerScore;
        this.enemyScore = enemyScore;
    }
}
