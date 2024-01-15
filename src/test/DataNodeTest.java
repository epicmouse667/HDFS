package test;

import api.DataNode;
import impl.DataNodeImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class DataNodeTest {
    static DataNodeImpl dn;

    @Before
    public void setUp() {
        dn = new DataNodeImpl(1);
    }

    @After
    public void rmData() throws IOException {
        for(File file : new File("DATA/DN_1").listFiles()){
            String url = file.getAbsolutePath();
            Path path = Paths.get(url);
            Files.delete(path);
        }
    }

    @Test
    public void testRead() {
        int blockId = dn.randomBlockId();
        assertNotNull(dn.read(blockId));
    }

    @Test
    public void testAppend() {
        int blockId = dn.randomBlockId();
        byte[] toWrite = "Hello World".getBytes(StandardCharsets.UTF_8);

        dn.append(blockId, toWrite);
        byte[] read = dn.read(blockId);

        int n = toWrite.length;
        int N = read.length;
        for (int i = 0; i < n; i++) {
            assertEquals("Block ID: " + blockId + ". Read block bytes and appended bytes differ at the " + i
                    + " byte to the eof.", toWrite[n - 1 - i], read[N - 1 - i]);
        }
    }
}
