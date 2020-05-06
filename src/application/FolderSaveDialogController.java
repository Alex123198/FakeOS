package application;

import java.awt.MouseInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FolderSaveDialogController {
	@FXML private AnchorPane root;
	@FXML private TextField folderNameTextField;
	@FXML private Button okButton;
	
	private static ImageView saveFolderMinimized;
	
	private static boolean systemExplorerIsWaiting;
	
	private static AlertEvent closeAlertListener;
	private static AlertEvent openAlertListener;
	private static FolderNameTransmissionEvent folderNameTransmissionListener;
	
	private double stageLocationX;
	private double stageLocationY;
	private double mousePosX;
	private double mousePosY;
	private Screen screen;
	private Rectangle2D bounds;
	
	private Stage stage;	
	
	static {
		try {
			saveFolderMinimized = new ImageView(new Image(new FileInputStream("Resources/folder_save_button.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public FolderSaveDialogController() {
		screen = Screen.getPrimary();
		bounds = screen.getBounds();
		
		stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.setWidth(300);
		stage.setHeight(150);
		stageLocationX = bounds.getWidth() / 2 - stage.getWidth() / 2; 
		stageLocationY = bounds.getHeight() / 2 - stage.getHeight() / 2;
		stage.setX(stageLocationX);
		stage.setY(stageLocationY);
		stage.setResizable(false);
		
		FolderSaveDialogController.systemExplorerIsWaiting = true;
		
		openAlertListener.onAlert(saveFolderMinimized);
	}
	
	public void initialize() {
		makeWindowDraggable();
		okButton.setDisable(true);
		
		stage.setScene(new Scene(root));
		stage.showAndWait();
	}
	
	public void onTextFieldChange() {
		if(folderNameTextField.getText().length() == 0) {
			okButton.setDisable(true);
		} else {
			okButton.setDisable(false);
		}
	}
	
	public void onCancelButtonClicked() {
		closeAlertListener.onAlert(saveFolderMinimized);
		FolderSaveDialogController.systemExplorerIsWaiting = false;
		stage.close();
	}
	
	public void onOkButtonClicked() {
		closeAlertListener.onAlert(saveFolderMinimized);
		folderNameTransmissionListener.onTransmission(folderNameTextField.getText());
		FolderSaveDialogController.systemExplorerIsWaiting = false;
		stage.close();
	}
	
	public static void addOpenFolderSaveDialogListener(AlertEvent ob) {
		openAlertListener = ob;
	}
	
	public static void addCloseFolderSaveDialogListener(AlertEvent ob) {
		closeAlertListener = ob;
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
	
	public static void addFolderNameTransmissionListener(FolderNameTransmissionEvent ob) {
		folderNameTransmissionListener = ob;
	}
	
	public static boolean getSystemExplorerIsWaiting() {
		return systemExplorerIsWaiting;
	}
}
