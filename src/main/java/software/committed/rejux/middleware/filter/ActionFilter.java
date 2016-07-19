package software.committed.rejux.middleware.filter;

@FunctionalInterface
public interface ActionFilter {
  boolean accepts(Object action);
}
