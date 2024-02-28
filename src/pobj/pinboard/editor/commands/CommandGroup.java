package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

import java.util.List;

public class CommandGroup implements Command {
    private EditorInterface editor;
    private List<Clip> clips;
    private ClipGroup group;

    public CommandGroup(EditorInterface editor, List<Clip> clips) {
        this.editor = editor;
        this.clips = clips;
    }

    @Override
    public void execute() {
        // Créer un groupe avec les clips sélectionnés
        group = new ClipGroup();
        for (Clip clip: clips) {
        	
        	group.addClip(clip);
        }

        // Retirer les clips individuels de la planche
        for (Clip clip : clips) {
            editor.getBoard().removeClip(clip);
        }
        //clips.add(group);

        // Ajouter le groupe à la planche
        editor.getBoard().addClip(group);
    }

    @Override
    public void undo() {
        // Retirer le groupe de la planche
        editor.getBoard().removeClip(group);
        //clips.remove(clips.size() - 1);
        // Réintroduire les clips individuels sur la planche
        for (Clip clip : clips) {
            editor.getBoard().addClip(clip);
        }
    }
}
