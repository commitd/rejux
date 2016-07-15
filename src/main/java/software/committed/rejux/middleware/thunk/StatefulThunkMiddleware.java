package software.committed.rejux.middleware.thunk;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.StatefulMiddleware;

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
