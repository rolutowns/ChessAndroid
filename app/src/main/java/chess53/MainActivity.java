package chess53;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chessandroid.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public Button newButton, openButton;

    public Game selected;
    public ListView gameListView;
    public static List<Game> games = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        newButton = findViewById(R.id.newGameButton);
        openButton = findViewById(R.id.openGameButton);
        openButton.setEnabled(false);


        newButton.setOnClickListener(v -> {
            Intent newGameIntent = new Intent(MainActivity.this, PlayActivity.class);
            startActivity(newGameIntent);
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplayActivity.currReplay=selected;
                Intent replayIntent = new Intent(MainActivity.this, ReplayActivity.class);
                startActivity(replayIntent);
            }
        });
    }

    protected void onStart() {

        super.onStart();

        gameListView = findViewById(R.id.gameListView);
        gameListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        List<Game> temp = Game.load(this);
        if (temp!=null) games = Game.load(this);
        gameListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games));
        gameListView.setOnItemClickListener((parent, view, position, id) -> {
            openButton.setEnabled(true);
            selected = (Game) gameListView.getItemAtPosition(position);
        });
    }
}
