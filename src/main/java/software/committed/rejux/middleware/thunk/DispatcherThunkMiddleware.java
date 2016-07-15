package software.committed.rejux.middleware.thunk;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;

public class DispatcherThunkMiddleware<G> implements Middleware<G> {

	@Override
	public void apply(Dispatcher first, G store, Action action, Dispatcher next) {
		if (action instanceof DispatcherThunkAction) {
			DispatcherThunkAction<G> thunk = (DispatcherThunkAction<G>) action;
			thunk.execute(first, store);
		} else {
			next.dispatch(action);
		}
	}

}