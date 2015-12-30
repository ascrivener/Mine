import java.util.*;
public class Driver{
	public static void main(String [] args){
		Game game = new Game(10,10);

		while(true){
			game.display();
			game.displayKnown();
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