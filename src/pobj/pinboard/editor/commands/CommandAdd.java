package pobj.pinboard.editor.commands;

import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.document.Clip;

import java.util.ArrayList;
import java.util.List;

public class CommandAdd implements Command {
    private final EditorInterface editor;
    private final List<Clip> clipsToAdd;
    private final List<Clip> addedClips;

    public CommandAdd(EditorInterface editor, Clip toAdd) {
        this.editor = editor;
        this.clipsToAdd = new ArrayList<>();
        this.clipsToAdd.add(toAdd);
        this.addedClips = new ArrayList<>();
    }

    public CommandAdd(EditorInterface editor, List<Clip> toAdd) {
        this.editor = editor;
        this.clipsToAdd = new ArrayList<>(toAdd);
        this.addedClips = new ArrayList<>();
    }

    @Override
    public void execute() {
        for (Clip clip : clipsToAdd) {
            editor.getBoard().addClip(clip);
            addedClips.add(clip);
        }
    }

    @Override
    public void undo() {
        for (Clip clip : addedClips) {
            editor.getBoard().removeClip(clip);
        }
        addedClips.clear();
    }
}
