package software.committed.rejux.interfaces;

public interface StateHolder<S> {

	S getState();

	Subscription subscribe(Subscriber<S> subscriber);
}
