import java.util.*;
public class Driver{
	public static void main(String [] args){
		Game game = new Game(22,99);

		while(true){
			game.display();
			game.displayKnown();
			// System.out.println(game.board.tiles[3][2]);
			if (!game.makeMove(game.getMove()))
				break;
			if (game.finished()){
				game.display();
				game.displayKnown();
				System.out.println("You win!");
				break;
			}
		}
	}
}