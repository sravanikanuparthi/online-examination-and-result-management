package util;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExamTimer extends Thread {
    private final long durationSeconds;
    private final AtomicBoolean expired = new AtomicBoolean(false);
    private volatile boolean stopped = false;

    public ExamTimer(long durationSeconds) {
        this.durationSeconds = durationSeconds;
        setName("ExamTimer");
        setDaemon(true);
    }

    public boolean isExpired() {
        return expired.get();
    }

    public void stopTimer() {
        stopped = true;
        interrupt();
    }

    @Override
    public void run() {
        long remaining = durationSeconds;

        try {
            while (remaining > 0 && !stopped) {
                long min = remaining / 60;
                long sec = remaining % 60;
                System.out.printf("\rTime Remaining: %02d:%02d ", min, sec);
                System.out.flush();

                Thread.sleep(1000);
                remaining--;
            }

            if (!stopped) {
                expired.set(true);
                System.out.println("\n\n*** TIME EXPIRED - AUTO SUBMITTING ***");
            }
        } catch (InterruptedException ignored) {
            // Timer stopped normally.
        }
    }
}
