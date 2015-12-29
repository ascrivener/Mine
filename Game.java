import java.util.*;
public class Game{
	Board board;
	public Game(int board_size, int bomb_count){
		board = new Board(board_size,bomb_count);
	}

	public void display(){
		board.display();
	}

	public Location getMove(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter row:");
		int row = scan.nextInt();
		System.out.println("Enter column:");
		int col = scan.nextInt();
		return new Location(row,col);
	}

	public boolean makeMove(Location location){
		Tile tile = board.tiles[location.row][location.col];
		flip(tile);
		if (tile.isBomb){
			System.out.println("BOOM!");
			return false;
		}
		propogate_flips(tile);
		return true;
	}

	public void propogate_flips(Tile tile){
		if (tile.adjacent_bomb_count==0){
			for (Tile t : board.getAdjacentTiles(tile)){
				if (!t.isFlipped){
					flip(t);
					propogate_flips(t);
				}
			}
		}
	}

	public void flip(Tile tile){
		if (!tile.isFlipped){
			tile.isFlipped = true;
			board.flipped_count++;
		}
	}

	public HashSet<Tile> getKnownBombs(ArrayList<HashSet<Tile>> config_list){
		if (config_list.size() == 0)
			return new HashSet<Tile>();
		else{
			s = config_list.get(0);
			for (HashSet<Tile> h : config_list){
				s.retainAll(h);
			}
		}
		return s;
	}

	public HashSet<Tile> getKnownFree(ArrayList<HashSet<Tile>> config_list,Tile t){
		HashSet<Tile> adjacent_tiles = new HashSet<Tile>(board.getAdjacentTiles(t));
		if (config_list.size() == 0)
			return adjacent_tiles;
		else{
			s = config_list.get(0);
			for (HashSet<Tile> h : config_list){
				s.addAll(h);
			}
		}
		return adjacent_tiles.removeAll(s);
	}

	public boolean finished(){
		return Math.pow(board.board_size,2)-board.flipped_count == board.bomb_count;
	}
}