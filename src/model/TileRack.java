package model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class TileRack {

    private ObservableList<Tile> tileRack = FXCollections.observableArrayList();
    private ListChangeListener<Tile> currentListener;
    private TileBag tileBag;

    public TileRack(TileBag tileBag) {
        this.tileBag = tileBag;
        for (int i = 0; i < 7; i++) {
            tileRack.add(tileBag.drawLetter());
        }
    }

    public void registerChangeListener(ListChangeListener<Tile> listener) {
        currentListener = listener;
        tileRack.addListener(listener);
    }

    public void removeChangeListener() {
        tileRack.removeListener(currentListener);
    }

    public boolean contains(Tile t) {
        return tileRack.contains(t);
    }

    public void remove(int index) {
        tileRack.remove(index);
    }

    public void add(Tile t) {
        tileRack.add(t);
    }

    public void refillFromBag() {
        while (!tileBag.isEmpty() && tileRack.size() < 7) {
            tileRack.add(tileBag.drawLetter());
        }
    }

    public ObservableList<Tile> getTileRack() {
        return tileRack;
    }

}
