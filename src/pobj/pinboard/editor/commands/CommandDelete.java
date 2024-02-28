package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

import java.util.ArrayList;
import java.util.List;

public class CommandDelete implements Command {
    private EditorInterface editor;
    private List<Clip> clipsToDelete;

    public CommandDelete(EditorInterface editor, List<Clip> clipsToDelete) {
        this.editor = editor;
        this.clipsToDelete = new ArrayList<>(clipsToDelete);
    }

    @Override
    public void execute() {
        for (Clip clip : clipsToDelete) {
            editor.getBoard().removeClip(clip);
        }
    }

    @Override
    public void undo() {
        for (Clip clip : clipsToDelete) {
            editor.getBoard().addClip(clip);
        }
    }
}
