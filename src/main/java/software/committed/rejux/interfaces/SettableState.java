package software.committed.rejux.interfaces;

public interface SettableState<S> extends State<S> {

	void setState(S state);
}
