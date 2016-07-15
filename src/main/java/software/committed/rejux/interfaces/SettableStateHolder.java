package software.committed.rejux.interfaces;

public interface SettableStateHolder<S> extends StateHolder<S> {

	void setState(S state);
}
