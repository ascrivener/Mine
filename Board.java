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
		}
		

		for (int i = 0; i < board_size; i++){
			for (int j = 0; j < board_size; j++){
				int adjacent_bomb_count = 0;
				for (Tile tile : getAdjacentTiles(tiles[i][j])){
					if (tile.inBounds(board_size) && tile.isBomb)
						adjacent_bomb_count++;
				}
				tiles[i][j].adjacent_bomb_count = adjacent_bomb_count;
			}
		}
	}

	public ArrayList<Tile> getAdjacentTiles(Tile tile){
		Location location = tile.location;
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

}