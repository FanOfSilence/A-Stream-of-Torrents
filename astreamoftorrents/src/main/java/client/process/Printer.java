package client.process;

/**
 * Created by Jesper on 2017-08-03.
 */
public class Printer implements Runnable {
    private boolean active;

    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    @Override
    public void run() {
        if (!active) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //TODO: Print everything interesting about torrents
    }
}
