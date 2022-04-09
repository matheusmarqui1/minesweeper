package marqui.matheus.cm.model;

import java.util.ArrayList;
import java.util.List;

import marqui.matheus.cm.exception.ExplosionException;

public class Board {
	private int lines;
	private int columns;
	private int minesQty;
	
	final private List<Cell> cells =  new ArrayList<Cell>();

	public Board(int lines, int columns, int minesQty) {
		this.lines = lines;
		this.columns = columns;
		this.minesQty = minesQty;
		
		generateCells();
		associateNeighbors();
		putRandomMines();
	}
	
	public void open(int line, int column) {
		try {
			cells.parallelStream()
				.filter(c -> c.getLine() == line && c.getColumn() == column)
				.findFirst()
				.ifPresent(c -> c.open());
		} catch (ExplosionException e) {
			cells.forEach(c -> c.setOpened(true));
			throw e;
		}
	}
	
	public void toggleMark(int line, int column) {
		cells.parallelStream()
			.filter(c -> c.getLine() == line && c.getColumn() == column)
			.findFirst()
			.ifPresent(c -> c.toggleMark());
	}

	private void generateCells() {
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				cells.add(new Cell(i, j));
			}
		}
	}
	
	private void associateNeighbors() {
		for (Cell cell1 : cells) {
			for (Cell cell2 : cells) {
				cell1.addNeighbor(cell2);
			}
		}
	}
	
	private void putRandomMines() {
		long armedMines = 0;
		
		do {
			int aleatoryIndex = (int) (Math.random() * cells.size());
			cells.get(aleatoryIndex).mine();
			armedMines = cells.stream().filter((c) -> c.isMined()).count();
		} while(armedMines < minesQty);
	}
	
	public boolean goalReached() {
		return cells.stream().allMatch(c -> c.goalReached());
	}
	
	public void restart() {
		cells.stream().forEach(c -> c.restart());
		putRandomMines();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int k = 0;
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				sb.append(" ");
				sb.append(cells.get(k));
				sb.append(" ");
				k++;
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
}
