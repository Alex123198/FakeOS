package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface OpenSaveDialogEvent {
	void onOpen(ImageView image);
}
