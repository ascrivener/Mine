import java.util.*;
public class Game{
	Board board;
	public Game(int board_size, int bomb_count){
		board = new Board(board_size,bomb_count);
	}

	public void display(){
		board.display();
	}

	public void displayKnown(){
		board.displayKnown();
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
		//propogate_flips(tile);
		return true;
	}

	public void propogate_flips(Tile tile){
		if (tile.adjacent_bomb_count==0){
			for (Tile t : tile.adjacency_list){
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

			if (!tile.knownFree){
				tile.knownFree = true;
				removeConfigsFree(tile);
			}

			updateBoardInfo(tile);
		}
	}

	//bad name
	public void updateBoardInfo(Tile tile){
		//make them queues?
		for (Tile t : tile.getKnownBombs()){
			if (!t.knownBomb){
				t.knownBomb = true;
				//remove configs of t's neighbors that don't contain t
				//alter t's configs?
				removeConfigsBomb(t);
			}
		}
		for (Tile t : tile.getKnownFree()){
			if (!t.knownFree){
				t.knownFree = true;
				//remove configs of t's neighbors that do contain t
				//alter t's configs?
				removeConfigsFree(t); //only of flipped tiles?
			}
		}
	}

	public void removeConfigsBomb(Tile tile){
		for (Tile t : tile.adjacency_list){
			if (t.isFlipped){
				for (int i = t.config_list.size()-1; i >= 0; i--){
					HashSet<Tile> h = t.config_list.get(i);
					if (!h.contains(tile)){
						t.config_list.remove(h);
					}
				}
				updateBoardInfo(t);
			}
		}
	}

	public void removeConfigsFree(Tile tile){
		for (Tile t : tile.adjacency_list){
			if (t.isFlipped){
				for (int i = t.config_list.size()-1; i >= 0; i--){
					HashSet<Tile> h = t.config_list.get(i);
					if (h.contains(tile)){
						t.config_list.remove(h);
					}
				}
				updateBoardInfo(t);
			}
		}
	}

	public boolean finished(){
		return Math.pow(board.board_size,2)-board.flipped_count == board.bomb_count;
	}
}