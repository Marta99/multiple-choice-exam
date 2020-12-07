package server.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Scanner implements ScannerInt {

    private final BufferedReader reader;

    public Scanner() {
        InputStreamReader input = new InputStreamReader(System.in);
        this.reader = new BufferedReader(input);
    }

    @Override
    public Command scan() throws IOException, UnsupportedCommandException {
        String command = reader.readLine();
        switch (command) {
            case "start" :
                return Command.START_EXAM;
            case "finish" :
                return Command.FINISH_EXAM;
        }
        throw new UnsupportedCommandException("\"" + command + "\" is not a supported command.");
    }
}
