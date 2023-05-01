package chess53;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Game implements Serializable {

    public String name;
    public ArrayList<Piece[]> moves;

    public Date date;
    private Calendar cal;

    public static final long serialVersionUID = 1L;

    public static final String storeFile = "data.dat";

    public Game(String name, ArrayList<Piece[]> moves){
        this.name=name;
        this.moves=moves;
        cal = new GregorianCalendar();
        cal.set(Calendar.MILLISECOND,0);
        cal.setTime(cal.getTime());
        date = cal.getTime();
    }

    public ArrayList<Piece[]> getMoves(){
        return moves;
    }


    public String getName(){
        return name;
    }

    public Date getDate(){
        return date;
    }

    public static void save(Context parent) throws IOException {
        File file = new File(parent.getFilesDir(), storeFile);
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
            stream.writeObject(MainActivity.games);
        } catch(Exception e) {
            e.printStackTrace();
        }
        stream.close();
    }

    public static void load(Context parent) {
        File file = new File(parent.getFilesDir(), storeFile);
        try {
            ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file.toPath()));
            MainActivity.games = (ArrayList<Game>) in.readObject();
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public String toString(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ROOT);
        return name + " Played On " + df.format(cal.getTime());
    }
}
