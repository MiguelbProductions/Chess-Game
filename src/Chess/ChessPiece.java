package Chess;

import BoardGame.Board;
import BoardGame.Piece;
import BoardGame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int MoveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void increaseMoveCount() {
        MoveCount++;
    }

    public void decreaseMoveCount() {
        MoveCount--;
    }

    public int getMoveCount() {
        return MoveCount;
    }

    protected boolean IsOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);

        return p != null && p.getColor() != color;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPostion(position);
    }

    public ChessPosition GetChessePiece() {
        return ChessPosition.fromPostion(position);
    }
}
