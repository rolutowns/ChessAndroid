package chess53;

import java.util.ArrayList;

/**
 * Chess game implementation that allows for en passant capture, castling, and detects checkmates
 *
 * @author Krish Patel
 * @author Roshan Varadhan
 */
public class Chess {

    /**
     * The board of the game, represented by a 2D array of Pieces
     */
    static Piece[][] board;

    public ArrayList<Piece[]> game;

    /**
     * Boolean to keep track of the current turn, true for white, false for black
     */
    public static boolean whiteTurn;

    /**
     * String to keep track of the end result of the game, null if not over
     */
    public static String end;


    /**
     * Boolean to keep track of whether white is currently in check
     */
    private static boolean whiteInCheck;

    /**
     * Boolean to keep track of whether black is currently in check
     */
    private static boolean blackInCheck;

    /**
     * Piece to track the last moved piece, used for En Passant
     */
    static Piece lastMoved;

    public boolean undoable;

    private boolean legalMove;

    public boolean currCheck;

    /**
     * Initializes a new game of Chess by creating a new board, setting the first turn to white
     * and creating new instances of all the pieces in their starting positions.
     */
    public Chess() {
        board = new Piece[8][8];
        lastMoved = null;
        whiteTurn = true;
        end = null;
        game = new ArrayList<>();
        undoable = false;
        currCheck = false;

        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);

        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(false);
            board[2][i] = null;
            board[3][i] = null;
            board[4][i] = null;
            board[5][i] = null;
            board[6][i] = new Pawn(true);

        }
        game.add(sendBoard());
    }

    /**
     * Plays one turn of chess.
     * If a player is in check, checks for checkmate.
     * Promotes a pawn if it reaches the end of the board.
     * Continues to prompt for a legal move until a valid one is entered.
     */
    public void playTurn(String move) {
        int[] kingPos = getKingPos(whiteTurn);
        Piece tempKing = board[kingPos[0]][kingPos[1]];
        legalMove = false;
        if (move.contains("resign")) {
            end = whiteTurn ? "Black Wins" : "White wins";
            return;
        }
        if (move.contains("draw")) {
            end = "Draw";
            return;
        }
        // If no draw
        int[] src = new int[2];
        int[] dest = new int[2];
        src[1] = Integer.parseInt(move.substring(0, 1));
        src[0] = Integer.parseInt(move.substring(1, 2));
        dest[1] = Integer.parseInt(move.substring(2, 3));
        dest[0] = Integer.parseInt(move.substring(3, 4));
        if (board[src[0]][src[1]] != null && board[src[0]][src[1]].isWhite() == whiteTurn) {
            Piece endPiece = board[dest[0]][dest[1]];
            Piece startPiece = board[src[0]][src[1]];
            String promoteTo = null;
            Piece enPassantPotential;
            if (startPiece instanceof Pawn && endPiece == null && Math.abs(dest[1] - src[1]) == 1) {
                enPassantPotential = board[src[0]][dest[1]];
            } else enPassantPotential = null;

            // pawn promotion
            if (canBePromoted(src, dest[0])) {
                promoteTo = move.substring(move.length() - 1);
                legalMove = board[src[0]][src[1]].isLegalMove(src[0], dest[0], src[1], dest[1]);
            } else if (whiteInCheck || blackInCheck) {
                if (startPiece instanceof King) {
                    if (Math.abs(dest[1] - src[1]) == 2) legalMove = false;
                    else legalMove = board[src[0]][src[1]].isLegalMove(src[0], dest[0], src[1], dest[1]);
                } else legalMove = board[src[0]][src[1]].isLegalMove(src[0], dest[0], src[1], dest[1]);
            } else legalMove = board[src[0]][src[1]].isLegalMove(src[0], dest[0], src[1], dest[1]);
            // if move results in self check
            if (legalMove) {
                kingPos = getKingPos(whiteTurn);
                tempKing = board[kingPos[0]][kingPos[1]];
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[i][j] == null) continue;
                        if (board[i][j].isLegalMove(i, kingPos[0], j, kingPos[1])) {
                            board[i][j] = board[kingPos[0]][kingPos[1]];
                            board[kingPos[0]][kingPos[1]] = tempKing;
                            board[src[0]][src[1]] = startPiece;
                            board[dest[0]][dest[1]] = endPiece;
                            if (startPiece instanceof King) {
                                if (dest[1] - src[1] == 2) {
                                    board[src[0]][7] = board[src[0]][5];
                                    board[src[0]][5] = null;
                                } else if (src[1] - dest[1] == 2) {
                                    board[src[0]][0] = board[src[0]][3];
                                    board[src[0]][3] = null;
                                }
                            }
                            if (enPassantPotential != null)
                                board[src[0]][dest[1]] = enPassantPotential;
                            legalMove = false;
                            break;
                        }
                    }
                }
            }
            if (legalMove && promoteTo != null) promote(dest, promoteTo);
        }
        if (legalMove) {
            lastMoved = board[dest[0]][dest[1]];
            lastMoved.hasMoved = true;
            whiteTurn = !whiteTurn;
            if(whiteTurn) PlayActivity.gameText.setText("White's turn");
            else PlayActivity.gameText.setText("Black's turn");            game.add(sendBoard());
            undoable = true;
        }
        currCheck = false;
        kingPos = getKingPos(whiteTurn);
        tempKing = board[kingPos[0]][kingPos[1]];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) continue;
                if (board[i][j].isLegalMove(i, kingPos[0], j, kingPos[1])) {
                    board[i][j] = board[kingPos[0]][kingPos[1]];
                    board[kingPos[0]][kingPos[1]] = tempKing;
                    if (tempKing.isWhite()) whiteInCheck = true;
                    else blackInCheck = true;
                    currCheck = true;
                    break;
                }
            }
        }
        if (!currCheck) {
            if (tempKing.isWhite()) whiteInCheck = false;
            else blackInCheck = false;
        }

        if (whiteTurn && whiteInCheck) {
            boolean mate = noValidMoves();
            if (mate) {
                end = "Checkmate: Black Wins";
                return;
            }
        }
        if (!whiteTurn && blackInCheck) {
            boolean mate = noValidMoves();
            if (mate) {
                end = "Checkmate: White Wins";
                return;
            }
        }
        if (noValidMoves()) {
            end = "Stalemate";
        }
        if(!legalMove) PlayActivity.gameText.setText("Illegal Move Attempted. Try again.");
    }

    /**
     * Returns a boolean to check if the player has any legal moves
     * Utilized to check for checkmate if in check or stalemate if not in check
     *
     * @return a boolean representing if the player can legally make a move
     */
    private static boolean noValidMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) continue;
                if (board[i][j].isWhite() != whiteTurn) continue;
                Piece aPiece = board[i][j];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Piece bPiece = board[x][y];
                        Piece enPassantPotential2;
                        if (aPiece instanceof Pawn && bPiece == null && Math.abs(y - j) == 1) {
                            enPassantPotential2 = board[i][y];
                        } else enPassantPotential2 = null;
                        boolean canMove = aPiece.isLegalMove(i, x, j, y);
                        if (canMove) {
                            int[] kingPos = getKingPos(whiteTurn);
                            Piece tempKing = board[kingPos[0]][kingPos[1]];
                            for (int w = 0; w < 8; w++) {
                                for (int z = 0; z < 8; z++) {
                                    if (board[w][z] == null) continue;
                                    if (board[w][z].isLegalMove(w, kingPos[0], z, kingPos[1])) {
                                        board[w][z] = board[kingPos[0]][kingPos[1]];
                                        board[kingPos[0]][kingPos[1]] = tempKing;
                                        board[i][j] = aPiece;
                                        board[x][y] = bPiece;
                                        if (enPassantPotential2 != null)
                                            board[i][y] = enPassantPotential2;
                                        canMove = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (canMove) {
                            board[i][j] = aPiece;
                            board[x][y] = bPiece;
                            if (enPassantPotential2 != null) board[i][y] = enPassantPotential2;
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the current position of the player's king
     * Utilized to check if the king is currently in check, or will move into check
     *
     * @param w a boolean to determine the color of the king. True if white, false if black
     * @return An integer array of size two that returns the coordinates of the king in the two dimensional chess board.
     */
    private static int[] getKingPos(boolean w) {
        int[] pos = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] instanceof King) {
                    if (board[i][j].isWhite() == w) {
                        pos[0] = i;
                        pos[1] = j;
                    }
                }
            }
        }
        return pos;
    }

    /**
     * @param src     The 2 item array with the  indices of the current piece about to be moved
     * @param destRow The row that the piece is about to move to
     * @return true if the piece is a pawn eligible for promotion, false if not.
     */
    private static boolean canBePromoted(int[] src, int destRow) {
        Piece p = board[src[0]][src[1]];
        if (p instanceof Pawn) {
            return p.isWhite() && destRow == 0 || !p.isWhite() && destRow == 7;
        }
        return false;
    }

    /**
     * Promotes a pawn into a higher piece if possible.
     *
     * @param dest      A 2 item array containing the location of the piece to be promoted
     * @param promoteTo A string representing the piece to be promoted to.
     */
    private static void promote(int[] dest, String promoteTo) {
        switch (promoteTo) {
            case "R":
                board[dest[0]][dest[1]] = new Rook(whiteTurn);
                break;
            case "N":
                board[dest[0]][dest[1]] = new Knight(whiteTurn);
                break;
            case "B":
                board[dest[0]][dest[1]] = new Bishop(whiteTurn);
                break;
            default:
                board[dest[0]][dest[1]] = new Queen(whiteTurn);
                break;
        }
    }

    public void ai() {
        for (int i = 0; i < 64; i++) {
            int starty = i % 8;
            int startx = i / 8;
            if (board[startx][starty] == null) continue;
            if (board[startx][starty].isWhite() != whiteTurn) continue;
            String x1 = Integer.toString(startx);
            String y1 = Integer.toString(starty);
            for (int j = 0; j < 64; j++) {
                int endy = j % 8;
                int endx = j / 8;
                if (board[endx][endy]!=null && board[endx][endy].isWhite()==whiteTurn) continue;
                String y2 = Integer.toString(endy);
                String x2 = Integer.toString(endx);
                playTurn(y1 + x1 + y2 + x2);
                if (legalMove) return;
            }
        }
    }

    public void undo() {
        int last = game.size() - 2;
        if (last < 0) return;
        Piece[] lastBoard = game.get(last);
        for (int i = 0; i < 64; i++) board[i / 8][i % 8] = lastBoard[i];
        lastMoved = lastBoard[64];
        whiteTurn = !whiteTurn;
        if(whiteTurn) PlayActivity.gameText.setText("White's turn");
        else PlayActivity.gameText.setText("Black's turn");
        game.remove(game.size() - 1);
        undoable = false;
    }

    public Piece[] sendBoard() {
        Piece[] tempBoard = new Piece[65];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece temp = board[i][j];
                if (temp == null) {
                    tempBoard[i * 8 + j] = null;
                    continue;
                }
                String type = temp.getType();
                Piece newPiece = null;
                switch (type) {
                    case "PAWN":
                        newPiece = new Pawn(temp.isWhite());
                        break;
                    case "ROOK":
                        newPiece = new Rook(temp.isWhite());
                        break;
                    case "BISHOP":
                        newPiece = new Bishop(temp.isWhite());
                        break;
                    case "KNIGHT":
                        newPiece = new Knight(temp.isWhite());
                        break;
                    case "QUEEN":
                        newPiece = new Queen(temp.isWhite());
                        break;
                    case "KING":
                        newPiece = new King(temp.isWhite());
                        break;
                }
                newPiece.setEnPassant(temp.enPassantable());
                newPiece.hasMoved = temp.hasMoved;
                tempBoard[i * 8 + j] = newPiece;
            }
        }
        tempBoard[64] = lastMoved;
        return tempBoard;
    }

    public ArrayList<Piece[]> sendGame(){
        return game;
    }

    /**
     * Returns the ending text for the chess game.
     * This method is called after the game has ended and
     * the end state has been determined (checkmate, draw, or resignation).
     *
     * @return A string representing the end state of the game.
     */
    public String getEndText() {
        return end;
    }
}