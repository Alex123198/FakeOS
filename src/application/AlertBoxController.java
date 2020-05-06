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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertBoxController {
	@FXML private VBox root;
	
	private Label label;
	private Button button;
		
	private static ImageView alertBoxMinimized;
	private static String message;
	private static AlertEvent closeAlertListener;
	private static AlertEvent openAlertListener;
	
	private double stageLocationX;
	private double stageLocationY;
	private double mousePosX;
	private double mousePosY;
	private Screen screen;
	private Rectangle2D bounds;
	
	private Stage stage;	
	
	static {
		try {
			alertBoxMinimized = new ImageView(new Image(new FileInputStream("Resources/alert.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public AlertBoxController() {
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
		
		label = new Label(message);
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setTextFill(Color.web("#ffcb0a"));
		
		button = new Button("OK");			
		button.setOnAction(e -> {
			closeAlertListener.onAlert(alertBoxMinimized);
			stage.close();
		});
		
		openAlertListener.onAlert(alertBoxMinimized);
	}
	
	public void initialize() {
		root.setPadding(new Insets(15));
		root.getChildren().addAll(label, button);
		root.setAlignment(Pos.CENTER);
		
		makeWindowDraggable();
		
		stage.setScene(new Scene(root));
		stage.showAndWait();
	}
	
	public void onButtonClick() {
		closeAlertListener.onAlert(alertBoxMinimized);
		stage.close();
	}
	
	public static void addOpenAlertListener(AlertEvent ob) {
		openAlertListener = ob;
	}
	
	public static void addCloseAlertListener(AlertEvent ob) {
		closeAlertListener = ob;
	}
	
	public static void setMessage(String message) {
		AlertBoxController.message = message;
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
}
