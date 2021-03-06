package minesweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.ArrayList;

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
	
	// return all neighbors of (x, y) as an ArrayList<Integer[]>
	private ArrayList<Integer[]> getNeighbors(int x, int y) {
	    ArrayList<Integer[]> neighbors = new ArrayList<Integer[]>();
	    for (int i = -1; i <= 1; i++) {
	        for (int j = -1; j <= 1; j++) {
	            if (validPos(x + i, y +j) && (i != 0 || j != 0)) {
	                neighbors.add(new Integer[] {x + i, y + j});
	            }
	        }
	    }
	    return neighbors;
	}
	
	// return count of bombs in 3 * 3 neighborhood of (x, y) ((x, y) excluded)
	private synchronized int bombCountNeighbor(int x, int y) {
		int count = 0;
		for (Integer[] pos : getNeighbors(x, y)) {
		    if (board[pos[1]][pos[0]] == true) {
		        count += 1;
		    }
		}
		return count;
	}
	
	// (x, y) must be untouched
	// (x, y) must be in range
	public synchronized void setFlag(int x, int y) {
		if (state[y][x] == '-') {
			state[y][x] = 'F';
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	// (x, y) must be flagged
	// (x, y) must be in range
	public synchronized void deFlag(int x, int y) {
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
	public synchronized boolean dig(int x, int y) {
		if (state[y][x] != '-') {
			throw new UnsupportedOperationException();
		}
		boolean hasBomb = board[y][x];
		if (hasBomb) {
		    board[y][x] = false;
		    for (Integer[] pos : getNeighbors(x, y)) {
		        int tx = pos[0];
		        int ty = pos[1];
		        if (state[ty][tx] != '-'
		            && state[ty][tx] != 'F') {
		            state[ty][tx] -= 1;
		        }
		    }
		}
		
	    ArrayDeque<Integer[]> queue = new ArrayDeque<Integer[]>();
	    HashSet<String> used = new HashSet<String>();
	    queue.add(new Integer[] {x, y});
	    used.add(String.format("%s,%s", x, y));
	    while (!queue.isEmpty()) {
	        Integer[] pos = queue.poll();
	        int tx = pos[0];
	        int ty = pos[1];
	        int bombCount = bombCountNeighbor(tx, ty);
	        if (bombCount == 0) {
	            state[ty][tx] = ' ';
	            for (Integer[] tpos : getNeighbors(tx, ty)) {
	                if (!used.contains(String.format("%s,%s", tpos[0], tpos[1]))) {
	                    queue.add(new Integer[] {tpos[0], tpos[1]});
	                    used.add(String.format("%s,%s", tpos[0], tpos[1]));
	                }
	            }
	        } else {
	            state[ty][tx] = (char) ('0' + (char) bombCount);
	        }
	    }
	    
		return hasBomb;
	}
	
	// (x, y) must be in range
	public synchronized char getState(int x, int y) {
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
	public synchronized String toString() {
		StringBuilder sb = new StringBuilder();
		for (char[] sa : state) {
			for (char s : sa) {
				sb.append(s);
				sb.append(' ');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		Board b = new Board(5, 3);
		System.out.println(b.toString());
	}
}
