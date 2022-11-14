import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientMainClass {
    public static void main(String args[]){
        Socket MySocket=null;
        DataInputStream in=null;
        DataOutputStream out=null;
        Scanner reader=new Scanner(System.in);
        try {
            MySocket=new Socket("127.0.0.1",8080);
            Client clientThread=new Client(MySocket);
            clientThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
