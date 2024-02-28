package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandMove implements Command {
    private EditorInterface editor;
    private Clip clip;
    private double deltaX;
    private double deltaY;

    public CommandMove(EditorInterface editor, Clip clip, double deltaX, double deltaY) {
        this.editor = editor;
        this.clip = clip;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    @Override
    public void execute() {
        // Déplacer le clip
        clip.move(deltaX, deltaY);
    }

    @Override
    public void undo() {
        // Annuler le déplacement du clip en sens inverse
        clip.move(-deltaX, -deltaY);
    }
}
