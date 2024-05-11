package Chess;

import BoardGame.Board;
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

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.PlacePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void innitialSetup() {
        placeNewPiece('c', 1, new Rook(board, Color.white));
        placeNewPiece('c', 2, new Rook(board, Color.white));
        placeNewPiece('d', 2, new Rook(board, Color.white));
        placeNewPiece('e', 2, new Rook(board, Color.white));
        placeNewPiece('e', 1, new Rook(board, Color.white));
        placeNewPiece('d', 1, new King(board, Color.white));

        placeNewPiece('c', 7, new Rook(board, Color.black));
        placeNewPiece('c', 8, new Rook(board, Color.black));
        placeNewPiece('d', 7, new Rook(board, Color.black));
        placeNewPiece('e', 7, new Rook(board, Color.black));
        placeNewPiece('e', 8, new Rook(board, Color.black));
        placeNewPiece('d', 8, new King(board, Color.black));
    }
}
