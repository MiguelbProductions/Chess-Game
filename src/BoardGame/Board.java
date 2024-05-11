package BoardGame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Errror creating board: there must be at least 1 row and 1 column");
        }

        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if (!positionExits(row, column)) {
            throw new BoardException("Position not on the board");
        }

        return pieces[row][column];
    }

    public Piece piece(Position position) {
        return piece(position.getRow(), position.getColumn());
    }

    public void PlacePiece(Piece piece, Position position) {
        if (therIsAPiece(position.getRow(), position.getColumn())) {
            throw new BoardException("There is already a piece on position " + position);
        }

        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public boolean positionExits(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExits(Position position) {
        return positionExits(position.getRow(),position.getColumn());
    }

    public boolean therIsAPiece(int row, int column) {
        if (!positionExits(row, column)) {
            throw new BoardException("Position not on the board");
        }

        return pieces[row][column] != null;
    }

    public boolean therIsAPiece(Position position) {
        return therIsAPiece(position.getRow(), position.getColumn());
    }
}
