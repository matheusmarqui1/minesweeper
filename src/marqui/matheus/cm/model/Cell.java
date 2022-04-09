package marqui.matheus.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import marqui.matheus.cm.exception.ExplosionException;

public class Cell {
	
	private final int line;
	private final int column;
	
	private boolean opened = false;
	private boolean mined = false;
	private boolean marked = false;
	
	private List<Cell> neighbors = new ArrayList<Cell>();
	
	Cell(int line, int column){
		this.column = column;
		this.line = line;
	}
	
	boolean addNeighbor(Cell neighbor) {
		if(neighbor == null || !(neighbor instanceof Cell))
			throw new IllegalArgumentException("The field 'neighbor' needs to be a cell.");
		
		boolean diagonal = neighbor.line != this.line && neighbor.column != this.column;
			
		int distanceBetweenColumns = Math.abs(neighbor.column - this.column);
		int distanceBetweenLines = Math.abs(neighbor.line - this.line);
	
		int distanceBetweenCells = distanceBetweenLines + distanceBetweenColumns;
			
		if(!diagonal && distanceBetweenCells == 1) {
			neighbors.add(neighbor);
			return true;
		}else if (diagonal && distanceBetweenCells == 2) {
			neighbors.add(neighbor);
			return true;
		}else return false;
	}
	
	void toggleMark() {
		if(!this.opened) {
			this.marked = !this.marked;
		}
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	void setOpened(boolean opened) {
		this.opened = opened;
	}
	
	boolean open() {
		if(!opened && !marked) {
			this.opened = true;
			
			if(this.mined) throw new ExplosionException();
			
			if(this.secureNeighborhood()) {
				neighbors.forEach((n) -> n.open());
			}
			return true;
		}
		return false;
	}
	
	public boolean isOpened() {
		return this.opened;
	}
	
	void mine() {
		this.mined = true;
	}
	
	public boolean isMined() {
		return mined;
	}

	boolean secureNeighborhood() {
		Predicate<Cell> isNeighborSecure = (n) -> !n.mined;
		return neighbors.stream().allMatch(isNeighborSecure);
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	
	boolean goalReached() {
		boolean isDiscoved = !this.mined && this.opened;
		boolean isProtected = this.mined && this.marked;
		
		return isDiscoved ^ isProtected;
	}
	
	long minesInNeighborhood() {
		return neighbors.stream().filter((n) -> n.mined).count();
	}
	
	void restart() {
		this.opened = false;
		this.mined = false;
		this.marked = false;
	}
	
	public String toString() {
		if(this.marked) 
			return "x";
		else if(this.opened && this.mined) 
			return "*";
		else if(this.opened && this.minesInNeighborhood() > 0)
			return Long.toString(this.minesInNeighborhood());
		else if (this.opened)
			return " ";
		else return "?";
		
	}
	
}
