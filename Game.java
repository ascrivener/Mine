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
		propogate_flips(tile);
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
		//System.out.println("flipping " +  tile.location.row + " " + tile.location.col);
		if (!tile.isFlipped){
			tile.isFlipped = true;
			board.flipped_count++;

			//remove configs that contradict, i.e. two bombs next to a 1

			removeConfigs(tile);


			
			if (!tile.knownFree){
				tile.knownFree = true;
				//remove configs that contradict, i.e. two bombs next to a 1
				removeConfigsFree(tile);
			}

			updateBoardInfo(tile);
		}
	}

	//bad name
	public void removeConfigs(Tile tile){
		//go thru each tile in a 5x5 square around tile and remove accordingly

		HashSet<Tile> field = new HashSet<Tile>();

		field.addAll(tile.adjacency_list);
		for (Tile t : tile.adjacency_list){
			field.addAll(t.adjacency_list);
		}
		field.remove(tile);

		for (Tile t : field){
			if (!t.isBomb){
				if (t.isFlipped){
					//go thru tile's configs, remove accordingly
					affect(t,tile);
				}
				//go thru t's configs, remove accordingly
				affect(tile,t);
				if (t.isFlipped)
					updateBoardInfo(t);
			}
		}

		//necessary?
		updateBoardInfo(tile);
	}

	public void affect(Tile tile1,Tile tile2){
		for (int i = tile2.config_list.size()-1; i>= 0; i--){
			HashSet<Tile> h = new HashSet<Tile>(tile2.config_list.get(i));
			h.retainAll(tile1.adjacency_list);
			if (h.size() > tile1.adjacent_bomb_count){
				tile2.config_list.remove(tile2.config_list.get(i));
			}

			else{
				if (!tile1.config_list.isEmpty()){
					h = new HashSet<Tile>(tile2.adjacency_list);
					h.removeAll(tile2.config_list.get(i));

					boolean flag = false;
					for (HashSet<Tile> h2 : tile1.config_list){
						HashSet<Tile> h3 = new HashSet<Tile>(h);
						h3.retainAll(h2);
						if (h3.isEmpty()) //they do not intersect, so it is consistent.
							flag = true;
					}

					if (!flag){ //intersected with every config
						tile2.config_list.remove(tile2.config_list.get(i));
					}
				}
			}
		}
	}

	//bad name
	public void updateBoardInfo(Tile tile){
		//remove configs that contradict, i.e. two bombs next to a 1
		for (Tile t : tile.getKnownBombs()){
			if (!t.knownBomb){
				t.knownBomb = true;
				removeConfigsBomb(t);
			}
		}
		for (Tile t : tile.getKnownFree()){
			if (!t.knownFree){
				t.knownFree = true;
				removeConfigsFree(t);
			}
		}
	}

	public void removeConfigsBomb(Tile tile){
		for (Tile t : tile.adjacency_list){
			if (!t.isBomb){
				for (int i = t.config_list.size()-1; i >= 0; i--){
					HashSet<Tile> h = t.config_list.get(i);
					if (!h.contains(tile)){
						t.config_list.remove(h);
					}
				}
				if (t.isFlipped)
					updateBoardInfo(t);
			}
		}
	}

	public void removeConfigsFree(Tile tile){
		for (Tile t : tile.adjacency_list){
			if (!t.isBomb){
				for (int i = t.config_list.size()-1; i >= 0; i--){
					HashSet<Tile> h = t.config_list.get(i);
					if (h.contains(tile)){
						t.config_list.remove(h);
					}
				}
				if (t.isFlipped)
					updateBoardInfo(t);
			}
		}
	}

	public boolean finished(){
		return Math.pow(board.board_size,2)-board.flipped_count == board.bomb_count;
	}
}