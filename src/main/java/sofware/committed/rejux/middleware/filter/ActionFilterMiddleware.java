package sofware.committed.rejux.middleware.filter;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.StatefulMiddleware;

public class ActionFilterMiddleware<S> implements StatefulMiddleware<S> {

	private final ActionFilter filter;

	public ActionFilterMiddleware(ActionFilter filter) {
		this.filter = filter;
	}

	@Override
	public void apply(Dispatcher first, S state, Action action, Dispatcher next) {
		if (filter.accepts(action)) {
			next.dispatch(action);
		}
	}

}
