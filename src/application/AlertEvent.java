package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface AlertEvent {
	void onAlert(ImageView image);
}
