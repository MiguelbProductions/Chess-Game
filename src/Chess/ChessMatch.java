package Chess;

import java.util.ArrayList;
import java.util.List;

import BoardGame.Board;
import BoardGame.Piece;
import BoardGame.Position;
import Chess.Pieces.*;


public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.white;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckmate() {
        return !checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0; i<board.getRows(); i++) {
            for (int j=0; j<board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).PossibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(target);

        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.white && target.getRow() == 0) || (movedPiece.getColor() == Color.black && target.getRow() == 7)) {
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = (testCheck(opponent(currentPlayer)));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        else {
            nextTurn();
        }

        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        }
        else {
            enPassantVulnerable = null;
        }

        return (ChessPiece)capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.RemovePiece(source);
        p.increaseMoveCount();

        Piece capturedPiece = board.RemovePiece(target);
        board.PlacePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.RemovePiece(sourceT);

            board.PlacePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.RemovePiece(sourceT);

            board.PlacePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.white) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                }
                else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.RemovePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.RemovePiece(target);
        p.decreaseMoveCount();

        board.PlacePiece(p, source);

        if (capturedPiece != null) {
            board.PlacePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.RemovePiece(targetT);

            board.PlacePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.RemovePiece(targetT);

            board.PlacePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.therIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).PossibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.white) ? Color.black : Color.white;
    }

    private Color opponent(Color color) {
        return (color == Color.white) ? Color.black : Color.white;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).toList();
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).toList();
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.PossibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).toList();
        for (Piece p : list) {
            boolean[][] mat = p.PossibleMoves();
            for (int i=0; i<board.getRows(); i++) {
                for (int j=0; j<board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.PlacePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }

        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.RemovePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.PlacePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.white));
        placeNewPiece('b', 1, new Knight(board, Color.white));
        placeNewPiece('c', 1, new Bishop(board, Color.white));
        placeNewPiece('d', 1, new Queen(board, Color.white));
        placeNewPiece('e', 1, new King(board, Color.white, this));
        placeNewPiece('f', 1, new Bishop(board, Color.white));
        placeNewPiece('g', 1, new Knight(board, Color.white));
        placeNewPiece('h', 1, new Rook(board, Color.white));
        placeNewPiece('a', 2, new Pawn(board, Color.white, this));
        placeNewPiece('b', 2, new Pawn(board, Color.white, this));
        placeNewPiece('c', 2, new Pawn(board, Color.white, this));
        placeNewPiece('d', 2, new Pawn(board, Color.white, this));
        placeNewPiece('e', 2, new Pawn(board, Color.white, this));
        placeNewPiece('f', 2, new Pawn(board, Color.white, this));
        placeNewPiece('g', 2, new Pawn(board, Color.white, this));
        placeNewPiece('h', 2, new Pawn(board, Color.white, this));

        placeNewPiece('a', 8, new Rook(board, Color.black));
        placeNewPiece('b', 8, new Knight(board, Color.black));
        placeNewPiece('c', 8, new Bishop(board, Color.black));
        placeNewPiece('d', 8, new Queen(board, Color.black));
        placeNewPiece('e', 8, new King(board, Color.black, this));
        placeNewPiece('f', 8, new Bishop(board, Color.black));
        placeNewPiece('g', 8, new Knight(board, Color.black));
        placeNewPiece('h', 8, new Rook(board, Color.black));
        placeNewPiece('a', 7, new Pawn(board, Color.black, this));
        placeNewPiece('b', 7, new Pawn(board, Color.black, this));
        placeNewPiece('c', 7, new Pawn(board, Color.black, this));
        placeNewPiece('d', 7, new Pawn(board, Color.black, this));
        placeNewPiece('e', 7, new Pawn(board, Color.black, this));
        placeNewPiece('f', 7, new Pawn(board, Color.black, this));
        placeNewPiece('g', 7, new Pawn(board, Color.black, this));
        placeNewPiece('h', 7, new Pawn(board, Color.black, this));
    }
}