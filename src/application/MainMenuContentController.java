package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class MainMenuContentController {
	@FXML private Rectangle notepadArea;
	@FXML private Rectangle systemExplorerArea;
	@FXML private Rectangle shutdownArea;
	
	//notepad
	private FXMLLoader notepadLoader;
	private BorderPane notepad;
	private TextWindowController textWindowController;
	private boolean isNotepadOpen;	
	private ImageView notepadMinimized;	
	private OpenNotepadEvent openNotepadListener;
	private CloseNotepadEvent closeNotepadListener;
	
	//system explorer
	private FXMLLoader systemExplorerLoader;
	private BorderPane systemExplorer;
	private SystemExplorerController systemExplorerController;
	private boolean isSystemExplorerOpen;	
	private ImageView systemExplorerMinimized;	
	private OpenSystemExplorerEvent openSystemExplorerListener;
	private CloseSystemExplorerEvent closeSystemExplorerListener;
	
	private ShutDownEvent shutdownListener;
	
	private Screen screen;
	private Rectangle2D bounds;
	
	public MainMenuContentController() {		
		//notepad
		
		notepad = null;
		isNotepadOpen = false;
		
		try {
			 notepadMinimized = new ImageView(new Image(new FileInputStream("Resources/notepad_image.png")));
			 
			 notepadMinimized.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					notepadMinizedPressed();					
				}				 
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//systemExplorer
		
		systemExplorer = null;
		isSystemExplorerOpen = false;
		
		try {
			 systemExplorerMinimized = new ImageView(new Image(new FileInputStream("Resources/system_explorer.png")));
			 
			 systemExplorerMinimized.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					systemExplorerMinimizedPressed();					
				}
				 
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		
	}
	
	public void notepadAreaClicked() {
		if(!isNotepadOpen) {
			isNotepadOpen = true;
			notepadLoader = new FXMLLoader(getClass().getResource("TextWindow.fxml"));
			try {
				openNotepadListener.onOpen(notepadMinimized);
				notepad = notepadLoader.load();
				textWindowController = notepadLoader.getController();
				
				textWindowController.addCloseNotepadListener((image) -> {
					isNotepadOpen = false;
					closeNotepadListener.onClose(notepadMinimized);
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void systemExplorerAreaClicked() {
		if(!isSystemExplorerOpen) {
			isSystemExplorerOpen = true;
			systemExplorerLoader = new FXMLLoader(getClass().getResource("SystemExplorer.fxml"));
			try {
				openSystemExplorerListener.onOpen(systemExplorerMinimized);
				systemExplorer = systemExplorerLoader.load();
				systemExplorerController = systemExplorerLoader.getController();
				systemExplorerController.addEditEventListener(this::openNotepadForEdit);
				systemExplorerController.addCloseSystemExplorerListener((image) -> {
					isSystemExplorerOpen = false;
					closeSystemExplorerListener.onClose(systemExplorerMinimized);
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void notepadMinizedPressed() {
		if(textWindowController.getIsWindowMinimized() == true) {
			textWindowController.setIsWindowMinimized(false);
			textWindowController.getStage().show();
		} else {
			textWindowController.minimizeButtonClicked();
		}
	}
	
	private void systemExplorerMinimizedPressed() {
		
		if(systemExplorerController.getIsWindowMinimized() == true) {
			systemExplorerController.setIsWindowMinimized(false);
			systemExplorerController.getStage().show();
		} else {
			systemExplorerController.minimizeButtonClicked();
		}
		
	}
	
	public void openNotepadForEdit(File fileToEdit) {
		if(!isNotepadOpen) {
			isNotepadOpen = true;
			notepadLoader = new FXMLLoader(getClass().getResource("TextWindow.fxml"));
			try {
				openNotepadListener.onOpen(notepadMinimized);
				notepad = notepadLoader.load();
				textWindowController = notepadLoader.getController();
				
				
				textWindowController.addCloseNotepadListener((image) -> {
					isNotepadOpen = false;
					closeNotepadListener.onClose(notepadMinimized);
				});
				
				Scanner reader = new Scanner(fileToEdit);
				StringBuilder text = new StringBuilder();
				
				textWindowController.setMetaPath(reader.nextLine());
				while(reader.hasNextLine()) {
					text.append(reader.nextLine());
				}
				reader.close();
				textWindowController.getTextArea().setText(text.toString());
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public boolean getIsNotepadOpen() {
		return isNotepadOpen;
	}
	
	public boolean getIsSystemExplorerOpen() {
		return isSystemExplorerOpen;
	}
	
	public TextWindowController getTextWindowController() {		
		return textWindowController;
	}
	
	public SystemExplorerController getSystemExplorerController() {		
		return systemExplorerController;
	}	
	
	public void addOpenNotepadListener(OpenNotepadEvent ob) {
		openNotepadListener = ob;
	}
	
	public void addOpenSystemExplorerListener(OpenSystemExplorerEvent ob) {
		openSystemExplorerListener = ob;
	}
	
	public void addCloseNotepadListener(CloseNotepadEvent ob) {
		closeNotepadListener = ob;
	}
	
	public void addCloseSystemExplorerListener(CloseSystemExplorerEvent ob) {
		closeSystemExplorerListener = ob;
	}
	
	public void addShutDownListener(ShutDownEvent ob) {
		shutdownListener = ob;
	}

	public void shutdownAreaClicked() {
		shutdownListener.onShutDown();
	}
}



