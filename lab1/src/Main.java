import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        byte[] a =Files.readAllBytes(Paths.get("DATA/DN_1/block_1"));
        byte[] b =Files.readAllBytes(Paths.get("DATA/DN_1/block_2"));
        byte[] byteout = new byte[a.length+b.length];
        System.arraycopy(a,0,byteout,0,a.length);
        System.arraycopy(b,0,byteout,a.length,b.length);
        for(int i = 0;i<a.length+b.length;i++){
            if(byteout[i]==0) System.out.println(i);
        }
        String out =  new String(byteout);
        System.out.println(out);
    }
}