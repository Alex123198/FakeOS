package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface CloseSystemExplorerEvent {
	void onClose(ImageView image);
}
