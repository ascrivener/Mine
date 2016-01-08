import java.util.*;
public class Driver{
	public static void main(String [] args){
		Game game = new Game(10,10);

		while(true){
			game.display();
			game.displayKnown();
			// System.out.println(game.board.tiles[0][1]);
			// System.out.println(game.board.tiles[1][1]);
			// System.out.println(game.board.tiles[2][1]);
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