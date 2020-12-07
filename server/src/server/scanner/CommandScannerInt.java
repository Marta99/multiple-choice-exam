package server.scanner;

import java.io.IOException;

public interface CommandScannerInt {

    public Command scan() throws IOException, UnsupportedCommandException;


}
