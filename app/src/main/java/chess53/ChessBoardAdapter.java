package chess53;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chessandroid.R;

public class ChessBoardAdapter extends BaseAdapter {

    private final Context mContext;

    private GridView parent;

    public Piece[] pieces;

    public int firstSelected = -1;
    public int secondSelected = -1;
    public PieceButton pieceOne = null;
    public PieceButton pieceTwo = null;

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
            ImageButton background = itemContainerView.findViewById(R.id.background);
            if ((position+position/8)%2==0) background.setImageResource(R.drawable.white_square);
            else background.setImageResource(R.drawable.black_square);

            PieceButton pieceView = itemContainerView.findViewById(R.id.piece);
            pieceView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            pieceView.setPadding(0, 0, 0, 0);
            pieceView.setImageResource(getImageResource(pieces[position]));
            pieceView.setTag(position);

            pieceView.setOnClickListener(view -> {
                ChessBoardAdapter adapter = ChessBoardAdapter.this;
                PieceButton piece = (PieceButton) view;
                if(piece.selected) {
                    if(piece.equals(adapter.pieceOne)) {
                        adapter.pieceOne = null;
                        if(adapter.secondSelected != -1) {
                            adapter.pieceTwo.setImageResource(getImageResource(pieces[(Integer) adapter.pieceTwo.getTag()]));
                            adapter.pieceTwo.selected = false;
                        }
                        adapter.pieceTwo = null;
                        adapter.firstSelected = -1;
                        adapter.secondSelected = -1;
                    } else if(piece.equals(adapter.pieceTwo)) {
                        adapter.pieceTwo = null;
                        adapter.secondSelected = -1;
                    }
                    piece.setImageResource(getImageResource(pieces[(Integer)piece.getTag()]));
                    piece.selected = false;

                } else {
                    if(adapter.firstSelected == -1) {
                        adapter.firstSelected = (Integer)piece.getTag();
                        adapter.pieceOne = piece;
                    } else {
                        if(adapter.secondSelected != -1) {
                            adapter.pieceTwo.setImageResource(getImageResource(pieces[(Integer)adapter.pieceTwo.getTag()]));
                            adapter.pieceTwo.selected = false;
                        }
                        adapter.pieceTwo = piece;
                        adapter.secondSelected = (Integer)piece.getTag();
                    }
                    piece.setImageResource(getImageResource(pieces[(Integer)piece.getTag()]));
                    piece.selected = true;
                }
            });

            TextView text = itemContainerView.findViewById(R.id.square_label);
            text.setText("");
        } else {
            itemContainerView = (RelativeLayout) convertView;
            ViewGroup.LayoutParams containerParams = convertView.getLayoutParams();
            containerParams.width = squareWidth;
            containerParams.height = squareWidth;
            itemContainerView.setLayoutParams(containerParams);
            PieceButton pieceView = itemContainerView.findViewById(R.id.piece);
            pieceView.setImageResource(getImageResource(pieces[(Integer)pieceView.getTag()]));

        }
        return itemContainerView;
    }

    public void setParent(ViewGroup parent) {
        this.parent = (GridView) parent;
    }

    public int getImageResource(Piece data) {
        boolean isWhite = data.isWhite();

        if(isWhite) {
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
        this.parent.invalidateViews();
    }
}
