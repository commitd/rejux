package software.committed.rejux.middleware.filter;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.StatefulMiddleware;

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
