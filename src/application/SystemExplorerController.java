package application;

import java.awt.MouseInfo;
import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SystemExplorerController {
	@FXML private BorderPane root;
	@FXML private AnchorPane anchorPane;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button newFolderButton;
	@FXML private HBox titleBar;
	
	private TreeView<File> treeView;
	
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
	
	private String folderName;
	
	private CloseSystemExplorerEvent closeSystemExplorerListener;
	private EditEvent editEventListener;
	
	public SystemExplorerController() {
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
		
		folderName = null;

		treeView = new TreeView<File>(new SimpleFileTreeItem(new File("home/")));
		treeView.setPrefSize(minimizedWidth, minimizedHeight - 30);
		treeView.setOnMouseClicked((MouseEvent e) -> {
			TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
			File file=null;
			
			if(item != null) {
				file = item.getValue();
				newFolderButton.setDisable(false);
			} else {
				newFolderButton.setDisable(true);
			}
			
			
			if(file != null && file.isFile()) {
				editButton.setDisable(false);
				if(e.getClickCount() > 1) {
					editButtonClicked();
				}
			}else {
				editButton.setDisable(true);
			}
		});
		
		FolderSaveDialogController.addFolderNameTransmissionListener((folder_Name) -> {
			folderName = folder_Name;
		});
	}
	
	public void initialize() {
		root.setPrefSize(minimizedWidth, minimizedHeight);
		Scene scene = new Scene(root);			
		
		makeWindowDraggable();
		anchorPane.setPrefSize(minimizedWidth, minimizedHeight);
		anchorPane.getChildren().add(treeView);
		
		editButton.setDisable(true);
		newFolderButton.setDisable(true);
		
		stage.setScene(scene);
		stage.show();		
	}
	
	public void editButtonClicked() {
		TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
		File file = item.getValue();
		
		editEventListener.onEdit(file);
		stage.close();
		closeSystemExplorerListener.onClose(new ImageView());
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	public void deleteButtonClicked() {
		TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
		File file = null;
		
		if(item != null) {
			file = item.getValue();
			if(!file.toString().equals("home")) {
				deleteDirectory(file);
				item.getParent().getChildren().remove(item);
				treeView.refresh();
			} else {
				AlertBoxController.setMessage("Can't delete the root of the file system!");
				try {
					FXMLLoader.load(getClass().getResource("AlertBox.fxml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void newFolderButtonClicked() {
		folderName = null;
		try {
			FXMLLoader.load(getClass().getResource("FolderSaveDialog.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(FolderSaveDialogController.getSystemExplorerIsWaiting() == true) {}
		
		if(folderName != null) {
			TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
			File file = item.getValue();
			// TODO optimize
			if(file.isDirectory()) {
				File dir=new File(file.toString() + "/" + folderName);
				if(dir.exists()) {
					AlertBoxController.setMessage("Folder already exists!");
					try {
						FXMLLoader.load(getClass().getResource("AlertBox.fxml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					if(!item.isExpanded()) {
						item.expandedProperty().set(true);
					}
					dir.mkdir();
					SimpleFileTreeItem itm = new SimpleFileTreeItem(dir);
					item.getChildren().add(itm);
				}
			} else {
				File dir = new File(file.getParentFile().toString() + "/" + folderName);
				if(dir.exists()) {
					AlertBoxController.setMessage("Folder already exists!");
					try {
						FXMLLoader.load(getClass().getResource("AlertBox.fxml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					dir.mkdir();
					SimpleFileTreeItem itm = new SimpleFileTreeItem(dir);
					item.getParent().getChildren().add(itm);
				}
			}
			
			treeView.refresh();
		}
		
	}
	
	public void closeButtonClicked() {
		stage.close();
		closeSystemExplorerListener.onClose(new ImageView());		
	}
	
	public void maximizeButtonClicked() {
		if(!isMaximazed) {
			stageLocationX = stage.getX();
			stageLocationY = stage.getY();
			stage.setWidth(bounds.getWidth());
			stage.setHeight(bounds.getHeight() - 25);
			stage.setX(0);
			stage.setY(0);
			treeView.setPrefSize(bounds.getWidth(), bounds.getHeight() - 55);
			isMaximazed = true;
			titleBar.setPrefWidth(bounds.getWidth() - 80);
		} else {
			stage.setWidth(minimizedWidth);
			stage.setHeight(minimizedHeight);
			stage.setX(stageLocationX);
			stage.setY(stageLocationY);
			treeView.setPrefSize(minimizedWidth, minimizedHeight - 30);
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
				treeView.setPrefSize(minimizedWidth, minimizedHeight - 30);
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
	
	public void addCloseSystemExplorerListener(CloseSystemExplorerEvent ob) {
		closeSystemExplorerListener = ob;
	}
	
	public void addEditEventListener(EditEvent e) {
		editEventListener = e;
	}
	
}












