package impl;
//TODO: your implementation
import api.Client;
import api.DataNode;
import api.NameNode;
import api.NameNodeHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import sun.nio.cs.ext.MacThai;
import utils.FileDesc;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ClientImpl implements Client{

    final int MAX_DATA_NODE = 1;
    final int MAX_BLOCK_SIZE = 4*1024;
    private static final AtomicLong fdGenerator = new AtomicLong(0);
    private static final AtomicLong blockidGenerator = new AtomicLong(0);
    private NameNode nameNode;
    private DataNode[] dataNodes = new DataNode[MAX_DATA_NODE];
    private Map<Integer,FileDesc> fdMap;
    public ClientImpl(){
        this.fdMap = new HashMap<>();
        try{
            String[] args = {};
            Properties properties = new Properties();
            properties.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            properties.put("org.omg.CORBA.ORBInitialPort", "1050");
            ORB orb = ORB.init(args,properties);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            nameNode = NameNodeHelper.narrow(ncRef.resolve_str("NameNode"));
            System.out.println("nameNode is obtained");
            for (int i = 0; i < 1; i++) {
                int j = i + 1;
                dataNodes[i] = api.DataNodeHelper.narrow(ncRef.resolve_str("DataNode" + j));
                System.out.println("dataNode" + j + " is obtained");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int open(String filepath, int mode) {
        String str =  nameNode.open(filepath,mode);
        System.out.println("filedesc str:"+str);
        if(str == null)return -1;
        else{
            FileDesc fileDesc = FileDesc.fromString(str);
            fileDesc.setLastAccessTime(System.currentTimeMillis());
            long fd = fdGenerator.incrementAndGet();
            fdMap.put((int)fd,fileDesc);
            return (int)fd;
        }

    }

@Override
public void append(int fd, byte[] bytes)  {
    FileDesc fileDesc = fdMap.get(fd);
    if (fileDesc == null || fileDesc.getMode() == 0b01) {
        // Either file descriptor is not found or the mode is not suitable for appending
        return;
    }
    int totalSize = bytes.length;
    int processedSize = 0;
    long size = fileDesc.getSize();
    int remnant = (int)size%MAX_BLOCK_SIZE;
    int blockId;
    int dataNodeIndex=1;
    List<String> blockPaths = fileDesc.getBlockPaths();
    byte [] out ;
    String blockPath;
    while(processedSize<totalSize){
        dataNodeIndex = dataNodeIndex%MAX_DATA_NODE+1;
        out = new byte[MAX_BLOCK_SIZE];
        blockId = (int)blockidGenerator.incrementAndGet();
        System.arraycopy(bytes,processedSize,out,0,Math.min(totalSize-processedSize,MAX_BLOCK_SIZE));
        DataNode dataNode = dataNodes[dataNodeIndex-1];
        System.out.println("out is :"+new String(out));
        dataNode.append(blockId,out);
        processedSize+=Math.min(totalSize-processedSize,MAX_BLOCK_SIZE);
        blockPath = "dataNode: " + dataNodeIndex + " block: " + blockId;
        blockPaths.add(blockPath);
        fileDesc.setBlockPaths(blockPaths);
        fdMap.put(fd,fileDesc);
    }
    fileDesc.setSize(size + totalSize);
    fileDesc.setLastModifyTime(System.currentTimeMillis());
    System.out.println(fileDesc.toString());
}

    @Override
    public byte[] read(int fd) {
        FileDesc fileDesc = fdMap.get(fd);
        int mode = fileDesc.getMode();
        if (fileDesc == null || (mode & 0b01) != 0b01) {
            // Either file descriptor is not found or the mode is not suitable for appending
            return null;
        }
        int totalSize = (int) fileDesc.getSize();
        byte[] bytes = new byte[(int) totalSize];
        List<String> blockLocations = fileDesc.getBlockPaths();
        if (blockLocations.isEmpty()) {
            return null;
        }
        int processedSize = 0;
        for (String blockLocation : blockLocations) {
            String[] parts = blockLocation.split(" ");
            int dataNodeIndex = Integer.parseInt(parts[1]);
            int blockId = Integer.parseInt(parts[3]);
            DataNode dataNode = this.dataNodes[dataNodeIndex - 1];
            System.out.println("blockid:"+blockId);
            byte[] bytesRead = dataNode.read(blockId);
            int pos =0;
            for (pos=0;pos<bytesRead.length;pos++){
                if(bytesRead[pos]==0)break;
            }
            System.arraycopy(bytesRead, 0, bytes, processedSize, pos);
            processedSize += pos;
        }
        return bytes;
    }

    @Override
    public void close(int fd) {
        FileDesc fileDesc = fdMap.get(fd);
        if (fileDesc == null) {
            return;
        }
        nameNode.close(fileDesc.toString());
        fdMap.remove(fd);
    }


}
