package mException;

public class Exit extends Exception {
    private static final long serialVersionUID = 1L;

    public Exit() {}

    public Exit(String msg) {
        super(msg);
    }
}

