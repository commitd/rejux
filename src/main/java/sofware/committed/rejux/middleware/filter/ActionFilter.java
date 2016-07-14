package sofware.committed.rejux.middleware.filter;

import sofware.committed.rejux.Action;

@FunctionalInterface
public interface ActionFilter {
	boolean accepts(Action action);
}
