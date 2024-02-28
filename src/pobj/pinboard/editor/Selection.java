package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;

public class Selection {
	private List<Clip> list;
	
	public Selection() {
		this.list = new ArrayList<>();
	}
	
	public void select(Board board, double x, double y) {
		list.clear();
		
		for (Clip clip: board.getContents()) {
			if (clip.isSelected(x, y)) {
				list.add(clip);
				break;
			}
		}
	}
	
	public void toggleSelect(Board board, double x, double y) {
		for (Clip clip: board.getContents()) {
			if (clip.isSelected(x, y)) {
				if (list.contains(clip)) list.remove(clip);
				else list.add(clip);
			}
		}
	}
	
	public void clear() {
		list.clear();
	}
	
	public List<Clip> getContents() {
		return list;
	}
	
	public void drawFeedback(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
        for (Clip clip : list) {
            clip.draw(gc);
        }
	}
}
