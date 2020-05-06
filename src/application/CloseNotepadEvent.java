package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface CloseNotepadEvent {
	void onClose(ImageView image);
}
