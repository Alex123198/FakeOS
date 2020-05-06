package application;

@FunctionalInterface
public interface UpdatePathEvent {
	void onUpdate(String path);
}
