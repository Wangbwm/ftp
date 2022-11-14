import java.sql.*;

public class Database {
    Connection con = null;
    public void connect(){
        System.out.println("连接数据库");
        try {
            Class.forName("com.mysql.ci.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String uri = "jdbc:mysql://localhost/ftp?characterEncoding = utf-8&useSSL = false&serverTimezone = GMT";
        String user = "root";
        String password = "1259963577";
        try {
            con = DriverManager.getConnection(uri, user, password);
            if(con!=null) {
                System.out.println("连接成功");
            }else{
                System.out.println("连接失败");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean FindUsr(Usr usr){

        try {
            Statement sql = con.createStatement();
            ResultSet rs = sql.executeQuery("select * from usr where USR ='"+usr.name+"' and password ='"+usr.password+"' ");
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean CreateUsr(Usr usr){
        try {
            Statement sql = con.createStatement();
            ResultSet rs = sql.executeQuery("select * from usr where USR ='"+usr.name+"' ");
            if(rs.next()){
                return false;
            }else{
                sql = con.createStatement();
                String SQL="insert into usr values('"+usr.name+"','"+usr.password+"') ";
                sql.executeUpdate("insert into usr values('"+usr.name+"','"+usr.password+"') ");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
