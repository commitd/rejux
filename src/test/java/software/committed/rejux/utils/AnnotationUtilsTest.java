package software.committed.rejux.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.impl.AbstractReducer;

public class AnnotationUtilsTest {


  @Test
  public void testIsAnnotationPresentInHierarchyForOrphan()
      throws NoSuchMethodException, SecurityException {
    boolean has = AnnotationUtils
        .isAnnotationPresentInHierarchy(Orphan.class.getMethod("has", String.class), Reduce.class);
    assertTrue(has);

    boolean not = AnnotationUtils
        .isAnnotationPresentInHierarchy(Orphan.class.getMethod("not", String.class), Reduce.class);
    assertFalse(not);
  }

  @Test
  public void testIsAnnotationPresentInHierarchyForParentInterface()
      throws NoSuchMethodException, SecurityException {

    boolean has = AnnotationUtils.isAnnotationPresentInHierarchy(
        ChildOfInterface.class.getMethod("has", String.class), Reduce.class);
    assertTrue(has);

    boolean not = AnnotationUtils.isAnnotationPresentInHierarchy(
        ChildOfInterface.class.getMethod("not", String.class), Reduce.class);
    assertFalse(not);
  }

  @Test
  public void testIsAnnotationPresentInHierarchyForForParentClass()
      throws NoSuchMethodException, SecurityException {
    boolean has = AnnotationUtils.isAnnotationPresentInHierarchy(
        ChildOfClass.class.getMethod("has", String.class), Reduce.class);
    assertTrue(has);

    boolean not = AnnotationUtils.isAnnotationPresentInHierarchy(
        ChildOfClass.class.getMethod("not", String.class), Reduce.class);
    assertFalse(not);
  }


  @Test
  public void testGetAnnotationForOrphan() {
    Reduce has = AnnotationUtils.getAnnotation(Orphan.class, "has", new Class[] {String.class},
        Reduce.class);
    assertNotNull(has);

    Reduce not = AnnotationUtils.getAnnotation(Orphan.class, "not", new Class[] {String.class},
        Reduce.class);
    assertNull(not);
  }

  @Test
  public void testGetAnnotationForParentInterface() {
    Reduce has = AnnotationUtils.getAnnotation(ChildOfInterface.class, "has",
        new Class[] {String.class}, Reduce.class);
    assertNotNull(has);

    Reduce not = AnnotationUtils.getAnnotation(ChildOfInterface.class, "not",
        new Class[] {String.class}, Reduce.class);
    assertNull(not);

  }

  @Test
  public void testGetAnnotationForParentClass() {
    Reduce has = AnnotationUtils.getAnnotation(ChildOfClass.class, "has",
        new Class[] {String.class}, Reduce.class);
    assertNotNull(has);

    Reduce not = AnnotationUtils.getAnnotation(ChildOfClass.class, "not",
        new Class[] {String.class}, Reduce.class);
    assertNull(not);

  }

  public interface ParentInterface {
    @Reduce(value = FakeReducer.class)
    void has(String param);

    void not(String param);
  }

  public class ChildOfInterface implements ParentInterface {
    @Override
    public void has(String param) {

    }


    @Override
    public void not(String param) {

    }
  }

  public class ParentClass {
    @Reduce(value = FakeReducer.class)
    public void has(String param) {

    }


    public void not(String param) {

    }
  }

  public class ChildOfClass extends ParentClass {
    @Override
    public void has(String param) {

    }


    @Override
    public void not(String param) {

    }
  }


  public class Orphan {

    @Reduce(value = FakeReducer.class)
    public void has(String param) {

    }


    public void not(String param) {

    }

  }

  public static class FakeReducer extends AbstractReducer<String> {
    protected FakeReducer() {
      super(String.class);
    }

    @Override
    public String reduce(String state, Object action) {
      return state;
    }
  }
}


