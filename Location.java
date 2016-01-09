public class Location{
	public int row;
	public int col;

	public Location(int row, int col){
		this.row = row;
		this.col = col;
	}

	public String toString(){
		return this.row + " " + this.col;
	}

	public boolean inBounds(int board_size){
		int row = this.row;
		int col = this.col;
		return row >= 0 && row < board_size && col >= 0 && col < board_size;
	}
}