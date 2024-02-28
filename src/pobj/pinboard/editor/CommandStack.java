package pobj.pinboard.editor;

import pobj.pinboard.editor.commands.Command;

import java.util.Stack;

public class CommandStack {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public CommandStack() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void addCommand(Command cmd) {
        undoStack.push(cmd);
        redoStack.clear(); // Vider la pile redo après l'ajout d'une nouvelle commande
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }

    public boolean isUndoEmpty() {
        return undoStack.isEmpty();
    }

    public boolean isRedoEmpty() {
        return redoStack.isEmpty();
    }
}
