package application;
import java.io.File;

@FunctionalInterface
public interface EditEvent {
	void onEdit(File f);
}
