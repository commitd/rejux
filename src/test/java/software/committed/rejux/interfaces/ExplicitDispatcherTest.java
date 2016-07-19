package software.committed.rejux.interfaces;

import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.Mockito;

public class ExplicitDispatcherTest {

  @Test
  public void test() {
    Dispatcher spy = spy(new FakeDispatcher());

    Object action = new Object();
    spy.accept(action);

    Mockito.verify(spy).dispatch(action);
  }


  private static class FakeDispatcher implements Dispatcher {
    @Override
    public void dispatch(Object action) {
      // Do nothing

    }
  }

}
