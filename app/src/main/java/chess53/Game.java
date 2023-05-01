package chess53;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Game implements Serializable, Comparable<Game> {

    public String name;
    public ArrayList<Piece[]> moves;

    public Date date;
    private Calendar cal;

    public Game(String name, ArrayList<Piece[]> moves){
        this.name=name;
        this.moves=moves;
        cal = new GregorianCalendar();
        cal.set(Calendar.MILLISECOND,0);
        cal.setTime(cal.getTime());
        date = cal.getTime();
    }

    public Game(String name, ArrayList<Piece[]> moves, Date date){
        this.name=name;
        this.moves=moves;
        this.date=date;

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

    public boolean save(Context parent){
        File f = new File(parent.getFilesDir() + "/" + this.name + ".chess");
        if( f.exists() ){
            return false;
        }
        ObjectOutputStream oos;
        try{
            FileOutputStream fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            }
        catch( IOException e ){
            Log.i("ERRORR!", e.toString());
            return false;
        }

        return true;
    }

    public static List<Game> load(Context parent) {
        Game loadedGame;
        String[] files = parent.getFilesDir().list((file, name) -> {
            if (name == null) {
                return false;
            }
            if (name.endsWith(".chess")) {
                return true;
            }
            return false;
        });

        List<Game> fileList = null;

        if (files == null) {
            return null;
        }

        for (int i = 0; i < files.length; i++) {
            files[i] = files[i].substring(0, files[i].lastIndexOf(".chess"));
        }
        File f;
        List<Game> l = new ArrayList<>();
        Game gdn = null;
        for (int i = 0; i < files.length; i++) {
            files[i] = parent.getFilesDir() + "/" + files[i] + ".chess";
            Log.i("A", files[i]);
            f = new File(files[i]);
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                loadedGame = (Game) ois.readObject();
                ois.close();
            } catch (IOException e) {
                Log.i("ERRORR!", e.toString());
                return null;
            } catch (ClassNotFoundException e) {
                Log.i("ERRORR!", e.toString());
                return null;
            }

            gdn = new Game(loadedGame.getName(), loadedGame.getMoves(), loadedGame.getDate());
            l.add(gdn);
        }
        return l;
    }

        @Override
    public int compareTo(Game o) {
        return 0;
    }

    public String toString(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ROOT);
        return name + " Played On " + df.format(cal.getTime());
    }
}
