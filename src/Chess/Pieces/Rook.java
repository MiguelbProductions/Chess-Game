package Chess.Pieces;

import BoardGame.Board;
import BoardGame.Position;
import Chess.ChessPiece;
import Chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean[][] PossibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        p.setValues(position.getRow() - 1, position.getColumn());
        while (getBoard().positionExits(p) && !getBoard().therIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setRow(p.getRow() - 1);
        }

        if (getBoard().positionExits(p) && IsOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(position.getRow(), position.getColumn() - 1);
        while (getBoard().positionExits(p) && !getBoard().therIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setColumn(p.getColumn() - 1);
        }

        if (getBoard().positionExits(p) && IsOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(position.getRow(), position.getColumn() + 1);
        while (getBoard().positionExits(p) && !getBoard().therIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setColumn(p.getColumn() + 1);
        }

        if (getBoard().positionExits(p) && IsOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        p.setValues(position.getRow() + 1, position.getColumn());
        while (getBoard().positionExits(p) && !getBoard().therIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setRow(p.getRow() + 1);
        }

        if (getBoard().positionExits(p) && IsOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
}
