package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class ToolSelection implements Tool {
    private double lastX, lastY;

    @Override
    public void press(EditorInterface i, MouseEvent e) {
    	lastX = e.getX();
    	lastY = e.getY();
        if (e.isShiftDown()) {
            i.getSelection().toggleSelect(i.getBoard(), e.getX(), e.getY());
        } else {
            i.getSelection().select(i.getBoard(), e.getX(), e.getY());
        }
    }

    @Override
    public void drag(EditorInterface i, MouseEvent e) {
        double offsetX = e.getX() - lastX;
        double offsetY = e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();

        for (Clip clip : i.getSelection().getContents()) {
            clip.move(offsetX, offsetY);
            //clip.draw(null);
        }
        //i.getBoard().draw(); // Mettre à jour l'affichage après le déplacement
    }

    @Override
    public void release(EditorInterface i, MouseEvent e) {
        // Réinitialiser les valeurs après le relâchement du bouton de la souris
        lastX = 0;
        lastY = 0;
    }

    @Override
    public void drawFeedback(EditorInterface i, GraphicsContext gc) {
        i.getSelection().drawFeedback(gc);
    }

    @Override
    public String getName(EditorInterface i) {
        return "Selection Tool";
    }

}
