package Chess;

import BoardGame.Board;
import BoardGame.Piece;
import BoardGame.Position;
import Chess.Pieces.King;
import Chess.Pieces.Rook;

import javax.xml.transform.Source;

public class ChessMatch {
    private Board board;
    private int turn = 1;
    private Color CurrentPlayer = Color.white;

    public ChessMatch() {
        board = new Board(8, 8);
        innitialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return CurrentPlayer;
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

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        ValidateSourcePosition(position);
        return board.piece(position).PossibleMoves();
    }

    public ChessPiece PerfomChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        ValidateSourcePosition(source);
        ValidateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        NextTurn();

        return (ChessPiece) capturedPiece;
    }

    private void ValidateSourcePosition(Position position) {
        if (!board.therIsAPiece(position)) {
            throw new ChessException("There is no piece on source position") ;
        }
        if (CurrentPlayer !=  ((ChessPiece)board.piece(position)).getColor()) {
            throw  new ChessException("The chosen piece in not yours");
        }

        if (!board.piece((position)).isThereAnyPossibleMove()) {
            throw new ChessException(("There is no possible moves for chosen piece."));
        }
    }

    public void ValidateTargetPosition(Position source, Position target) {
        if (!board.piece(source).PossibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.RemovePiece(source);
        Piece capturedPiece = board.RemovePiece(target);

        board.PlacePiece(p, target);

        return capturedPiece;
    }

    private void NextTurn() {
        turn++;
        CurrentPlayer = (CurrentPlayer == Color.white) ? Color.black : Color.white;
    }
}
