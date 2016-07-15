package software.committed.rejux;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import software.committed.rejux.interfaces.State;
import software.committed.rejux.interfaces.Store;

public class RejuxTest {

	public interface FakeState {

		String test();
	}

	@Test
	public void createStore() {
		Store<FakeState> store = Rejux.createStore(FakeState.class, () -> "test");
		assertThat(store).isNotNull();
	}

	@Test
	public void createState() {
		State<Object> state = Rejux.createState(Object.class, new Object());
		assertThat(state).isNotNull();
	}
}
