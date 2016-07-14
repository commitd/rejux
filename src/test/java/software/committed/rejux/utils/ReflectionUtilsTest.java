package software.committed.rejux.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import software.committed.rejux.Rejux;
import software.committed.rejux.impl.Store;
import software.committed.rejux.utils.ReflectionUtils;

public class ReflectionUtilsTest {

	@Test
	public void testFindParameterlessMethodsWithReturnType() {
		Exemplar e = new Exemplar();
		List<Method> list = ReflectionUtils.findParameterlessMethodsWithReturnType(e, Store.class);

		assertThat(list)
				.extracting("name")
				.containsExactlyInAnyOrder("validButNullStore", "getString", "getAnotherString");
	}

	@Test
	public void testGetAllReturnedOfType() {
		Exemplar e = new Exemplar();
		List<Store> list = ReflectionUtils.getAllReturnedOfType(e, Store.class);

		assertThat(list).hasSize(2);
	}

	@Test
	public void testFindMethods() {
		Exemplar e = new Exemplar();
		List<Method> list = ReflectionUtils.findMethods(e, Store.class, Integer.class);

		assertThat(list).hasSize(1).extracting("name").containsExactly("notAStoreAsParam");
	}

	@Test
	public void testCallWithDefault() throws NoSuchMethodException, SecurityException {

		Exemplar e = new Exemplar();
		Method m = e.getClass().getMethod("hello", String.class);
		String ok = ReflectionUtils.callWithDefaultReturn(e, String.class, "fail", m, "world");

		assertThat(ok).isEqualTo("hello world");

		String err = ReflectionUtils.callWithDefaultReturn(e, String.class, "fail", m);
		assertThat(err).isEqualTo("fail");
	}

	public class Exemplar {

		public void notAtStore() {

		}

		public Store<String> notAStoreAsParam(Integer doh) {
			return Rejux.createStore("fail", (s, a) -> s);
		}

		public Store<String> validButNullStore() {
			return null;
		}

		public Store<String> getString() {
			return Rejux.createStore("first", (s, a) -> s);
		}

		public Store<String> getAnotherString() {
			return Rejux.createStore("another", (s, a) -> s);
		}

		public String hello(String value) {
			return "hello " + value;
		}
	}
}
