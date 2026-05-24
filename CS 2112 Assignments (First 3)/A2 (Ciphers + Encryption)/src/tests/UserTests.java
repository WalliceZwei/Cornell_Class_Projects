package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cipher.Cipher;
import cipher.CipherFactory;
import org.junit.jupiter.api.Test;

import java.io.*;

public class UserTests {
    private final CipherFactory cipherFactory = new CipherFactory();

    @Test
    void testNegativeShiftCaesar() {
        Cipher caesar = cipherFactory.getCaesarCipher(-21);
        assertEquals("wmnst", caesar.encrypt("rhino"));
        assertEquals("btrgfy", caesar.encrypt("wombat"));
    }

    @Test
    void testModulusCaesar() {
        Cipher caesar = cipherFactory.getCaesarCipher(31);
        assertEquals("wmnst", caesar.encrypt("rhino"));
        assertEquals("btrgfy", caesar.encrypt("wombat"));
    }

    @Test
    void testExtraCharCaesar() {
        Cipher caesar = cipherFactory.getCaesarCipher(5);
        assertEquals("wmnst", caesar.encrypt("r!@)9h!)*#in!*@)@)0+_+o"));
        assertEquals("btrgfy", caesar.encrypt("w!)@(#omb()@*!)#*at>><"));
    }

    @Test
    void testUTF8CharsRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "´∂ß∆˚øˆ∑œ´∆´¨ˆø∆ß∂sadkjqu";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

    @Test
    void testNothingRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

    @Test
    void testChunkingRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "CS 2112/ENGRD 2112 is an honors version of CS 2110/ENGRD 2110. Credit is given for only one of 2110 and 2112. Transfer between 2110 and 2112 (in either direction) is encouraged during the first three weeks. We cover intermediate software design and introduce some key computer science ideas. The topics are similar to those in 2110 but are covered in greater depth with more challenging assignments. Topics include object-oriented programming, program structure and organization, program reasoning using specifications and invariants, recursion, design patterns, concurrent programming, graphical user interfaces, data structures, sorting and graph algorithms, asymptotic complexity, and simple algorithm analysis. Java is the principal programming language.";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

    @Test
    // i is added because 126 % 2 == 0, so we want to create a conflict where the 2 byte is discluded from the previous chunk
    void test2ByteChunkingRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "iøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøø";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

    // i is added for the same reason as before, except 126%3 == 0 instead of 2
    @Test
    void test3ByteChunkingRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "iππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππππ";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

    @Test
    void test4ByteChunkingRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDE0E";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

    @Test

    void testMonoAlphabetIdentity() throws Exception{
        Cipher mono = cipherFactory.getMonoCipher("abcdefghijklmnopqrstuvwxyz");
        assertEquals("yes", mono.encrypt("yes"));
        assertEquals("", mono.encrypt("80192"));
    }

    @Test

    void testMonoAlphabetNormal() throws Exception{
        Cipher mono = cipherFactory.getMonoCipher("bcdefghijklmnopqrstuvwxyza");
        assertEquals("bcde", mono.encrypt("abcd"));
        assertEquals("abcd", mono.encrypt("zabc"));
    }





    @Test
    void testHardStr() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "rsa␣is␣alright,␣i␣guess";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }

}
