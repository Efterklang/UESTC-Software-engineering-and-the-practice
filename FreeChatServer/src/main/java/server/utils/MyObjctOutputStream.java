package server.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjctOutputStream {
    /*
     * How to Fix java.io.StreamCorruptedException: invalid type code in Java?
     * https://www.geeksforgeeks.org/how-to-fix-java-io-streamcorruptedexception-
     * invalid-type-code-in-java/
     */
    public class MyObjectOutputStream extends ObjectOutputStream {
        public MyObjectOutputStream(OutputStream out) throws Exception {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            return;
        }
    }

}
