package utils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//TODO: According to your design, complete the FileDesc class, which wraps the information returned by NameNode open()
public class FileDesc {
    /* the id should be assigned uniquely during the lifetime of NameNode,
     * so that NameNode can know which client's open has over at close
     * e.g., on nameNode1
     * client1 opened file "Hello.txt" with mode 'w' , and retrieved a FileDesc with 0x889
     * client2 tries opening the same file "Hello.txt" with mode 'w' , and since the 0x889 is not closed yet, the return
     * value of open() is null.
     * after a while client1 call close() with the FileDesc of id 0x889.
     * client2 tries again and get a new FileDesc with a new id 0x88a
     */
    private long id;
    private int mode;
    private long size;
    private String filePath;
    private List<String> blockPaths;
    private long createTime;
    private long lastAccessTime;
    private long lastModifyTime;

    // Constructor with all parameters
    public FileDesc(long id, int mode,long size, String filePath, List<String> blockPaths, long createTime, long lastAccessTime, long lastModifyTime) {
        this.id = id;
        this.size = size;
        this.mode = mode;
        this.filePath = filePath;
        this.blockPaths = blockPaths;
        this.createTime = createTime;
        this.lastAccessTime = lastAccessTime;
        this.lastModifyTime = lastModifyTime;
    }

    public FileDesc(long id){
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getBlockPaths() {
        return blockPaths;
    }

    public long getSize() {
        return size;
    }

    public int getMode() {
        return mode;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setBlockPaths(List<String> blockPaths) {
        this.blockPaths = blockPaths;
    }

    @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }

        // Parse from a JSON string

        public static FileDesc fromString(String jsonStr) {
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, FileDesc.class);
        }

        // Example usage
        public static void main(String[] args) {
            List<String> blockPaths = new ArrayList<>();
//            blockPaths.add("/Data/DN_1/BLOCK1");
//            blockPaths.add("/Data/DN_2/BLOCK2");

            // Create a FileDesc object
            FileDesc fileDesc = new FileDesc(12345, 777,0, "/path/to/file", blockPaths, System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis());

            // Convert to a JSON string
            String jsonStr = fileDesc.toString();
            System.out.println("FileDesc as JSON: " + jsonStr);

            // Convert back from the JSON string
            FileDesc parsedFileDesc = FileDesc.fromString(jsonStr);
            System.out.println("Parsed FileDesc: " + parsedFileDesc.toString());
        }
}


