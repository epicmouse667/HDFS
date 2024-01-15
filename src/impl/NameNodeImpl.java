package impl;
//TODO: your implementation
import api.NameNodePOA;
import com.google.gson.Gson;
import utils.FileDesc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


// ======== FsImage =======
//FsImage (name,replicas,block-ids,....)
//e.g. /data/<filename>/part-<index>,r:2,{1,3}



// We us FsImage.txt to store the fsimage of namenode, which is a <String filepath,FileDesc filedesc> mapping function.
// Once we open a file, we search for the corresponding filepath,filedesc pair in the txt file, get the filedesc and store
// it in a map member of namenode
//once we close the file, we write the filepath,filedesc pair back to the file
//notice that we need to take notice the mode of the file. we describte the mode in two digit binary numbers, if the first
//number is 1, then it is readable, if the second number is 1 then it is writeable. If we want to read a file that is not readable
//then return null
public class NameNodeImpl extends NameNodePOA {
    private Map<String, FileDesc> fileMap; // Map to store filepath and FileDesc pairs
    private final String fsImagePath = "DATA/FsImage.txt"; // Path to the FsImage file
    private static final AtomicLong idGenerator = new AtomicLong(0); // Global ID generator
    private Map<String, Boolean> writeLocks; // Tracks if a file is currently open for writing

    public NameNodeImpl() {
        fileMap = new HashMap<>();
        writeLocks = new HashMap<>();
        loadFsImage();
    }

    private void loadFsImage() {
        // Load the FsImage from the file
        Path path = Paths.get(fsImagePath);
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                Gson gson = new Gson();
                while ((line = reader.readLine()) != null) {
                    FileDesc fileDesc = gson.fromJson(line, FileDesc.class);
                    fileMap.put(fileDesc.getFilePath(), fileDesc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(String key : fileMap.keySet()) System.out.println(key);
        }
    }

    private void writeFsImage() {
        // Write the current state of the fileMap back to the FsImage file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fsImagePath))) {
            Gson gson = new Gson();
            for (FileDesc fileDesc : fileMap.values()) {
                writer.write(gson.toJson(fileDesc));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String open(String filepath, int mode) {
        FileDesc fileDesc = fileMap.get(filepath);
        boolean isWritable = (mode & 0b10) == 0b10;

//        boolean isWritable = true;
        if (fileDesc != null) {
            // Check if the file is currently open for writing
            if (isWritable && writeLocks.getOrDefault(filepath, false)) {
                // File is already open for writing and cannot be opened again for writing
                return null;
            }
        } else {
            // If the file doesn't exist, create a new FileDesc with a new unique ID
            long newId = idGenerator.incrementAndGet();
            fileDesc = new FileDesc(newId, mode, 0,filepath, new ArrayList<>(),
                    System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis());
            fileMap.put(filepath, fileDesc);
        }
        if (isWritable) {
            writeLocks.put(filepath, true);
        }

        writeFsImage(); // Update the FsImage file with the new state
        return new Gson().toJson(fileDesc);
    }

    @Override
    public void close(String fileInfo) {
        Gson gson = new Gson();
        FileDesc fileDesc = gson.fromJson(fileInfo, FileDesc.class);
        if (fileDesc != null && fileDesc.getFilePath() != null) {
            // Unlock the file for writing
            if ((fileDesc.getMode() & 0b01) == 0b01) {
                writeLocks.put(fileDesc.getFilePath(), false);
            }
            fileMap.put(fileDesc.getFilePath(), fileDesc);
            writeFsImage(); // Update the FsImage file with the new state
        }
    }
}
