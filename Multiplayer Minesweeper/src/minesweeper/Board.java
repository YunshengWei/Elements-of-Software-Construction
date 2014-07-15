package minesweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Board {
	
	private final boolean[][] board;
	private final char[][] state;
	private final int sizeX;
	private final int sizeY;
	
	// construct a Board from file
	public Board(File file) throws IOException {
		try (
				BufferedReader br = new BufferedReader(new FileReader(file));
			) {
				String line = br.readLine();
				sizeX = Integer.parseInt(line.split(" ")[0]);
				sizeY = Integer.parseInt(line.split(" ")[1]);
				board = new boolean[sizeY][sizeX];
				state = new char[sizeY][sizeX];
				for (int i = 0; i < sizeY; i++) {
					line = br.readLine();
					String[] grids = line.split(" ");
					for (int j = 0; j < sizeX; j++) {
						board[i][j] = grids[j].equals("1");
						state[i][j] = '-';
					}
				}
			}
	}
	
	// construct a random Board of size sizeX * sizeY
	public Board(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		board = new boolean[sizeY][sizeX];
		state = new char[sizeY][sizeX];
		Random generator = new Random();
		for (int i = 0; i < sizeY; i++) {
			for (int j = 0; j < sizeX; j++) {
				board[i][j] = generator.nextBoolean();
				state[i][j] = '-';
			}
		}
	}
	
	// return whether (x, y) is a valid position
	private boolean validPos(int x, int y) {
		return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
	}
	
	// return count of bombs in 3 * 3 neighborhood of (x, y) ((x, y) excluded)
	private int bombCountNeighbor(int x, int y) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (validPos(x, y) && board[y][x]) {
					count += 1;
				}
			}
		}
		return count;
	}
	
	// (x, y) must be untouched
	// (x, y) must be in range
	public void setFlag(int x, int y) {
		if (state[y][x] == '-') {
			state[y][x] = 'F';
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	// (x, y) must be flagged
	// (x, y) must be in range
	public void deFlag(int x, int y) {
		if (state[y][x] == 'F') {
			state[y][x] = '-';
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	// dig (x, y) and update state
	// remove bomb in (x, y) if any
	// return true if board[y][x] has bomb else false
	// (x, y) must be in range
	public boolean dig(int x, int y) {
		if (state[y][x] != '-') {
			throw new UnsupportedOperationException();
		}
		boolean hasBomb = board[y][x];
		board[y][x] = false;
		// TODO Finish the dig procedure
		state[y][x] = 1;
		return hasBomb;
	}
	
	// (x, y) must be in range
	public char getState(int x, int y) {
		return state[y][x];
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	// a string representation of the board's state	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (char[] sa : state) {
			for (char s : sa) {
				sb.append(s);
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		Board b = new Board(5, 3);
		System.out.println(b.toString());
	}
}
