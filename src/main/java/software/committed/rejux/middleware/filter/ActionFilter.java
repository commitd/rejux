package software.committed.rejux.middleware.filter;

import software.committed.rejux.interfaces.Action;

@FunctionalInterface
public interface ActionFilter {
	boolean accepts(Action action);
}
