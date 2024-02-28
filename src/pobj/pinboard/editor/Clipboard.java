package pobj.pinboard.editor;

import pobj.pinboard.document.Clip;

import java.util.ArrayList;
import java.util.List;

public class Clipboard {
    private static Clipboard instance = new Clipboard();
    private List<Clip> copiedClips;
    private List<ClipboardListener> listeners;

    private Clipboard() {
        copiedClips = new ArrayList<>();
        listeners = new ArrayList<>();

    }

    public static Clipboard getInstance() {
        return instance;
    }

    public void copyToClipboard(List<Clip> clips) {
        copiedClips.clear();
        for (Clip clip : clips) {
            copiedClips.add(clip.copy());
        }
        notifyListeners();
    }

    public List<Clip> copyFromClipboard() {
    	List<Clip> copiedClipsCopy = new ArrayList<>();
        for (Clip clip : copiedClips) {
            copiedClipsCopy.add(clip.copy());
        }
        return copiedClipsCopy;
    }

    public void clear() {
        copiedClips.clear();
        notifyListeners();
    }

    public boolean isEmpty() {
        return copiedClips.isEmpty();
    }
    
    public void addListener(ClipboardListener listener) {
        listeners.add(listener);

    }

    public void removeListener(ClipboardListener listener) {
        listeners.remove(listener);
        
    }

    public void notifyListeners() {
        for (ClipboardListener listener : listeners) {
            listener.clipboardChanged();
        }
    }
}
