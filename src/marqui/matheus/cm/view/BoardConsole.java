package marqui.matheus.cm.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import marqui.matheus.cm.exception.Exit;
import marqui.matheus.cm.exception.ExplosionException;
import marqui.matheus.cm.model.Board;

public class BoardConsole {
	private Board board;
	private Scanner in;

	public BoardConsole(Board board) {
		this.board = board;
		this.in = new Scanner(System.in);
		
		initGame();
	}

	private void initGame() {
		try {
			boolean keepOn = true;
			while(keepOn) {
				gameCycle();
				System.out.println("Another game? (Y/n)");
				String answer = in.nextLine();
				if("n".equalsIgnoreCase(answer)) {
					keepOn = false;
					throw new Exit();
				}else {
					board.restart();
				}
			}
		} catch (Exit e) {
			System.out.println("Bye bye!");
		} finally {
			in.close();
		}
	}

	private void gameCycle() {
		try {
			while(!board.goalReached()) {
				System.out.println(board);
				
				String capturedText = captureText("Enter (x, y) values: ");
				Iterator<Integer> xy = Arrays.stream(capturedText.split(","))
					.map(e -> Integer.parseInt(e.trim())).iterator();
				
				capturedText = captureText("1 - Open\n2 - (Un)mark\n-> ");
				if("1".equals(capturedText)) {
					board.open(xy.next(), xy.next());
				} else if("2".equals(capturedText)) {
					board.toggleMark(xy.next(), xy.next());
				}
				System.out.println(board);
				System.out.println("You won!");
			}
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("You lose.");
		}
	}

	private String captureText(String text) {
		System.out.print(text);
		String enteredValue = in.nextLine();
		
		if("exit".equalsIgnoreCase(enteredValue)) {
			throw new Exit();
		}
		
		return enteredValue;
	}
	

}
