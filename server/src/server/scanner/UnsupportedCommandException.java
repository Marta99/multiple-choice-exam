package server.scanner;

public class UnsupportedCommandException extends Exception {
    public UnsupportedCommandException() {
    }

    public UnsupportedCommandException(String message) {
        super(message);
    }
}
