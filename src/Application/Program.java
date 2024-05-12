package Application;

import Chess.ChessException;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while (true) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());

                System.out.println();
                System.out.print("Source: ");
                ChessPosition soruce = UI.ReadChessPosition(sc);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.ReadChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.PerfomChessMove(soruce, target);
            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }
}
