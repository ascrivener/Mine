import java.util.*;
public class Board{

	public Tile[][] tiles;
	public int board_size,bomb_count,flipped_count;

	public Board(int board_size, int bomb_count){
		this.board_size = board_size;
		this.bomb_count = bomb_count;
		this.flipped_count = 0;
		tiles = new Tile[board_size][board_size];

		for (int i =0; i < board_size; i++){
			for (int j = 0; j < board_size; j++){
				tiles[i][j] = new Tile(false,false,0,new Location(i,j));
			}
		}

		while (bomb_count > 0){
			int rand_bomb_location = (int)(Math.random()*Math.pow(board_size,2));
			int row = rand_bomb_location/board_size;
			int col = rand_bomb_location%board_size;
			if (!tiles[row][col].isBomb){
				tiles[row][col].isBomb = true;
				bomb_count--;
			}
			// tiles[0][0].isBomb = true;
			// bomb_count--;
			// tiles[2][0].isBomb = true;
			// bomb_count--;
		}
		
		for (int i = 0; i < board_size; i++){
			for (int j = 0; j < board_size; j++){
				tiles[i][j].adjacency_list = getAdjacentTiles(new Location(i,j));
				int adjacent_bomb_count = 0;
				for (Tile tile : tiles[i][j].adjacency_list){
					if (tile.isBomb)
						adjacent_bomb_count++;
				}
				tiles[i][j].config_list = getConfigurations(new ArrayList<Tile>(tiles[i][j].adjacency_list),adjacent_bomb_count);
				tiles[i][j].adjacent_bomb_count = adjacent_bomb_count;
			}
		}
	}

	public ArrayList<HashSet<Tile>> getConfigurations(ArrayList<Tile> adjacent_list,int adjacent_bomb_count){
		if (adjacent_bomb_count==0)
			return new ArrayList<HashSet<Tile>>();
		else if (adjacent_bomb_count==adjacent_list.size()){
			HashSet<Tile> h = new HashSet<Tile>(new ArrayList<Tile>(adjacent_list));
			ArrayList<HashSet<Tile>> output = new ArrayList<HashSet<Tile>>();
			output.add(h);
			return output;
		}
		else{
			Tile firstTile = adjacent_list.remove(0);
			ArrayList<Tile> copy = new ArrayList<Tile>(adjacent_list);
			ArrayList<HashSet<Tile>> bigList = getConfigurations(adjacent_list,adjacent_bomb_count);
			ArrayList<HashSet<Tile>> smallList = getConfigurations(copy,adjacent_bomb_count-1);

			if (smallList.isEmpty()){
				HashSet<Tile> h = new HashSet<Tile>();
				h.add(firstTile);
				smallList.add(h);
			}
			else{
				for (HashSet<Tile> h : smallList){
					h.add(firstTile);
				}
			}

			ArrayList<HashSet<Tile>> newList = new ArrayList<HashSet<Tile>>(bigList);
			newList.addAll(smallList);

			return newList;
		}
	}

	public ArrayList<Tile> getAdjacentTiles(Location location){
		int row = location.row;
		int col = location.col;

		ArrayList<Location> dirs = new ArrayList<Location>();

		dirs.add(new Location(-1,-1));
		dirs.add(new Location(-1,0));
		dirs.add(new Location(-1,1));
		dirs.add(new Location(0,-1));
		dirs.add(new Location(0,1));
		dirs.add(new Location(1,-1));
		dirs.add(new Location(1,0));
		dirs.add(new Location(1,1));

		ArrayList<Tile> adjacent_list = new ArrayList<Tile>();

		for (Location dir : dirs){
			Location loc = new Location(row+dir.row,col+dir.col);
			if (loc.inBounds(board_size))
				adjacent_list.add(tiles[loc.row][loc.col]);
		}

		return adjacent_list;
	}

	public void display(){
		System.out.println();
		for (int i = 0; i < board_size; i++){
			System.out.print(i + " ");
		}
		System.out.println("\n");
		for (int i = 0; i < board_size; i++){
			for (int j = 0; j < board_size; j++){
				String out = tiles[i][j].isFlipped ? "" + tiles[i][j].adjacent_bomb_count : "X";
				System.out.print(out + " ");
			}
			System.out.println(" " + i);
		}
		System.out.println();
	}

	public void displayKnown(){
		System.out.println();
		for (int i = 0; i < board_size; i++){
			System.out.print(i + " ");
		}
		System.out.println("\n");
		for (int i = 0; i < board_size; i++){
			for (int j = 0; j < board_size; j++){
				String value = tiles[i][j].knownFree ? "F" : "X";
				value = tiles[i][j].knownBomb ? "B" : value;
				value = tiles[i][j].isFlipped ? "" + tiles[i][j].adjacent_bomb_count : value;
				System.out.print(value + " ");
			}
			System.out.println(" " + i);
		}
		System.out.println();
	}

}