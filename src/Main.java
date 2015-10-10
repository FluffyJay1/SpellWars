

public class Main {

	public static void main(String[] args) {
		Screen screen = new StartScreen();
		while(screen != null){
			screen = screen.update();
		}
	}
}
