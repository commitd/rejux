package software.committed.rejux.interfaces;

public interface State<S> {

	Class<S> getType();

	S get();

	default S state() {
		return get();
	}
}
