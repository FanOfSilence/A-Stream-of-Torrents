package torrent.state;

import java.util.Observer;

/**
 * Created by Jesper on 2017-08-06.
 */
public interface IObservable {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
}
