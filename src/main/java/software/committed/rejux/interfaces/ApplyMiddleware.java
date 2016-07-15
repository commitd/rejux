package software.committed.rejux.interfaces;

public interface ApplyMiddleware<S> {

	Dispatcher applyMiddleware(Dispatcher first, Dispatcher next);

}
