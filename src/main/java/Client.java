import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{
    Socket MySocket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    Scanner reader=new Scanner(System.in);
    Client(Socket MySocket) throws IOException {
        this.MySocket=MySocket;
        in=new DataInputStream(this.MySocket.getInputStream());
        out=new DataOutputStream(this.MySocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            Start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Start() throws IOException {
        System.out.println("欢迎使用MyFTP");
        System.out.println("1.登录 2.注册");
        int choice=reader.nextInt();

            if(choice==1) {
                out.writeUTF(String.valueOf(choice));
                out.flush();
                LogIn();
            } else if (choice==2) {
                out.writeUTF(String.valueOf(choice));
                out.flush();
                Register();
            }
            else{
                System.out.println("输入错误！");
                Start();
            }
        }


    public void LogIn() throws IOException {
        String GET=in.readUTF();
        while (GET.compareTo("Server get choice")!=0){
            GET=in.readUTF();
        }
        System.out.println("登录FTP");
        System.out.print("输入账号:");
        String name=reader.next();
        System.out.print("输入密码:");
        String password=reader.next();
        out.writeUTF(name+";"+password);
        out.flush();
        GET=in.readUTF();
        if(GET.compareTo("Find")==0){
            System.out.println("密码正确！");
            Usr clientUsr=new Usr();
            clientUsr.name=name;
            clientUsr.password=password;
            display(clientUsr);
        }
        else{
            System.out.println("密码错误！");
            LogIn();
        }
    }

    public void Register() throws IOException {
        String GET=in.readUTF();
        while (GET.compareTo("Server get choice")!=0){
            GET=in.readUTF();
        }
        System.out.println("注册账号");
        System.out.print("输入账号:");
        String name=reader.next();
        System.out.print("输入密码:");
        String password=reader.next();
        out.writeUTF(name+";"+password);
        out.flush();
        GET=in.readUTF();
        if(GET.compareTo("success")==0){
            System.out.println("注册成功！");
            LogIn();
        }
        else{
            System.out.println("注册失败！");
            Register();
        }
    }

    public void display(Usr clientUsr){
        try {
            out.writeUTF("in display");
            out.flush();
            String GET=in.readUTF();
            if(GET.compareTo("Server get it")==0){
                menu(clientUsr);
            }else{
                System.out.println("服务器无响应，关闭连接");
                in.close();
                out.close();
                MySocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void menu(Usr clientUsr) throws IOException {
        Scanner reader=new Scanner(System.in);
        System.out.println("FTP菜单");
        System.out.println("1. 查看文件");
        System.out.println("2. 上传");
        System.out.println("3. 下载");
        System.out.println("4. 退出");
        int choice=-1;
        System.out.print("输入：");
        choice=reader.nextInt();
        switch (choice){
            case 1:{
                out.writeUTF("look");
                out.flush();
                look(clientUsr);
            }
            case 2:{
                out.writeUTF("upload");
                out.flush();
                upload(clientUsr);
            }
            case 3:{
                out.writeUTF("download");
                out.flush();
                download(clientUsr);
            }
            case 4:{
                out.writeUTF("exit");
                out.flush();
                in.close();
                out.close();
                MySocket.close();
                System.out.println("已经退出");
                System.exit(0);
            }
            default:{
                System.out.println("输入错误");
                menu(clientUsr);
            }
        }
    }
    public void look(Usr clientUsr) throws IOException {
        String GET=new String();
        GET=in.readUTF();
        System.out.println("文件浏览：");
        System.out.println(GET);
    }
    public void upload(Usr clientUsr){

    }
    public void download(Usr clientUsr){

    }

}
