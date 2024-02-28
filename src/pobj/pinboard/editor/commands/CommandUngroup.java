package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

import java.util.ArrayList;
import java.util.List;

public class CommandUngroup implements Command {
    private EditorInterface editor;
    private ClipGroup group;
    private List<Clip> clips;

    public CommandUngroup(EditorInterface editor, ClipGroup group) {
        this.editor = editor;
        this.group = group;
        clips = group.getClips();
    }

    @Override
    public void execute() {
        // Récupérer les clips du groupe
        //List<Clip> clips = new ArrayList<>(group.getClips());

        // Retirer le groupe de la planche
        editor.getBoard().removeClip(group);

        // Ajouter les clips individuels à la planche
        for (Clip clip : clips) {
            editor.getBoard().addClip(clip);
        }
    }

    @Override
    public void undo() {
        // Créer un nouveau groupe avec les clips individuels
        group = new ClipGroup();
        for (Clip clip: clips) {
        	
        	group.addClip(clip);
        }

        // Retirer les clips individuels de la planche
        for (Clip clip : clips) {
            editor.getBoard().removeClip(clip);
        }

        // Ajouter le nouveau groupe à la planche
        editor.getBoard().addClip(group);
    }
}

