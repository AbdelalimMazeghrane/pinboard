package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {
	private List<Clip> clips;
	
	public Board() {
		clips = new ArrayList<>();
	}
	
	public List<Clip> getContents() {
		return clips;
	}
	
	public void addClip(Clip clip) {
		clips.add(clip);
	}
	
	public void addClip(List<Clip> clip) {
		for(Clip c: clip) {
			addClip(c);
		}
	}
	
	public void removeClip(Clip clip) {
		clips.remove(clip);
	}
	
	public void removeClip(List<Clip> clip) {
		for(Clip c: clip) {
			removeClip(c);
		}
	}
	
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		for (Clip clip: clips) {
			clip.draw(gc);
		}
	}

}
