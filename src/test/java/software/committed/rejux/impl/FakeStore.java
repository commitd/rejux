package software.committed.rejux.impl;

public interface FakeStore {

  default String hello() {
    return "hello " + world();
  }

  String world();

}
