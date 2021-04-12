package model;

public class Squares {
    private char [][] squares = new char[GlobalSettings.row][GlobalSettings.columns];
    private TileType tType ;
    private boolean occupied = false ;
    private SquareType sqType;

    public Squares (boolean o, TileType tt, SquareType st){
        this.occupied = o;
        this.tType = tt ;
        this.sqType = st;
    }


    public void setOccupied(){
        this.occupied = true ;
    }

}