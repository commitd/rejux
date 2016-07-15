package software.committed.rejux.interfaces;

@FunctionalInterface
public interface Subscriber<S> {

	void onStateChanged(S state);

}
