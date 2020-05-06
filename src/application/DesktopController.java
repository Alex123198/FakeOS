package application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import com.jfoenix.controls.JFXDrawer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DesktopController {
	@FXML private AnchorPane root;
	@FXML private Shape startButton;
	@FXML private Line taskBarLine;
	@FXML private Label timeLabel;
	@FXML private JFXDrawer mainMenuDrawer;
	@FXML private GridPane taskBarGrid;
	
	private int numOfAppsInTaskBar;
	
	private Date date;
	private Screen screen;
	private Rectangle2D bounds;
	
	private FXMLLoader mainMenuContentLoader;
	private VBox mainMenuContent;
	private MainMenuContentController mainMenuContentController;	
	
	
	public DesktopController() {
		numOfAppsInTaskBar = 0;
		screen = Screen.getPrimary();
		bounds = screen.getBounds();
		date = new Date();
		try {
			mainMenuContentLoader = new FXMLLoader(getClass().getResource("MainMenuContent.fxml")); 
			mainMenuContent = mainMenuContentLoader.load();
			mainMenuContentController = mainMenuContentLoader.getController();
			
			mainMenuContentController.addShutDownListener(() -> {
				closeOS();
			});
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void initialize() {
		root.setPrefSize(bounds.getWidth(), bounds.getHeight());
		
		startButton.setLayoutX(0);
		startButton.setLayoutY(bounds.getHeight() - 30);
		
		taskBarGrid.setLayoutX(40);
		taskBarGrid.setLayoutY(bounds.getHeight() - 25);		
		
		taskBarLine.setLayoutY((int)startButton.getLayoutY() + 3);		
		taskBarLine.setEndX((int)bounds.getWidth() - 130);

		// TODO make date updatable
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String[] dateTime = formatter.format(date).split(" ");

		timeLabel.setLayoutX(bounds.getWidth() - 70);
		
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        LocalTime currentTime = LocalTime.now();
	        String seconds = currentTime.getSecond() + "";
	        String minutes = currentTime.getMinute() + "";
	        String hours = currentTime.getHour() + "";
	        
	        if(seconds.length() == 1) {
	        	seconds = "0" + seconds;
	        }
	        
	        if(minutes.length() == 1) {
	        	minutes = "0" + minutes;
	        }
	        
	        if(hours.length() == 1) {
	        	hours = "0" + hours;
	        }
	        
	        if(seconds.equals("00") && minutes.equals("00") && hours.equals("00")) {
	        	dateTime[0] = formatter.format(new Date()).split(" ")[0];
	        }
	        
	        timeLabel.setText(dateTime[0] + "\n" + hours + ":" + minutes + ":" + seconds);
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();

	    mainMenuContent.setPrefWidth(mainMenuDrawer.getPrefWidth());
		mainMenuContent.setPrefHeight(mainMenuDrawer.getPrefHeight());		
		mainMenuDrawer.setLayoutX(0);		
		mainMenuDrawer.setLayoutY(bounds.getHeight() - mainMenuDrawer.getPrefHeight() - 30);
		mainMenuDrawer.setSidePane(mainMenuContent);
		
		mainMenuContentController.addOpenNotepadListener((image) -> {
			mainMenuDrawer.close();
			++numOfAppsInTaskBar;
			taskBarGrid.add(image, numOfAppsInTaskBar - 1, 0);
		});
		
		mainMenuContentController.addCloseNotepadListener((image) -> {
			ArrayList<Node> arr=new ArrayList<Node>();
			int index=taskBarGrid.getColumnIndex(image);
			
			for(int i = 0; i < taskBarGrid.getChildren().size(); ++i) {
				arr.add(taskBarGrid.getChildren().get(i));
				taskBarGrid.getChildren().remove(i);
				i--;
			}
			arr.remove(index);
			for(int i = 0; i < arr.size(); ++i) {
				taskBarGrid.add(arr.get(i), i, 0);
			}
			--numOfAppsInTaskBar;
		});
		
		SaveDialogController.addOpenSaveDialogListener((image) -> {
			++numOfAppsInTaskBar;
			taskBarGrid.add(image, numOfAppsInTaskBar - 1, 0);
		});
		
		SaveDialogController.addCloseSaveDialogListener((image) -> {
			ArrayList<Node> arr=new ArrayList<Node>();
			int index=taskBarGrid.getColumnIndex(image);
			
			for(int i = 0; i < taskBarGrid.getChildren().size(); ++i) {
				arr.add(taskBarGrid.getChildren().get(i));
				taskBarGrid.getChildren().remove(i);
				i--;
			}
			arr.remove(index);
			for(int i = 0; i < arr.size(); ++i) {
				taskBarGrid.add(arr.get(i), i, 0);
			}
			--numOfAppsInTaskBar;
		});
		
		FolderSaveDialogController.addOpenFolderSaveDialogListener((image) -> {
			++numOfAppsInTaskBar;
			taskBarGrid.add(image, numOfAppsInTaskBar - 1, 0);
		});
		
		FolderSaveDialogController.addCloseFolderSaveDialogListener((image) -> {
			ArrayList<Node> arr=new ArrayList<Node>();
			int index=taskBarGrid.getColumnIndex(image);
			
			for(int i = 0; i < taskBarGrid.getChildren().size(); ++i) {
				arr.add(taskBarGrid.getChildren().get(i));
				taskBarGrid.getChildren().remove(i);
				i--;
			}
			arr.remove(index);
			for(int i = 0; i < arr.size(); ++i) {
				taskBarGrid.add(arr.get(i), i, 0);
			}
			--numOfAppsInTaskBar;
		});
		
		mainMenuContentController.addOpenSystemExplorerListener((image) -> {
			mainMenuDrawer.close();
			++numOfAppsInTaskBar;
			taskBarGrid.add(image, numOfAppsInTaskBar - 1, 0);
		});
		
		mainMenuContentController.addCloseSystemExplorerListener((image) -> {
			ArrayList<Node> arr=new ArrayList<Node>();
			int index=taskBarGrid.getColumnIndex(image);
			
			for(int i = 0; i < taskBarGrid.getChildren().size(); ++i) {
				arr.add(taskBarGrid.getChildren().get(i));
				taskBarGrid.getChildren().remove(i);
				i--;
			}
			arr.remove(index);
			for(int i = 0; i < arr.size(); ++i) {
				taskBarGrid.add(arr.get(i), i, 0);
			}
			--numOfAppsInTaskBar;
		});
		
		AlertBoxController.addOpenAlertListener((image) -> {
			++numOfAppsInTaskBar;
			taskBarGrid.add(image, numOfAppsInTaskBar - 1, 0);
		});
		
		AlertBoxController.addCloseAlertListener((image) -> {
			ArrayList<Node> arr=new ArrayList<Node>();
			int index=taskBarGrid.getColumnIndex(image);
			
			for(int i = 0; i < taskBarGrid.getChildren().size(); ++i) {
				arr.add(taskBarGrid.getChildren().get(i));
				taskBarGrid.getChildren().remove(i);
				i--;
			}
			arr.remove(index);
			for(int i = 0; i < arr.size(); ++i) {
				taskBarGrid.add(arr.get(i), i, 0);
			}
			--numOfAppsInTaskBar;
		});
	}
	
	public void closeOS() {		
		if(mainMenuContentController.getIsNotepadOpen() == true) {
			mainMenuContentController.getTextWindowController().closeButtonClicked();
		}
		
		if(mainMenuContentController.getIsSystemExplorerOpen() == true) {
			mainMenuContentController.getSystemExplorerController().closeButtonClicked();
		}
		
		((Stage) root.getScene().getWindow()).close();
	}
	
	public void startButtonClicked() {
		if(mainMenuDrawer.isOpened()) {
			mainMenuDrawer.close();
		} else {
			mainMenuDrawer.open();
		}
	}
}
