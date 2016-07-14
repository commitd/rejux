package sofware.committed.rejux;

public interface StateHolder<S> {

	S getState();

	Subscription subscribe(Subscriber<S> subscriber);
}
