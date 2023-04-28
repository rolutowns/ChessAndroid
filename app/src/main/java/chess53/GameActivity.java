package chess53;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        resignButton = findViewById(R.id.resignButton);
        aiButton = findViewById(R.id.aiButton);
        drawButton = findViewById(R.id.drawButton);
        undoButton = findViewById(R.id.undoButton);

        resignButton.setOnClickListener(v -> {
            GameActivity activity = GameActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("resign");
            Toast.makeText(activity, chessBoard.getEndText(),Toast.LENGTH_LONG).show();
            if(chessBoard.getEndText()!=null) {
                activity.resignButton.setEnabled(false);
                activity.drawButton.setEnabled(false);
                activity.undoButton.setEnabled(false);
                activity.aiButton.setEnabled(false);
//                    Intent saveIntent = new Intent(GameActivity.this, SaveGameActivity.class);
//                    startActivityForResult(saveIntent, SAVE_GAME);
            }
        });
        drawButton.setOnClickListener(v -> {
            GameActivity activity = GameActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("draw");
            Toast.makeText(activity, chessBoard.getEndText(),Toast.LENGTH_LONG).show();
            if(chessBoard.getEndText()!=null) {
                activity.resignButton.setEnabled(false);
                activity.drawButton.setEnabled(false);
                activity.undoButton.setEnabled(false);
                activity.aiButton.setEnabled(false);
//                    Intent saveIntent = new Intent(GameActivity.this, SaveGameActivity.class);
//                    startActivityForResult(saveIntent, SAVE_GAME);
            }
        });
        undoButton.setOnClickListener(view -> {
            if(chessBoard.undoable) {
                chessBoard.undo();
                boardAdapter.setData(chessBoard.sendBoard());
                if(boardAdapter.pieceOne != null) {
                    boardAdapter.pieceOne.callOnClick();
                }
            }
        });


    }
}
