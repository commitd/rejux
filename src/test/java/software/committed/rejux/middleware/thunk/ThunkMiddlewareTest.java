package software.committed.rejux.middleware.thunk;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import software.committed.rejux.interfaces.Dispatcher;

public class ThunkMiddlewareTest {

  @Test
  public void testNonThunk() {
    final ThunkMiddleware<Object> m = new ThunkMiddleware<>();

    final Dispatcher first = Mockito.mock(Dispatcher.class);
    final Dispatcher next = Mockito.mock(Dispatcher.class);
    final Object action = new Object();
    final Object state = new Object();

    m.apply(first, state, action, next);

    Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
    Mockito.verify(next).dispatch(Matchers.any());
  }

  @Test
  public void testThunk() {
    final ThunkMiddleware<Object> m = new ThunkMiddleware<>();

    final Dispatcher first = Mockito.mock(Dispatcher.class);
    final Dispatcher next = Mockito.mock(Dispatcher.class);
    final ThunkAction<Object> action = Mockito.mock(ThunkAction.class);
    final Object state = new Object();

    m.apply(first, state, action, next);

    Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
    Mockito.verify(next, Mockito.never()).dispatch(Matchers.any());

    Mockito.verify(action).execute(first, state);
  }

}
