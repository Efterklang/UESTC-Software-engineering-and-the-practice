package client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

public class MyObjectInputStream extends ObjectInputStream {
    public MyObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    /**
     * The readStreamHeader method is provided to allow subclasses to read and
     * verify their own stream headers. It reads and verifies the magic number
     * and version number.
     *
     * @throws IOException              if there are I/O errors while reading from
     *                                  the
     *                                  underlying {@code InputStream}
     * @throws StreamCorruptedException if control information in the stream
     *                                  is inconsistent
     */
    @Override
    protected void readStreamHeader() throws IOException, StreamCorruptedException {
        super.readStreamHeader();
    }
}
