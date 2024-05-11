package Chess;

import BoardGame.Board;
import BoardGame.Position;
import Chess.Pieces.King;
import Chess.Pieces.Rook;

public class ChessMatch {
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        innitialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i=0; i < board.getRows(); i++) {
            for (int j=0; j < board.getRows(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return mat;
    }

    private void innitialSetup() {
        board.PlacePiece(new Rook(board, Color.white), new Position(2, 1));
        board.PlacePiece(new King(board, Color.black), new Position(3, 2));
        board.PlacePiece(new Rook(board, Color.white), new Position(5, 4));

    }
}
