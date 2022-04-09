package marqui.matheus.cm;

import marqui.matheus.cm.model.Board;
import marqui.matheus.cm.view.BoardConsole;

public class App {
	public static void main(String[] args) {
		Board board = new Board(6, 6, 3);
		new BoardConsole(board);
	}
}
