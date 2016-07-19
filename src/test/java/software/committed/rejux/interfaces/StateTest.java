package software.committed.rejux.interfaces;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StateTest {

  @Test
  public void testStateEqualsGet() {
    TestState s = new TestState();

    assertThat(s.get()).isEqualTo(s.state());
  }

  private static class TestState implements State<String> {

    @Override
    public Class<String> getType() {
      return String.class;
    }

    @Override
    public String state() {
      return "hello";
    }

  }
}
