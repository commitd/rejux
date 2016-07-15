package software.committed.rejux;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import software.committed.rejux.Rejux;
import software.committed.rejux.impl.SimpleStore;
import software.committed.rejux.impl.SuperStore;

public class RejuxTest {

	@Test
	public void createSuperstore() {
		SuperStore<Object> superstore = Rejux.createSuperStore(new Object());
		assertThat(superstore).isNotNull();
	}

	@Test
	public void createStore() {
		SimpleStore<Object> superstore = Rejux.createStore(new Object(), (s, a) -> s);
		assertThat(superstore).isNotNull();
	}
}
