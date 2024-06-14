/**
 * @brief Interface for the observer pattern.
 * 
 * The ScoreObserver is notified when the score changes. Implementing classes should provide the
 * implementation of the updateScore method to handle score updates.
 */
public interface ScoreObserver {
    /**
     * @brief Called when the score changes.
     * 
     * @param playerScore The current score of the player.
     * @param enemyScore The current score of the enemy.
     */
    void updateScore(int playerScore, int enemyScore);
}
