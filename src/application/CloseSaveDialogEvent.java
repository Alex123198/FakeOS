package application;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface CloseSaveDialogEvent {
	void onClose(ImageView image);
}
