import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{
    Socket OnServer = null;
    Database MyBase = null;
    DataOutputStream out = null;
    DataInputStream in = null;
    Scanner reader = new Scanner(System.in);
    Server(Socket OnServer,Database MyBase) throws IOException {
        this.MyBase=MyBase;
        this.OnServer=OnServer;
        out=new DataOutputStream(this.OnServer.getOutputStream());
        in =new DataInputStream(this.OnServer.getInputStream());
    }

    @Override
    public void run() {
        try {
            start();
        }catch (Exception e){
            System.out.println("客户端:"+OnServer.getInetAddress()+"已经下线");
        }
    }
    public void start(){
        System.out.println("客户端:"+OnServer.getInetAddress()+"已经上线");
        System.out.println("等待客户端:"+OnServer.getInetAddress()+"选择");
        try {
            String choice=in.readUTF();
            if(Integer.valueOf(choice)==1){
                System.out.println("客户端:"+OnServer.getInetAddress()+"登录");
                LogIn();
            } else if (Integer.valueOf(choice)==2) {
                System.out.println("客户端:"+OnServer.getInetAddress()+"注册");
                Register();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void LogIn() {
        try {
            out.writeUTF("Server get choice");
            out.flush();
            System.out.println("等待客户端账号密码");
            String Get = in.readUTF();
            Usr clientUsr = new Usr();
            String[] temp = Get.split(";");
            clientUsr.name = temp[0];
            clientUsr.password = temp[1];
            System.out.println(clientUsr.name + " " + clientUsr.password);
            if (MyBase.FindUsr(clientUsr)) {
                out.writeUTF("Find");
                out.flush();
                display(clientUsr);
            } else {
                out.writeUTF("NotFind");
                out.flush();
                LogIn();
            }

        } catch (Exception e) {
            System.out.println("客户端:" + OnServer.getInetAddress() + "已经下线");
        }
    }
    public void Register() {
        try {
            out.writeUTF("Server get choice");
            out.flush();
            System.out.println("等待客户端账号密码");
            String Get = in.readUTF();
            Usr clientUsr = new Usr();
            String[] temp = Get.split(";");
            clientUsr.name = temp[0];
            clientUsr.password = temp[1];
            System.out.println(clientUsr.name + " " + clientUsr.password);
            if (MyBase.CreateUsr(clientUsr)) {
                out.writeUTF("success");
                out.flush();
                LogIn();
            } else {
                out.writeUTF("false");
                out.flush();
                Register();
            }
        }catch (Exception e){
            System.out.println("客户端:"+OnServer.getInetAddress()+"已经下线");
        }
    }
    public void display(Usr clientUsr){
        try {
            String GET=in.readUTF();
            if(GET.compareTo("in display")==0){
                out.writeUTF("Server get it");
                out.flush();
                GET=in.readUTF();
                if(GET.compareTo("look")==0){
                    look(clientUsr);
                } else if (GET.compareTo("upload")==0) {
                    upload(clientUsr);
                } else if (GET.compareTo("download")==0) {
                    download(clientUsr);
                } else{
                    System.out.println("客户端:"+OnServer.getInetAddress()+"已退出");
                    out.close();
                    in.close();
                    OnServer.close();
                }

            }else{//客户端没响应，关闭连接
                System.out.println("客户端:"+OnServer.getInetAddress()+"无响应，断开连接");
                in.close();
                out.close();
                OnServer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void look(Usr clientUsr) throws IOException {
        String filepath="D:\\Desktop\\database\\"+clientUsr.name;

        File dir = new File(filepath);
        if  (!dir.exists()  && !dir.isDirectory())
        {
            System.out.println("//不存在");
            dir.mkdir();
            out.writeUTF("目录为空");
        } else {
            System.out.println("//目录存在");
            StringBuffer GET=new StringBuffer();
            String cout=displayDirectoryContents(clientUsr,dir,GET).toString();
            out.writeUTF(cout);
        }

    }
    public void upload(Usr clientUsr){

    }
    public void download(Usr clientUsr){

    }
    public static StringBuffer displayDirectoryContents(Usr clientUsr,File dir,StringBuffer GET) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("--目录:" + file.getCanonicalPath());
                    String []temp=file.getCanonicalPath().split("database");
                    GET.append("--目录:" + temp[1]+"\n");
                    displayDirectoryContents(clientUsr,file,GET);
                } else {
                    System.out.println("----文件:" + file.getCanonicalPath());
                    String []temp=file.getCanonicalPath().split("database");
                    GET.append("----文件:" + temp[1]+"\n");

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return GET;
    }

}
