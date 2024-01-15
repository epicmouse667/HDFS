package impl;
//TODO: your implementation
import api.DataNode;
import api.DataNodePOA;
import utils.FileDesc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataNodeImpl extends DataNodePOA {

    private String directory;
    public DataNodeImpl(int dataNodeId) {
        this.directory = "DATA/DN_" + dataNodeId;

        // 创建存储目录
        createStorageDirectory();
    }

    private void createStorageDirectory() {
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create directory: " + directory);
            }
        }
    }

    public byte[] read(int blockId) {
        Path path = Paths.get(directory, "block_"+String.valueOf(blockId));
        System.out.println(path);
        if (!Files.exists(path)) {
            System.out.println("Block does not exist: " + blockId);
            return new byte[0];
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read block: " + blockId);
            return new byte[0];
        }
    }

    public void append(int blockId, byte[] bytes) {
        Path path = Paths.get(directory, "block_"+String.valueOf(blockId));
        // 检查block是否存在，如果不存在，则创建一个新的
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                System.out.println("files created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create block: " + blockId);
                return;
            }
        }

        // 追加数据到block
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(path.toFile(), true))) {
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to append to block: " + blockId);
        }

    }


    @Override
    public int randomBlockId() {

        return 0;
    }
}
