package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipGroup extends AbstractClip implements Composite {
	
	private List<Clip> clips;

    public ClipGroup() {
		super(0, 0, 0, 0, Color.GREEN);
        clips = new ArrayList<>();
    }
    
    @Override
    public List<Clip> getClips() {
        return clips;
    }

    @Override
    public void draw(GraphicsContext ctx) {
        for (Clip clip : clips) {
            clip.draw(ctx);
        }
        //updateBoundingBox();
    }

    @Override
    public Clip copy() {
        ClipGroup copiedGroup = new ClipGroup();
        for (Clip clip : clips) {
            copiedGroup.addClip(clip.copy());
        }
        return copiedGroup;
    }


    @Override
    public void addClip(Clip toAdd) {
        clips.add(toAdd);
        updateBoundingBox();
    }

    @Override
    public void removeClip(Clip toRemove) {
        clips.remove(toRemove);
        updateBoundingBox(); // Mettre à jour le rectangle englobant après la suppression
    }
    
    private void updateBoundingBox() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Clip clip : clips) {
            //if (clip instanceof ClipRect) {
                //ClipRect rect = (ClipRect) clip;
        	if (clip != null) {
        		minX = Math.min(minX, clip.getLeft());
        		minY = Math.min(minY, clip.getTop());
        		maxX = Math.max(maxX, clip.getRight());
        		maxY = Math.max(maxY, clip.getBottom());        		
        	}
            //}
        }
        setGeometry(minX, minY, maxX, maxY);
        
        //boundingBox = new ClipRect(minX, minY, maxX, maxY);
    }
    
    @Override
    public void move(double x, double y) {
    	for (Clip clip: clips) {
    		clip.setGeometry(clip.getLeft() + x, clip.getTop() + y, clip.getRight() + x, clip.getBottom() + y);
    	}
    	updateBoundingBox();
	}


}
