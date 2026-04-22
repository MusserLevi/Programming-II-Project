import javax.swing.Timer;

public class GameTimer {
    private Timer timer;
    private int secondsLeft;
    private Runnable onTick;
    private Runnable onTimeUp;

    public GameTimer(String difficulty, Runnable onTick, Runnable onTimeUp) {
        this.onTick = onTick;
        this.onTimeUp = onTimeUp;
        
        // Set time based on difficulty
        switch (difficulty.toLowerCase()) {
            case "hard":
                secondsLeft = 30;
                break;
            case "medium":
                secondsLeft = 60;
                break;
            case "easy":
            default:
                secondsLeft = 180; // 3 minutes
                break;
        }
    }

    public void start() {
        timer = new Timer(1000, e -> {
            secondsLeft--;
            onTick.run(); // Update GUI
            
            if (secondsLeft <= 0) {
                timer.stop();
                onTimeUp.run(); // End game
            }
        });
        timer.start();
    }

    public void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public String getFormattedTime() {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}