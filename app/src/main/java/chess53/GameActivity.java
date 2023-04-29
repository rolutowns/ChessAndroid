package chess53;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chessandroid.R;

public class GameActivity extends AppCompatActivity{
    public Button resignButton, aiButton, drawButton, undoButton;
    public Chess chessBoard;
    public ChessBoardAdapter boardAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        chessBoard = new Chess();
        setContentView(R.layout.play_game_layout);
        GridView boardView = findViewById(R.id.board);
        boardAdapter = new ChessBoardAdapter(this);
        boardAdapter.setData(chessBoard.sendBoard());
        boardView.setAdapter(boardAdapter);
        resignButton = findViewById(R.id.resignButton);
        aiButton = findViewById(R.id.aiButton);
        drawButton = findViewById(R.id.drawButton);
        undoButton = findViewById(R.id.undoButton);

        resignButton.setOnClickListener(v -> {
            GameActivity activity = GameActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("resign");
            Toast.makeText(activity, chessBoard.getEndText(),Toast.LENGTH_LONG).show();
//            if(chessBoard.getEndText()!=null) {
                activity.resignButton.setEnabled(false);
                activity.drawButton.setEnabled(false);
                activity.undoButton.setEnabled(false);
                activity.aiButton.setEnabled(false);
//                    Intent saveIntent = new Intent(GameActivity.this, SaveGameActivity.class);
//                    startActivityForResult(saveIntent, SAVE_GAME);
//            }
        });
        drawButton.setOnClickListener(v -> {
            GameActivity activity = GameActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("draw");
            Toast.makeText(activity, chessBoard.getEndText(),Toast.LENGTH_LONG).show();
//            if(chessBoard.getEndText()!=null) {
                activity.resignButton.setEnabled(false);
                activity.drawButton.setEnabled(false);
                activity.undoButton.setEnabled(false);
                activity.aiButton.setEnabled(false);
//                    Intent saveIntent = new Intent(GameActivity.this, SaveGameActivity.class);
//                    startActivityForResult(saveIntent, SAVE_GAME);
//            }
        });
        undoButton.setOnClickListener(v -> {
            if(chessBoard.undoable) {
                chessBoard.undo();
                boardAdapter.setData(chessBoard.sendBoard());
                if(boardAdapter.pieceOne != null) {
                    boardAdapter.pieceOne.callOnClick();
                }
            }
        });

        boardView.setOnItemClickListener((parent, view, position, id) -> {
            GameActivity activity = GameActivity.this;
            Chess chessBoard = activity.chessBoard;
            ChessBoardAdapter boardAdapter = activity.boardAdapter;
            int start = boardAdapter.firstSelected;
            int end = boardAdapter.secondSelected;
            if(start == -1 || end == -1) {
                Toast.makeText(activity,"No move selected",Toast.LENGTH_LONG).show();
            } else {
                String x1 = Integer.toString(start % 8);
                String y1 = Integer.toString(start / 8);
                String x2 = Integer.toString(end % 8);
                String y2 = Integer.toString(end / 8);
                chessBoard.playTurn(x1+y1+x2+y2);
                boardAdapter.setData(chessBoard.sendBoard());
                boardAdapter.pieceOne.callOnClick();
                undoButton.setEnabled(chessBoard.undoable);
            }
        });


    }
}
