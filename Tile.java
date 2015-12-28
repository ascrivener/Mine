public class Tile{
	public boolean isBomb;
	public boolean isFlipped;
	public int adjacent_bomb_count;
	public Location location;

	public Tile(boolean isBomb,boolean isFlipped,int adjacent_bomb_count, Location location){
		this.isBomb = isBomb;
		this.isFlipped = isFlipped;
		this.adjacent_bomb_count = adjacent_bomb_count;
		this.location = location;
	}

	public boolean inBounds(int board_size){
		return location.inBounds(board_size);
	}
}