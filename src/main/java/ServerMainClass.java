import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;
import static java.lang.Thread.sleep;

public class ServerMainClass {
    static ConcurrentLinkedQueue<String>list=new ConcurrentLinkedQueue<>();
    static Database MyBase=new Database();
    public static void main(String args[]) throws IOException {
        MyBase.connect();
        ServerSocket server = null;
        Socket OnServer=null;
        DataOutputStream out = null;
        DataInputStream in = null;
        Scanner reader = new Scanner(System.in);
        System.out.println("Waiting");
        server = new ServerSocket(8080);
        ExecutorService pool=new ThreadPoolExecutor(20,20,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(512));
        try {
            while (true) {
                OnServer = server.accept();
                pool.execute(new Server(OnServer,MyBase));
                sleep(1000);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    }

