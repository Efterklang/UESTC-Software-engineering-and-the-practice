package client.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/*
 * How to Fix java.io.StreamCorruptedException: invalid type code in Java?
 * Ref:https://www.geeksforgeeks.org/how-to-fix-java-io-streamcorruptedexception-invalid-type-code-in-java/
 */
public class MyObjectOutputStream extends ObjectOutputStream {
    public MyObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        return;
    }
}
