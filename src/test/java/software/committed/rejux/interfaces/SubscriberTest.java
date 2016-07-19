package software.committed.rejux.interfaces;

import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.Mockito;

public class SubscriberTest {

  @Test
  public void test() {
    Subscriber<String> spy = spy(new FakeSubscriber());


    String state = "test";
    spy.accept(state);

    Mockito.verify(spy).onStateChanged(state);
  }


  private static class FakeSubscriber implements Subscriber<String> {

    @Override
    public void onStateChanged(String state) {

    }
  }

}
