package marqui.matheus.cm.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import marqui.matheus.cm.exception.ExplosionException;

public class CellTest {
	private Cell cell;
	
	@BeforeEach
	void initCell() {
		cell = new Cell(3, 3);
	}
	
	@Test
	void testTrueNeighborCrossPosition() {
		List<Cell> neighbors = Arrays.asList(
				new Cell(2,3),
				new Cell(3,2),
				new Cell(4,3),
				new Cell(3,4)
		);
		
		boolean result = true;
		
		for (Cell neighborCell : neighbors) {
			if(!cell.addNeighbor(neighborCell)) {
				result = false;
				break;
			}
		}
		
		assertTrue(result);
	}
	
	@Test
	void testTrueNeighborDiagonalPosition() {
		List<Cell> neighbors = Arrays.asList(
				new Cell(2,2),
				new Cell(2,4),
				new Cell(4,2),
				new Cell(4,4)
		);
		
		boolean result = true;
		
		for (Cell neighborCell : neighbors) {
			if(!cell.addNeighbor(neighborCell)) {
				result = false;
				break;
			}
		}
		
		assertTrue(result);
	}
	
	@Test
	void testNullNeighborShouldThrowException() {
		Cell neighborCell = null;
		assertThrows(IllegalArgumentException.class, 
				() -> {
					cell.addNeighbor(neighborCell);
				}
		);
	}
	
	@Test
	void testFalseNeighbor() {
		List<Cell> neighbors = Arrays.asList(
				new Cell(1,3),
				new Cell(1,4),
				new Cell(3,3),
				new Cell(5,2),
				new Cell(5,3)
		);
		
		boolean result = false;
		
		for (Cell neighborCell : neighbors) {
			if(cell.addNeighbor(neighborCell)) {
				 result = true;
			}
		}
		
		assertFalse(result);
	}
	
	@Test
	void testInitialValueMarked() {
		assertFalse(cell.isMarked());
	}
	
	@Test
	void testToggleMark() {
		cell.toggleMark();
		assertTrue(cell.isMarked());
	}
	
	@Test
	void testDoubleToggleMark() {
		cell.toggleMark();
		cell.toggleMark();
		assertFalse(cell.isMarked());
	}
	
	@Test
	void testOpenNotMinedAndNotMarked() {
		assertTrue(cell.open());
	}
	
	@Test
	void testOpenNotMinedAndMarked() {
		cell.toggleMark();
		assertFalse(cell.open());
	}
	
	@Test
	void testOpenMinedAndMarked() {
		cell.toggleMark();
		cell.mine();
		assertFalse(cell.open());
	}
	
	@Test
	void testOpenMinedAndNotMarked() {
		cell.mine();
		assertThrows(ExplosionException.class, () -> cell.open());
	}
	
	@Test
	void testOpenWithNeighbors() {
		Cell cell11 = new Cell(1, 1);
		Cell cell22 = new Cell(2, 2);
		
		cell22.addNeighbor(cell11);
		
		cell.addNeighbor(cell22);
		
		cell.open();
		assertTrue(cell11.isOpened() && cell22.isOpened());
	}
	
	@Test
	void testOpenWithMinedNeighbors() {
		Cell cell11 = new Cell(1, 1);
		Cell cell12 = new Cell(1, 2);
		cell12.mine();
		
		Cell cell22 = new Cell(2, 2);
		cell22.addNeighbor(cell11);
		cell22.addNeighbor(cell12);
		
		cell.addNeighbor(cell22);
		cell.open();
		
		assertTrue(!cell11.isOpened() && cell22.isOpened());
	}
	
	
}
