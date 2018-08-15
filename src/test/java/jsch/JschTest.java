package jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import io.github.whimthen.script.entity.ServerUserInfo;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 15 下午3:1317 | 八月. 星期三
 */
public class JschTest extends TestCase {

    private static final String USER="blocklink";
    private static final String PASSWORD="135642789";
    private static final String HOST="192.168.20.13";
    private static final int DEFAULT_SSH_PORT=22;

    private static Channel channel;

    public static void main(String[] arg){

        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, DEFAULT_SSH_PORT);
            session.setPassword(PASSWORD);

            UserInfo userInfo = ServerUserInfo.newInstance();

            session.setUserInfo(userInfo);
            session.connect(30000);   // making a connection with timeout.

            channel = session.openChannel("shell");

            inOutput();
//
//            System.out.println("first");
//            TimeUnit.MINUTES.sleep(1);
//            channel.disconnect();
//
//            channel = session.openChannel("shell");
//            inOutput();
//
//            System.out.println("two");
            TimeUnit.MINUTES.sleep(1);
            System.exit(1);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public static void inOutput() throws FileNotFoundException, JSchException {
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        System.setIn(in);
        channel.setInputStream(System.in);
        File file = new File(JschTest.class.getClassLoader().getResource("").getPath() + "server.log");
        System.out.println(file.getAbsolutePath());
        FileOutputStream fileOut = new FileOutputStream(file, false);
        channel.setOutputStream(fileOut);
        channel.connect(3*1000);
    }

}
