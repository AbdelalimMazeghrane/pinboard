package pobj.pinboard.editor;
import pobj.pinboard.editor.commands.*;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipEllipse;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.document.ClipRect;
import pobj.pinboard.editor.tools.Tool;
import pobj.pinboard.editor.tools.ToolEllipse;
import pobj.pinboard.editor.tools.ToolRect;
import pobj.pinboard.editor.tools.ToolSelection;

public class EditorWindow extends javafx.application.Application implements EditorInterface, ClipboardListener {
	private Board board;
	private Canvas canvas;
	private Tool currentTool;
	private Selection selection;
	private MenuItem pasteItem;
	private CommandStack undoStack = new CommandStack();
	
	public EditorWindow(Stage stage) {
		
		selection = new Selection();
		
		currentTool = new ToolRect();
		
		// Menu Bar
		Menu menuFile = new Menu("File");
		MenuItem newItem = new MenuItem("New");
		MenuItem closeItem = new MenuItem("Close");
		menuFile.getItems().addAll(newItem, closeItem);
		
		
		newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                new EditorWindow(new Stage());
            }
        });
		
		closeItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	
                stage.close();
            }
        });
		
		Menu menuEdit = new Menu("Edit");
		Menu menuTools = new Menu("Tools");
		MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuTools);
		
		MenuItem copyItem = new MenuItem("Copy");
		pasteItem = new MenuItem("Paste");
		pasteItem.setDisable(true); 
		MenuItem deleteItem = new MenuItem("Delete");
		
		MenuItem groupItem = new MenuItem("Group");
		MenuItem ungroupItem = new MenuItem("Ungroup");
		
		MenuItem undoItem = new MenuItem("Undo");
		undoItem.setOnAction(e -> {
		    getUndoStack().undo();
		});

		MenuItem redoItem = new MenuItem("Redo");
		redoItem.setOnAction(e -> {
		    getUndoStack().redo();
		});


		menuEdit.getItems().addAll(copyItem, pasteItem, deleteItem,groupItem, ungroupItem, undoItem, redoItem);

		copyItem.setOnAction(e -> {
		    Clipboard.getInstance().copyToClipboard(selection.getContents());
		    //clipboardChanged();
		    //Clipboard.getInstance().notifyListeners();
		});

		pasteItem.setOnAction(e -> {
		    /*List<Clip> copiedClips = Clipboard.getInstance().copyFromClipboard();
		    
		    if (!copiedClips.isEmpty()) {
		        for (Clip clip : copiedClips) {
		            board.addClip(clip);
		        }
		        draw();
		    }*/
		    
		    List<Clip> clipsToPaste = Clipboard.getInstance().copyFromClipboard();
	        Command pasteCommand = new CommandAdd(this, clipsToPaste);
	        pasteCommand.execute();
	        getUndoStack().addCommand(pasteCommand);
		});

		deleteItem.setOnAction(e -> {
		    List<Clip> selectedClips = selection.getContents();
		    
		    Command deleteCommand = new CommandDelete(this, selectedClips);
		    deleteCommand.execute();
		    getUndoStack().addCommand(deleteCommand);
		    
		    /*board.removeClip(selectedClips);
		    selection.clear();
		    draw();**/
		    //Clipboard.getInstance().notifyListeners();
		    //clipboardChanged();
		});

		//menuBar.getMenus().add(menuEdit);
		
		

		groupItem.setOnAction(e -> {
		    // Appel à la méthode pour grouper les éléments sélectionnés
		    /*groupSelectedItems();
		    draw(); // Redessiner après le groupement*/
		    
		    List<Clip> selectedClips = selection.getContents();
		    if (selectedClips.size() > 1) {
		        Command groupCommand = new CommandGroup(this, selectedClips);
		        groupCommand.execute();
		        getUndoStack().addCommand(groupCommand);
		    }
		});

		ungroupItem.setOnAction(e -> {
		    // Appel à la méthode pour dégrouper les éléments sélectionnés
		    /*ungroupSelectedItems();
		    draw(); // Redessiner après le dégroupement*/
		    
		    List<Clip> selectedClips = selection.getContents();
		    if (selectedClips.size() == 1 && selectedClips.get(0) instanceof ClipGroup) {
		        ClipGroup groupToUngroup = (ClipGroup) selectedClips.get(0);
		        Command ungroupCommand = new CommandUngroup(this, groupToUngroup);
		        ungroupCommand.execute();
		        getUndoStack().addCommand(ungroupCommand);
		    }
		    //getUndoStack().addCommand(ungroupCommand);
		});
		
		

		
		// Tool Bar
		Button boxBtn = new Button("Box");
		Button ellipseBtn = new Button("Ellipse");
		Button imgBtn = new Button("Img...");
		ToolBar toolbar = new ToolBar(boxBtn, ellipseBtn, imgBtn);
		
		Button selectBtn = new Button("Select");
		selectBtn.setOnAction(e -> {
		    setCurrentTool(new ToolSelection());
		});
		toolbar.getItems().add(selectBtn);
		
		// Canvas
		canvas = new Canvas(800, 600);
		
		// 
		canvas.setOnMousePressed(e -> {
            if (currentTool != null) {
                currentTool.press(this, e);
                draw();
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (currentTool != null) {
                currentTool.drag(this, e);
                draw();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (currentTool != null) {
                currentTool.release(this, e);
                draw();
            }
        });
        // Footer
        Separator sep = new Separator();
        Label footer = new Label("Filled rectangle tool");
        
        boxBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentTool = new ToolRect();
                footer.textProperty().set("Rectangle");
            }
        });
        
		ellipseBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentTool = new ToolEllipse();
                footer.textProperty().set("Ellipse");
            }
        });
		
		imgBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentTool = null;
                footer.textProperty().set("Tmage");
            }
        });
		
		
		// vbox
		VBox vbox = new VBox();
		vbox.getChildren().add(menuBar);
		vbox.getChildren().add(toolbar);
		vbox.getChildren().add(canvas);
		vbox.getChildren().add(sep);
		vbox.getChildren().add(footer);
		
		stage.setTitle("Pin Board");
		stage.setScene(new javafx.scene.Scene(vbox));
		board = new Board();
		stage.show();
		
		Clipboard.getInstance().addListener(this);
	}
	
	private void groupSelectedItems() {
	    List<Clip> selectedClips = getSelection().getContents();
	    
	    if (!selectedClips.isEmpty()) {
	        ClipGroup group = new ClipGroup();
	        selectedClips.forEach(group::addClip); // Ajout des éléments sélectionnés au groupe
	        
	        getBoard().addClip(group); // Ajout du groupe à la planche
	        selectedClips.forEach(getBoard()::removeClip); // Retrait des éléments sélectionnés de la planche
	        getSelection().clear(); // Désélection des éléments après le groupement
	    }
	}

	private void ungroupSelectedItems() {
	    List<Clip> selectedClips = getSelection().getContents();
	    
	    if (!selectedClips.isEmpty()) {
	        for (Clip clip : selectedClips) {
	            if (clip instanceof ClipGroup) {
	                ClipGroup group = (ClipGroup) clip;
	                getBoard().removeClip(group); // Retrait du groupe de la planche
	                List<Clip> groupedClips = group.getClips();
	                getBoard().addClip(groupedClips); // Ajout des éléments du groupe à la planche
	            }
	        }
	        getSelection().clear(); // Désélection des éléments après le dégroupement
	    }
	}



	@Override
	public Board getBoard() {
		return board;
	}



	@Override
	public Selection getSelection() {
		// TODO Auto-generated method stub
		return selection;
	}



	@Override
	public CommandStack getUndoStack() {
		//return null;
		return undoStack;
		//return new CommandStack();
	}
	
	public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

	private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessiner la planche
        board.draw(gc);

        // Dessiner le retour d'affichage de l'outil actuel
        if (currentTool != null) {
            currentTool.drawFeedback(this, gc);
        }
    }



	@Override
	public void start(Stage arg0) throws Exception {
		//Clipboard.getInstance().addListener(this);
		
	}
	
	@Override
    public void stop() throws Exception {
        //Clipboard.getInstance().removeListener(this);
    }
	
	@Override
    public void clipboardChanged() {
		if (Clipboard.getInstance().isEmpty()) {
            pasteItem.setDisable(true); 
        } else {
            pasteItem.setDisable(false);
        }
    }
}
