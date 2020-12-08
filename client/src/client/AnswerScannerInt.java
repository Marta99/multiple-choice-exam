package client;

import java.io.IOException;
import java.util.Optional;

public interface AnswerScannerInt<T> {

    public Optional<T> scanAnswerID() throws IOException;
}
