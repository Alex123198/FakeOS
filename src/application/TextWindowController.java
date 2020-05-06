package application;

import java.awt.MouseInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TextWindowController {
	@FXML private BorderPane root;
	@FXML private HBox titleBar;
	@FXML private TextArea textArea;
	
	private Stage stage;
	private boolean isMaximazed;
	private Screen screen;
	private Rectangle2D bounds;
	private double minimizedWidth;
	private double minimizedHeight;
	private double stageLocationX;
	private double stageLocationY;
	private double mousePosX;
	private double mousePosY;
	private boolean isWindowMinimized;
	private String metaPath;
	private CloseNotepadEvent listener;
	
	private FXMLLoader saveDialogLoader;
	private BorderPane saveDialog;
	private SaveDialogController saveDialogController;
	
	
	public TextWindowController() {
		isMaximazed = false;
		screen = Screen.getPrimary();
		bounds = screen.getBounds();
		minimizedWidth = 600;
		minimizedHeight = 400;
		
		stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		stageLocationX = bounds.getWidth() / 5; 
		stageLocationY = bounds.getHeight() / 5;
		stage.setX(stageLocationX);
		stage.setY(stageLocationY);
		isWindowMinimized = false;
		
		metaPath=null;
		
		SaveDialogController.addUpdatePathListener((path) -> {
			metaPath = path;
		});
	}
	
	public void initialize() {
		root.setPrefSize(minimizedWidth, minimizedHeight);
		Scene scene = new Scene(root);			
		
		makeWindowDraggable();
		stage.setScene(scene);
		stage.show();		
	}
	
	public void closeButtonClicked() {
		stage.close();
		listener.onClose(new ImageView());		
	}
	
	public void saveButtonClicked() {
		if (metaPath == null) {
			saveDialogLoader = new FXMLLoader(getClass().getResource("SaveDialog.fxml"));

			try {
				saveDialog = saveDialogLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			saveDialogController = (SaveDialogController) saveDialogLoader.getController();
			saveDialogController.saveString(textArea.getText());
		} else {
			File f = new File(metaPath);
			try {
				FileWriter writer = new FileWriter(f);
				writer.write(metaPath+"\n");
				writer.write(textArea.getText());
				writer.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}
	
	public void maximizeButtonClicked() {
		if(!isMaximazed) {
			stageLocationX = stage.getX();
			stageLocationY = stage.getY();
			stage.setWidth(bounds.getWidth());
			stage.setHeight(bounds.getHeight() - 25);
			stage.setX(0);
			stage.setY(0);
			isMaximazed = true;
			titleBar.setPrefWidth(bounds.getWidth() - 80);
		} else {
			stage.setWidth(minimizedWidth);
			stage.setHeight(minimizedHeight);
			stage.setX(stageLocationX);
			stage.setY(stageLocationY);
			isMaximazed = false;
			titleBar.setPrefWidth(bounds.getWidth() + 80);
		}
	}
	
	public void makeWindowDraggable() {
		
		root.setOnMousePressed(event -> {
			mousePosX = MouseInfo.getPointerInfo().getLocation().getX();
			mousePosY = MouseInfo.getPointerInfo().getLocation().getY();
		});
		
		root.setOnMouseDragged((event) -> {
			if(isMaximazed) {
				isMaximazed = false;
				stage.setWidth(minimizedWidth);
				stage.setHeight(minimizedHeight);
				stage.setX(MouseInfo.getPointerInfo().getLocation().getX() - stage.getWidth() / 2);
			}
			
			stage.setX(stage.getX() + (MouseInfo.getPointerInfo().getLocation().getX() - mousePosX));
		    mousePosX = MouseInfo.getPointerInfo().getLocation().getX();
		    
		    stage.setY(stage.getY() + (MouseInfo.getPointerInfo().getLocation().getY() - mousePosY));
		    mousePosY = MouseInfo.getPointerInfo().getLocation().getY();

		    stage.setOpacity(0.8f);
		});		

		root.setOnMouseReleased((event) -> {
			stage.setOpacity(1.0f);
		});
		
	}
	
	public TextArea getTextArea() {
		return textArea;
	}
	
	public void minimizeButtonClicked() {
		stage.hide();
		isWindowMinimized = true;
	}
	
	public boolean getIsWindowMinimized() {
		
		return isWindowMinimized;
	}
	
	public void setIsWindowMinimized(boolean value) {
		
		 isWindowMinimized = value;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void addCloseNotepadListener(CloseNotepadEvent ob) {
		listener = ob;
	}
	
	public void setMetaPath(String path) {
		metaPath = path;
	}
}












