package application;

@FunctionalInterface
public interface ShutDownEvent {
	void onShutDown();
}
