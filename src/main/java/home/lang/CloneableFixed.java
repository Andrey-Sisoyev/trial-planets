package home.lang;

public interface CloneableFixed extends Cloneable {
    /**
     * @throws RTCloneNotSupported Runtime exception.
     */
    public CloneableFixed clone();
}
