package application;

import java.awt.MouseInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SaveDialogController {
	@FXML private BorderPane root;
	@FXML private AnchorPane anchorPane;
	@FXML private Button saveButton;
	@FXML private Label path;
	@FXML private TextField filename;
	
	private TreeView<File> treeView;
	
	private Stage stage;
	private Screen screen;
	private Rectangle2D bounds;
	private double minimizedWidth;
	private double minimizedHeight;
	private double stageLocationX;
	private double stageLocationY;
	private double mousePosX;
	private double mousePosY;
	private String textSaved;
	
	private ImageView saveButtonImage;
	
	private static CloseSaveDialogEvent closeSaveDialogListener;
	private static OpenSaveDialogEvent openSaveDialogListener;
	private static UpdatePathEvent updatePathListener;
	
	static {
		closeSaveDialogListener = null;
	}
	
	public SaveDialogController() {
		
		try {
			saveButtonImage = new ImageView(new Image(new FileInputStream("Resources/save_button.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		openSaveDialogListener.onOpen(saveButtonImage);
		
		screen = Screen.getPrimary();
		bounds = screen.getBounds();
		minimizedWidth = 600;
		minimizedHeight = 400;
		
		stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stageLocationX = bounds.getWidth() / 5; 
		stageLocationY = bounds.getHeight() / 5;
		stage.setX(stageLocationX);
		stage.setY(stageLocationY);
		
		treeView = new TreeView<File>(new SimpleFileTreeItem(new File("home/")));
		treeView.setPrefSize(minimizedWidth, minimizedHeight - 140);
		
		//event handler for tree view item clicked
		EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
			handleMouseClicked(event);
		};

		treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 
	}
	
	private void handleMouseClicked(MouseEvent event) {
	    Node node = event.getPickResult().getIntersectedNode();
	    
	    // Accept clicks only on node cells, and not on empty spaces of the TreeView
	    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
	    	try {
	    		File file = new File(((TreeItem)(treeView.getSelectionModel().getSelectedItem())).getValue().toString());
	    		
	    		if(file.isDirectory()) {
		        	path.setText(file.toString());
		        	if(filename.getText().length() > 0) {
		        		saveButton.setDisable(false);
		        	}
		        }
	    	} catch(NullPointerException e) {
	    		//This exception happens sometimes but it doesn't influence in a negative way the flow of the program
	    	}
	    }
	}
	
	public void filenameChanged() {
		if(filename.getText().length() > 0 && path.getText().length() > 0) {
			saveButton.setDisable(false);
		} else {
			saveButton.setDisable(true);
		}
	}
	
	public void saveButtonClicked() {
		
		File pathVar = new File(path.getText());
		File filenameVar = new File(pathVar + "/" + filename.getText());
		
		try {
			if(!pathVar.canWrite()) {
				AlertBoxController.setMessage("Can't write to that location. Please choose another one.");
				FXMLLoader.load(getClass().getResource("AlertBox.fxml"));
			} else if(filenameVar.exists()) {
				AlertBoxController.setMessage("File already exists. Please choose another name.");
				FXMLLoader.load(getClass().getResource("AlertBox.fxml"));
			} else {
				FileWriter out = new FileWriter(filenameVar);
				
				out.write(filenameVar + "\n");
				out.write(textSaved);
				out.close();
				updatePathListener.onUpdate(filenameVar+"");
				closeSaveDialogListener.onClose(saveButtonImage);
				stage.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initialize() {
		root.setPrefSize(minimizedWidth, minimizedHeight);
		saveButton.setDisable(true);

		Scene scene = new Scene(root);			
		
		makeWindowDraggable();
		anchorPane.setPrefSize(minimizedWidth, minimizedHeight);
		anchorPane.getChildren().add(treeView);
		treeView.setLayoutY(110);
		
		stage.setScene(scene);
		stage.show();		
	}
	
	public void closeButtonClicked() {
		closeSaveDialogListener.onClose(saveButtonImage);
		stage.close();		
	}
	
	public void makeWindowDraggable() {
		
		root.setOnMousePressed(event -> {
			mousePosX = MouseInfo.getPointerInfo().getLocation().getX();
			mousePosY = MouseInfo.getPointerInfo().getLocation().getY();
		});
		
		root.setOnMouseDragged((event) -> {
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
	
	public Stage getStage() {
		return stage;
	}
	
	public static void addOpenSaveDialogListener(OpenSaveDialogEvent ob) {
		openSaveDialogListener = ob;
	}
	
	public static void addCloseSaveDialogListener(CloseSaveDialogEvent ob) {
		closeSaveDialogListener = ob;
	}
	
	public static void addUpdatePathListener(UpdatePathEvent ob) {
		updatePathListener = ob;
	}
	
	void saveString(String textArea) {
		textSaved=textArea;
	}
	
	
}












