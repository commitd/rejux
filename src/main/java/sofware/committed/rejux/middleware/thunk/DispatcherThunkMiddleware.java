package sofware.committed.rejux.middleware.thunk;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Middleware;

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
