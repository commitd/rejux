package sofware.committed.rejux;

public interface SettableStateHolder<S> extends StateHolder<S> {

	void setState(S state);
}
