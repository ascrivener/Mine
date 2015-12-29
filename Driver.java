import java.util.*;
public class Driver{
	public static void main(String [] args){
		Game game = new Game(10,10);

		while(true){
			game.display();
			if (!game.makeMove(game.getMove()))
				break;
			if (game.finished()){
				game.display();
				System.out.println("You win!");
				break;
			}
		}
	}
}