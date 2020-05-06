package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface OpenSystemExplorerEvent {
	void onOpen(ImageView image);
}