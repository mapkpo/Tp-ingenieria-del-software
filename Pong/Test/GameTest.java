import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.KeyEvent;

public class GameTest {

    @Test
    public void testRightKeyPressed() {
        Game game = new Game();
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, 'R');
        
        game.keyPressed(keyEvent);
        assertTrue(game.rightPressedPlayer);
    }

    @Test
    public void testLeftKeyPressed() {
        Game game = new Game();
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, 'L');
        
        game.keyPressed(keyEvent);
        assertTrue(game.leftPressedPlayer);
    }

    @Test
    public void testRightKeyReleased() {
        Game game = new Game();
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, 'R');
        
        game.keyReleased(keyEvent);
        assertFalse(game.rightPressedPlayer);
    }

    @Test
    public void testLeftKeyReleased() {
        Game game = new Game();
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, 'L');
        
        game.keyReleased(keyEvent);
        assertFalse(game.leftPressedPlayer);
    }
}
