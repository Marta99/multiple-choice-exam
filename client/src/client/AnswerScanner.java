package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class AnswerScanner implements AnswerScannerInt<Integer> {
    private final BufferedReader reader;

    public AnswerScanner() {
        InputStreamReader input = new InputStreamReader(System.in);
        this.reader = new BufferedReader(input);
    }

    @Override
    public Optional<Integer> scanAnswerID() throws IOException {
        try {
            return Optional.of(Integer.parseInt(reader.readLine()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
