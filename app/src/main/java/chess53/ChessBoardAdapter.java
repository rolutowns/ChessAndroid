package chess53;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chessandroid.R;

public class ChessBoardAdapter extends BaseAdapter {

    private final Context mContext;

    private GridView boardView;

    public Piece[] pieces;

    public int firstSelected = -1;
    public int secondSelected = -1;
    public ImageView pieceOne = null;

    public ChessBoardAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return 64;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int squareWidth = parent.getMeasuredWidth()/8;
        RelativeLayout itemContainerView;
        if (convertView == null) {
            LayoutInflater layoutInflater = ((Activity)mContext).getLayoutInflater();
            itemContainerView = (RelativeLayout)layoutInflater.inflate(R.layout.grid_item, null);
            ViewGroup.LayoutParams containerParams = itemContainerView.getLayoutParams();
            if(containerParams == null) {
                containerParams = new ViewGroup.LayoutParams(squareWidth, squareWidth);
            }
            containerParams.width = squareWidth;
            containerParams.height = squareWidth;
            itemContainerView.setLayoutParams(containerParams);
            ImageView background = itemContainerView.findViewById(R.id.background);
            if ((position+position/8)%2==0) background.setImageResource(R.drawable.white_square);
            else background.setImageResource(R.drawable.black_square);

            ImageView pieceView = itemContainerView.findViewById(R.id.piece);
            pieceView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            pieceView.setPadding(0, 0, 0, 0);
            pieceView.setImageResource(getImageResource(pieces[position]));
            pieceView.setTag(position);

            boardView.setOnItemClickListener((parent1, view, position1, id) -> {
                ChessBoardAdapter adapter = ChessBoardAdapter.this;
                ImageView piece = view.findViewById(R.id.piece);
                Log.i("A", "Piece: " + piece.getTag());
                if (adapter.firstSelected == -1) {
                    adapter.firstSelected = (Integer) piece.getTag();
                } else {
                    if (adapter.secondSelected != -1) {
                        adapter.firstSelected = (Integer) piece.getTag();
                        adapter.secondSelected = -1;
                    } else {
                        adapter.secondSelected = (Integer) piece.getTag();
                    }
                }
                Log.i("A", "First Piece:" + adapter.firstSelected);
                Log.i("A", "Second Piece:" + adapter.secondSelected);
//                PlayActivity activity = PlayActivity.activity;
                if (adapter.firstSelected != -1 && adapter.secondSelected != -1) {
                    String x1 = Integer.toString(firstSelected % 8);
                    String y1 = Integer.toString(firstSelected / 8);
                    String x2 = Integer.toString(secondSelected % 8);
                    String y2 = Integer.toString(secondSelected / 8);
                    PlayActivity.chessBoard.playTurn(x1+y1+x2+y2);
                    setData(PlayActivity.chessBoard.sendBoard());
                    if (PlayActivity.chessBoard.undoable) PlayActivity.undoButton.setEnabled(true);
                    if (PlayActivity.chessBoard.currCheck) Toast.makeText(mContext, "Check", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            itemContainerView = (RelativeLayout) convertView;
            ViewGroup.LayoutParams containerParams = convertView.getLayoutParams();
            containerParams.width = squareWidth;
            containerParams.height = squareWidth;
            itemContainerView.setLayoutParams(containerParams);
            ImageView pieceView = itemContainerView.findViewById(R.id.piece);
            pieceView.setImageResource(getImageResource(pieces[(Integer)pieceView.getTag()]));
        }
        return itemContainerView;
    }

    public void setParent(ViewGroup parent) {
        boardView = (GridView) parent;
    }

    public int getImageResource(Piece data) {
        if (data==null) return R.drawable.piece_empty;
        if(data.isWhite()) {
            switch(data.getType()) {
                case "KING": return R.drawable.white_king;
                case "QUEEN": return R.drawable.white_queen;
                case "ROOK": return R.drawable.white_rook;
                case "BISHOP": return R.drawable.white_bishop;
                case "KNIGHT": return R.drawable.white_knight;
                case "PAWN": return R.drawable.white_pawn;
                default: return R.drawable.piece_empty;
            }
        } else {
            switch(data.getType()) {
                case "KING": return R.drawable.black_king;
                case "QUEEN": return R.drawable.black_queen;
                case "ROOK": return R.drawable.black_rook;
                case "BISHOP": return R.drawable.black_bishop;
                case "KNIGHT": return R.drawable.black_knight;
                case "PAWN": return R.drawable.black_pawn;
                default: return R.drawable.piece_empty;
            }
        }
    }

    public void setData(Piece[] pieces) {
        this.pieces = pieces;
        boardView.invalidateViews();
    }
}
