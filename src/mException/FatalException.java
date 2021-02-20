package mException;

public class FatalException extends Exception {
    private static final long serialVersionUID = 1L;

    public FatalException() {}

    public FatalException(String msg) {
        super(msg);
    }
    
}
