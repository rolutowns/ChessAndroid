package chess53;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.chessandroid.R;

public class PlayActivity extends Activity {
    public static Button resignButton;
    public static Button aiButton;
    public static Button drawButton;
    public static Button undoButton;
    public static Chess chessBoard;
    public ChessBoardAdapter boardAdapter;

    private final Handler handler = new Handler();
    private Runnable runnable;

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
            activity.resignButton.setEnabled(false);
            activity.drawButton.setEnabled(false);
            activity.undoButton.setEnabled(false);
            activity.aiButton.setEnabled(false);
            startActivityForResult(new Intent(PlayActivity.this, SaveActivity.class), 0);
        });
        drawButton.setOnClickListener(v -> {
            PlayActivity activity = PlayActivity.this;
            Chess chessBoard = activity.chessBoard;
            chessBoard.playTurn("draw");
            Toast.makeText(activity, chessBoard.getEndText(), Toast.LENGTH_LONG).show();
            activity.resignButton.setEnabled(false);
            activity.drawButton.setEnabled(false);
            activity.undoButton.setEnabled(false);
            activity.aiButton.setEnabled(false);
            startActivityForResult(new Intent(PlayActivity.this, SaveActivity.class), 0);
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
            activity.undoButton.setEnabled(true);
            chessBoard.ai();
            boardAdapter.setData(chessBoard.sendBoard());
            if (boardAdapter.pieceOne != null) {
                boardAdapter.pieceOne.callOnClick();
            }
        });

    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = () -> {
            handler.postDelayed(runnable, 1000);
            if (chessBoard.getEndText()!=null){
                    resignButton.setEnabled(false);
                    drawButton.setEnabled(false);
                    undoButton.setEnabled(false);
                    aiButton.setEnabled(false);
                    Toast.makeText(PlayActivity.this, chessBoard.getEndText(), Toast.LENGTH_LONG).show();
                    startActivityForResult(new Intent(PlayActivity.this, SaveActivity.class), 0);
                }
        }, 1000);
        super.onResume();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
         super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode == RESULT_OK) {
                Log.i("Q", "Reached Here");
                String name = data.getStringExtra("Name");
                Game newGame = new Game(name, chessBoard.sendGame());
                MainActivity.games.add(newGame);
            }
            else Toast.makeText(this,"Game Not Saved",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();

    }
}
