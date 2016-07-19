package software.committed.rejux;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import software.committed.rejux.interfaces.State;
import software.committed.rejux.interfaces.Store;

public class RejuxTest {

  public interface FakeState {

    String test();
  }

  @Test
  public void createStore() {
    final Store<FakeState> store = Rejux.createStore(FakeState.class, () -> "test");
    Assertions.assertThat(store).isNotNull();
  }

  @Test
  public void createState() {
    final State<Object> state = Rejux.createState(Object.class, new Object());
    Assertions.assertThat(state).isNotNull();
  }
}
