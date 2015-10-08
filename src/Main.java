import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RenderWindow window = new RenderWindow(new VideoMode(800,800), "SFML WORKS");
		 CircleShape shape = new CircleShape(100.f);
		    shape.setFillColor(Color.GREEN);

		    while (window.isOpen())
		    {
		        Event event;
		        while ((event = window.pollEvent()) != null)
		        {
		            if (event.type == Event.Type.CLOSED)
		                window.close();
		        }

		        window.clear();
		        window.draw(shape);
		        window.display();
		    }
	}

}
