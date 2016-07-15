package software.committed.rejux.interfaces;

public interface State<S> {

	S getState();

	Subscription subscribe(Subscriber<S> subscriber);
}
