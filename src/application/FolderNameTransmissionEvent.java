package application;

@FunctionalInterface
public interface FolderNameTransmissionEvent {
	void onTransmission(String folderName);
}
