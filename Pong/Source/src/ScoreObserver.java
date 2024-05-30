/**
 * Interface for the observer pattern. The ScoreObserver is notified when the score changes.
 */
public interface ScoreObserver {
    void updateScore(int playerScore, int enemyScore);
}
