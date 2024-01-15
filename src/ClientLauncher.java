import impl.ClientImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class    ClientLauncher {
    private static Map<Integer,String> fd2filename = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ClientImpl client = new ClientImpl();
        Scanner scanner = new Scanner(System.in);

        System.out.println("File Descriptor CLI");
        System.out.println("Commands: open <filename> <mode>, read <fd>, append <fd> <content>, close <fd>");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            handleCommand(client,command);
        }

    }
    public static void handleCommand(ClientImpl client,String command) {
        String[] parts = command.split(" ", 3);
        switch (parts[0]) {
            case "open":
                String modeStr = parts[2];
                int mode = 0;
                switch (modeStr){
                    case "r":
                        mode = 0b01;
                        break;
                    case "w":
                        mode = 0b10;
                        break;
                    case "rw":
                        mode = 0b11;
                        break;
                }
                int fd = client.open(parts[1],mode);
                fd2filename.put(fd,parts[1]);
                System.out.println(parts[1] +" has been opened, the fd is :"+fd);
                break;
            case "read":
                byte[] out = client.read(Integer.parseInt(parts[1]));
                System.out.println(new String(out));
                break;
            case "append":
                if (parts.length < 3) {
                    System.out.println("Not enough arguments for append");
                    break;
                }
                client.append(Integer.parseInt(parts[1]), parts[2].getBytes(StandardCharsets.UTF_8));
                System.out.println("append" + parts[2].getBytes(StandardCharsets.UTF_8)
                +" to file "+ fd2filename.get(parts[1]));
                break;
            case "close":
                client.close(Integer.parseInt(parts[1]));
                System.out.println(fd2filename.get(parts[1])+ "has been closed");
                break;
            default:
                System.out.println("Unknown command: " + parts[0]);
        }
    }

}
