package server.scanner;

import java.io.IOException;

public interface ScannerInt {

    public Command scan() throws IOException, UnsupportedCommandException;


}
