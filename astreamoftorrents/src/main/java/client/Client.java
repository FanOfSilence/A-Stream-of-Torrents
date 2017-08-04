package client;

import client.process.Printer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jesper on 2017-06-17.
 */

//TODO: Should loop eternally until closed. Continuously print state of torrents and give opportunity to add torrent
public class Client {
    ExecutorService leechers;
    Printer printerThread;

    public Client(String saveFilePath) {
        this.leechers = Executors.newFixedThreadPool(5);
        printerThread = new Printer();
    }

    public void run() {
        printerThread.run();
        //TODO: Read previous started torrents in list and continue on them
        //TODO: Keep on printing progress of torrents
        while (true) {
            if (false/*gets keyboard interrupt*/) {
                printerThread.setActive(false);
                //TODO: Read torrent file input
                printerThread.setActive(true);
            }
        }
    }
}
