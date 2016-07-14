package software.committed.rejux;

@FunctionalInterface
public interface Subscriber<S> {

	void onStateChanged(S state);

}
