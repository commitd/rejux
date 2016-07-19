package software.committed.rejux.interfaces;

import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.Mockito;

public class DispatcherTest {

  @Test
  public void test() {
    ExplicitDispatcher spy = spy(new FakeDispatcher());

    Dispatcher dispatcher = action -> {
      // TODO Auto-generated method stub

    };
    Object action = new Object();
    spy.accept(dispatcher, action);

    Mockito.verify(spy).dispatch(dispatcher, action);
  }


  private static class FakeDispatcher implements ExplicitDispatcher {
    @Override
    public void dispatch(Dispatcher dispatcher, Object action) {
      // Do nothing

    }
  }

}
