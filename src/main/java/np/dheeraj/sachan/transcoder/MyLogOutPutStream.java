package np.dheeraj.sachan.transcoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 10/29/14
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MyLogOutPutStream extends OutputStream {


        private static final int INTIAL_SIZE = 132;

        /** Carriage return */
        private static final int CR = 0x0d;

        /** Linefeed */
        private static final int LF = 0x0a;

        /** the internal buffer */
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(
                INTIAL_SIZE);

        private boolean skip = false;

        private final int level;

        /**
         * Creates a new instance of this class.
         * Uses the default level of 999.
         */
        public MyLogOutPutStream() {
            this(999);
        }

        /**
         * Creates a new instance of this class.
         *
         * @param level loglevel used to log data written to this stream.
         */
        public MyLogOutPutStream(final int level) {
            this.level = level;
        }

        /**
         * Write the data to the buffer and flush the buffer, if a line separator is
         * detected.
         *
         * @param cc data to log (byte).
         * @see java.io.OutputStream#write(int)
         */
        public void write(final int cc) throws IOException {
            final byte c = (byte) cc;
            buffer.write(cc);
            if(buffer.toString().contains("frame="))
            {
                processBuffer();
            }
        }

        /**
         * Flush this log stream.
         *
         * @see java.io.OutputStream#flush()
         */
        public void flush() {
            if (buffer.size() > 0) {
                processBuffer();
            }
        }

        /**
         * Writes all remaining data from the buffer.
         *
         * @see java.io.OutputStream#close()
         */
        public void close() throws IOException {
            if (buffer.size() > 0) {
                processBuffer();
            }
            super.close();
        }

        /**
         * @return the trace level of the log system
         */
        public int getMessageLevel() {
            return level;
        }

        /**
         * Write a block of characters to the output stream
         *
         * @param b the array containing the data
         * @param off the offset into the array where data starts
         * @param len the length of block
         * @throws java.io.IOException if the data cannot be written into the stream.
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(final byte[] b, final int off, final int len)
                throws IOException {
            // find the line breaks and pass other chars through in blocks
            int offset = off;
            int blockStartOffset = offset;
            int remaining = len;
            while (remaining > 0) {
                while (remaining > 0 && b[offset] != LF && b[offset] != CR) {
                    offset++;
                    remaining--;
                }
                // either end of buffer or a line separator char
                int blockLength = offset - blockStartOffset;
                if (blockLength > 0) {
                    buffer.write(b, blockStartOffset, blockLength);
                }
                while (remaining > 0 && (b[offset] == LF || b[offset] == CR)) {
                    write(b[offset]);
                    offset++;
                    remaining--;
                }
                blockStartOffset = offset;
            }
        }

        /**
         * Converts the buffer to a string and sends it to <code>processLine</code>.
         */
        protected void processBuffer() {
            processLine(buffer.toString());
            buffer.reset();
        }

        /**
         * Logs a line to the log system of the user.
         *
         * @param line
         *            the line to log.
         */
        protected void processLine(final String line) {
            processLine(line, level);
        }

        /**
         * Logs a line to the log system of the user.
         *
         * @param line the line to log.
         * @param level the log level to use
         */
        protected abstract void processLine(final String line, final int level);


}
