package cipher;

import java.io.InputStream;
import java.io.EOFException;
import java.io.IOException;

public class Chunks implements ChunkReader{


    // Class Invariant: All characters must have their full byte representation put inside a chunk
    // A chunk must <= chunk size 126 (positve integer)
    // Given a proper and correct inputstream and valid byte array
    // 0 index is the length of byte data, the rest is the byte data


    private InputStream byteScanner;

    Chunks(InputStream i){
        this.byteScanner = i;
    }

    public int chunkSize = 126;

    /**
     * Returns the maximum number of bytes in a chunk.
     */

    public int chunkSize() {return this.chunkSize;}

    /**
     * Checks inputstream for the next byte
     * @return True if there is another byte, False if there isn't another byte
     */

    // Basically, returns if you can read the next line, and also makes sure to be able to backtrack to that bit with mark() to read it

    public boolean hasNext(){
        byteScanner.mark(1);
        try {
            return byteScanner.read() != -1;
        }
        catch (IOException e){
            return false;
        }
    }

    /**
     * Returns the next chunk of up to {@code chunkSize()} bytes from the current
     * input stream. The returned bytes are placed in the array {@code data},
     * starting from index 0. The number of bytes returned is always
     * {@code chunkSize()}, unless the end of the input stream has been reached and
     * there are fewer than {@code chunkSize()} bytes available, in which case all
     * remaining bytes are returned. The values in {@code data} after the region in
     * which bytes were written are unspecified.
     *
     * @param data An array of length at least {@code chunkSize()}.
     * @return The number of bytes returned, which is always between 1 and the chunk
     *         size.
     * @throws EOFException if there are no more bytes available.
     * @throws IOException  if any IO exception occurs.
     */

    //Uses .reset() to account for hasNext() reading the input, uses a variable to track length of chunk
    // and makes sure through code that there aren't any multi-byte characters that would overflow the chunk size
    // using the properties of multi-byte characters via Wikipedia (110 for two bytes character, 1110 for 3, 11110 for 4)

    public int nextChunk (byte[] data) throws IOException, EOFException {
        int datapointer = 0;

        while (datapointer < chunkSize() && hasNext()){
            try {
                byteScanner.reset();
            }
            catch (IOException e){
                throw new IOException("Input Resetting Error", e);
            }
            try {
                int signedinteger = byteScanner.read();
                if (chunkSize() < 128 && datapointer > 122) {
                    if ((224 & signedinteger) == 192) {
                        if (datapointer + 2 >= chunkSize()) {
                            byteScanner.reset();
                            break;
                        }
                    } else if ((240 & signedinteger) == 224) {
                        if (datapointer + 3 >= chunkSize()) {
                            byteScanner.reset();
                            break;
                        }
                    } else if ((248 & signedinteger) == 240) {
                        byteScanner.reset();
                        break;
                    }
                }
                datapointer++;
                data[datapointer] = (byte) signedinteger;
            }
            catch (IOException e){
                throw new IOException("Input Reading Error", e);
            }
        }
        if (datapointer == 0){
            throw new EOFException("No more bytes to chunk from input stream");
        }
        data[0] = (byte) datapointer;
        return datapointer;
    }
}
