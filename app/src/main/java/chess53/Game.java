package chess53;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Game implements Serializable, Comparable<Game> {

    public String name;
    public ArrayList<Piece[]> moves;
    public Calendar cal;

    public Game(String name, ArrayList<Piece[]> moves){
        this.name=name;
        this.moves=moves;
        cal = new GregorianCalendar();
        cal.set(Calendar.MILLISECOND,0);
        cal.setTime(cal.getTime());
    }

    public ArrayList<Piece[]> getMoves(){
        return moves;
    }


    public String getName(){
        return name;
    }

    public String getDate(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ROOT);
        String date = df.format(cal.getTime());
        return date;
    }

    public boolean save(Context parent){
        File f = new File(parent.getFilesDir() + "/" + this.name + ".chess");
        if( f.exists() ){
            return false;
        }
        ObjectOutputStream oos;
        try{
            Log.i("A", this.name);
            FileOutputStream fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            Log.i("A", f.getAbsolutePath());
            Log.i("B", "GETS HERE");
            oos.writeObject(this);
            Log.i("D", "GETS HERE");
            oos.close();
            fos.close();
            Log.i("G", "GETS HERE");
        }
        catch( IOException e ){
            Log.i("ERRORR!", e.toString());
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Game o) {
        return 0;
    }
}
