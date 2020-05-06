package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface OpenNotepadEvent {
	void onOpen(ImageView image);
}
