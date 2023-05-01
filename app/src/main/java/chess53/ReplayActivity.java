package chess53;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.chessandroid.R;

import java.util.List;

public class ReplayActivity extends Activity {

    public static Game currReplay;
    public Button nextButton, exitButton;
    public ChessBoardAdapter boardAdapter;
    public TextView title;
    public int turn = 0;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        List<Piece[]> turns = currReplay.getMoves();
        setContentView(R.layout.replay_layout);
        GridView boardView = findViewById(R.id.board);
        boardAdapter = new ChessBoardAdapter(this);
        boardAdapter.setParent(boardView);
        boardView.setAdapter(boardAdapter);
        boardAdapter.setData(turns.get(turn));
        nextButton = findViewById(R.id.nextButton);
        exitButton = findViewById(R.id.exitButton);
        title = findViewById(R.id.title);
        title.setText(currReplay.getName());


        nextButton.setOnClickListener(v -> {
            turn++;
            boardAdapter.setData(turns.get(turn));
            if(turn == turns.size()-1) {
                v.setEnabled(false);
            }
        });

        exitButton.setOnClickListener(v -> finish());

        if(turns.size() == 1) {
            nextButton.setEnabled(false);
        }
    }

}
