package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pobj.pinboard.document.ClipEllipse;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.commands.CommandAdd;

public class ToolEllipse implements Tool {
    private double startX, startY, endX, endY;

    @Override
    public void press(EditorInterface i, MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        endX = startX;
        endY = startY;
    }

    @Override
    public void drag(EditorInterface i, MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
    }

    @Override
    public void release(EditorInterface i, MouseEvent e) {
    	if (startX > e.getX()) {
        	endX = startX;
        	startX = e.getX();
        } else {
        	endX = e.getX();
        }
        
        if (startY > e.getY()) {
        	endY = startY;
        	startY = e.getY();
        } else {
        	endY = e.getY();
        }
        //i.getBoard().addClip(new ClipEllipse(startX, startY, endX, endY, Color.RED));
        
        ClipEllipse clip = new ClipEllipse(startX, startY, endX, endY, Color.BLUE);
        CommandAdd addCommand = new CommandAdd(i, clip);
        addCommand.execute();
        i.getUndoStack().addCommand(addCommand);
    }

    @Override
    public void drawFeedback(EditorInterface i, GraphicsContext gc) {
        gc.strokeOval(startX, startY, endX - startX, endY - startY);
    }

    @Override
    public String getName(EditorInterface i) {
        return "Ellipse";
    }
}
