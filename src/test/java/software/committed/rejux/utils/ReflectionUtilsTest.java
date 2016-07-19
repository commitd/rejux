package software.committed.rejux.utils;

import java.lang.reflect.Method;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ReflectionUtilsTest {

  @Test
  public void testFindParameterlessMethodsWithReturnType() {
    final Exemplar e = new Exemplar();
    final List<Method> list =
        ReflectionUtils.findParameterlessMethodsWithReturnType(e, String.class);

    Assertions.assertThat(list).extracting("name").containsExactlyInAnyOrder("validButNullStore",
        "getString", "getAnotherString", "toString");
  }

  @Test
  public void testGetAllReturnedOfType() {
    final Exemplar e = new Exemplar();
    final List<String> list = ReflectionUtils.getAllReturnedOfType(e, String.class);

    Assertions.assertThat(list).containsExactlyInAnyOrder("first", "another", "toString");
  }

  @Test
  public void testFindMethods() {
    final Exemplar e = new Exemplar();
    final List<Method> list = ReflectionUtils.findMethods(e, String.class, Integer.class);

    Assertions.assertThat(list).hasSize(1).extracting("name").containsExactly("notAStoreAsParam");
  }

  @Test
  public void testCallWithDefault() throws NoSuchMethodException, SecurityException {

    final Exemplar e = new Exemplar();
    final Method m = e.getClass().getMethod("hello", String.class);
    final String ok = ReflectionUtils.callWithDefaultReturn(e, String.class, "fail", m, "world");

    Assertions.assertThat(ok).isEqualTo("hello world");

    final String err = ReflectionUtils.callWithDefaultReturn(e, String.class, "fail", m);
    Assertions.assertThat(err).isEqualTo("fail");
  }

  public class Exemplar {

    public void notAtStore() {

    }

    public String notAStoreAsParam(final Integer doh) {
      return "fail";
    }

    public String validButNullStore() {
      return null;
    }

    public String getString() {
      return "first";
    }

    public String getAnotherString() {
      return "another";
    }

    public String hello(final String value) {
      return "hello " + value;
    }

    @Override
    public String toString() {
      return "toString";
    }

  }
}
