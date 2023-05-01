package chess53;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chessandroid.R;

public class PlayActivity extends Activity {
    public Button resignButton;
    public Button aiButton;
    public Button drawButton;
    public static Button undoButton;
    public static Chess chessBoard;
    public ChessBoardAdapter boardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        chessBoard = new Chess();
        setContentView(R.layout.play_game_layout);
        GridView boardView = findViewById(R.id.board);
        boardAdapter = new ChessBoardAdapter(this);
        boardAdapter.setParent(boardView);
        boardView.setAdapter(boardAdapter);
        boardAdapter.setData(chessBoard.sendBoard());
        resignButton = findViewById(R.id.resignButton);
        aiButton = findViewById(R.id.aiButton);
        drawButton = findViewById(R.id.drawButton);
        undoButton = findViewById(R.id.undoButton);
        undoButton.setEnabled(false);

        resignButton.setOnClickListener(v -> {
            PlayActivity activity = PlayActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("resign");
            Toast.makeText(activity, chessBoard.getEndText(), Toast.LENGTH_LONG).show();
//            if(chessBoard.getEndText()!=null) {
            activity.resignButton.setEnabled(false);
            activity.drawButton.setEnabled(false);
            activity.undoButton.setEnabled(false);
            activity.aiButton.setEnabled(false);
//            }
        });
        drawButton.setOnClickListener(v -> {
            PlayActivity activity = PlayActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("draw");
            Toast.makeText(activity, chessBoard.getEndText(), Toast.LENGTH_LONG).show();
//            if(chessBoard.getEndText()!=null) {
            activity.resignButton.setEnabled(false);
            activity.drawButton.setEnabled(false);
            activity.undoButton.setEnabled(false);
            activity.aiButton.setEnabled(false);
//            }
        });
        undoButton.setOnClickListener(v -> {
            if (chessBoard.undoable) {
                chessBoard.undo();
                PlayActivity activity = PlayActivity.this;
                activity.undoButton.setEnabled(false);
                boardAdapter.setData(chessBoard.sendBoard());
                if (boardAdapter.pieceOne != null) {
                    boardAdapter.pieceOne.callOnClick();
                }
            }
        });
        aiButton.setOnClickListener(v -> {
            PlayActivity activity = PlayActivity.this;
            activity.undoButton.setEnabled(false);
            chessBoard.ai();
            boardAdapter.setData(chessBoard.sendBoard());
            if (boardAdapter.pieceOne != null) {
                boardAdapter.pieceOne.callOnClick();
            }
        });

    }
}
