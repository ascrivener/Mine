import java.util.*;
import org.apache.commons.lang3.builder.*;
public class Tile{
	public boolean isBomb;
	public boolean isFlipped;
	public int adjacent_bomb_count;
	public Location location;
	public ArrayList<HashSet<Tile>> config_list;

	public Tile(boolean isBomb,boolean isFlipped,int adjacent_bomb_count, Location location){
		this.isBomb = isBomb;
		this.isFlipped = isFlipped;
		this.adjacent_bomb_count = adjacent_bomb_count;
		this.location = location;
		config_list = new ArrayList<HashSet<Tile>>();

	}

	public boolean inBounds(int board_size){
		return location.inBounds(board_size);
	}

	public String toString(){
		return location.row + " " + location.col + "\n";
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder(17,31).
		append(location.row).
		append(location.col).
		toHashCode();
	}

	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Tile))
			return false;
		if (obj == this)
			return true;

		Tile rhs = (Tile) obj;

		return new EqualsBuilder().
		append(location.row,rhs.location.row).
		append(location.col,rhs.location.col).
		isEquals();
	}
}