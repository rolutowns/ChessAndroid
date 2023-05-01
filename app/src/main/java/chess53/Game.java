package chess53;

import java.util.ArrayList;

public class Game implements Comparable<Game>{

    public String name;
    public ArrayList<Piece[]> moves;

    public Game(String name, ArrayList<Piece[]> moves){
        this.name=name;
        this.moves=moves;
    }

    @Override
    public int compareTo(Game o) {
        return 0;
    }
}
