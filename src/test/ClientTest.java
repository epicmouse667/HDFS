package test;

import api.Client;
import impl.ClientImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;

public class ClientTest {
    static Client client;
    @Before
    public void setUp(){
        client = new ClientImpl();
    }

    @Test
    public void testWriteRead(){
//        String filename = FileSystem.newFilename();
        String filename = "1169093569TEST";
        int fd = client.open(filename, 0b11);
        client.append(fd,"hello".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(client.read(fd),"hello".getBytes(StandardCharsets.UTF_8));
        client.append(fd," world".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(client.read(fd)));
        assertArrayEquals(client.read(fd),"hello world".getBytes(StandardCharsets.UTF_8));
        client.close(fd);
    }

    @Test
    public void testWriteFail(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b01);
        client.append(fd,"Lala-land".getBytes(StandardCharsets.UTF_8));
        assertNull(client.read(fd));
        client.close(fd);
    }

    @Test
    public void testReadFail(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b10);
        assertNull(client.read(fd));
        client.close(fd);
    }
}
