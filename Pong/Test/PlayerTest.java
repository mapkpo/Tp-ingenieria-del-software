import org.junit.Test;
import org.mockito.Mockito;
import java.awt.Graphics;
import java.awt.Color;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testRightPressed() {
        Player player = new Player(5, 5);
        player.rightPressed();
        // Assuming the initial x position was 0
        assertEquals(6, player.x);
    }

    @Test
    public void testLeftPressed() {
        Player player = new Player(5, 5);
        player.leftPressed();
        // Assuming the initial x position was 0
        assertEquals(4, player.x);
    }

    @Test
    public void testInitialPosition() {
        Player player = new Player(5, 5);
        // Check if the initial position is set correctly
        assertEquals(5, player.x);
        assertEquals(5, player.y);
    }

    @Test
    public void testTickWithinBounds() {
        // Player within bounds, no change expected
        Player player = new Player(10, 0);
        player.tick();
        assertEquals(10, player.x);
    }

    @Test
    public void testTickAtRightBoundary() {
        // Player at right boundary, should move left by 1
        //Player player = new Player(Game.WIDTH - Game.player.WIDTH + 1, 0);   //esto genera un problema de autoreferencia ya que busca un dato propio para crearse (CREO)
        Player player = new Player(201 , 0);
        player.tick();
        assertEquals(Game.WIDTH - player.WIDTH, player.x);
    }

    @Test
    public void testTickAtLeftBoundary() {
        // Player at left boundary, should move right by 1
        Player player = new Player(-1, 0);
        player.tick();
        assertEquals(0, player.x);
    }

    @Test
    public void testRender() {
        // Create a mock Graphics object
        Graphics g = Mockito.mock(Graphics.class);

        // Create a Player object
        Player player = new Player(0, 0);

        // Call the render method
        player.render(g);

        // Verify that setColor was called with the correct color
        Mockito.verify(g).setColor(new Color(255, 255, 255));

        // Verify that fillRect was called with the correct parameters
        Mockito.verify(g).fillRect(player.x, player.y, player.WIDTH, player.HEIGHT);
    }
}
