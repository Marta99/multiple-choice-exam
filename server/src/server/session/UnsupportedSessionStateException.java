package server.session;

public class UnsupportedSessionStateException extends SessionException{
    public UnsupportedSessionStateException() {
        super();
    }

    public UnsupportedSessionStateException(String message) {
        super(message);
    }
}
