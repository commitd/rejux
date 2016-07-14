package sofware.committed.rejux.middleware.thunk;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.StatefulMiddleware;

public class StatefulThunkMiddleware<S> implements StatefulMiddleware<S> {

	@Override
	public void apply(Dispatcher first, S state, Action action, Dispatcher next) {
		if (action instanceof StatefulThunkAction) {
			StatefulThunkAction<S> thunk = (StatefulThunkAction<S>) action;
			thunk.execute(first, state);
		} else {
			next.dispatch(action);
		}
	}

}
