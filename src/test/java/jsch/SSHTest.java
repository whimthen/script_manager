package jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 15 下午3:2410 | 八月. 星期三
 */
public class SSHTest {

    private static long INTERVAL = 100L;
    private static int SESSION_TIMEOUT = 30000;
    private static int CHANNEL_TIMEOUT = 3000;
    private JSch jsch = null;
    private Session session = null;

    private SSHTest(SSHInfo sshInfo) throws JSchException {
        jsch =new JSch();
        session = jsch.getSession(sshInfo.getUser(),sshInfo.getHost(),sshInfo.getPort());
        session.setPassword(sshInfo.getPassword());
        session.setUserInfo(new MyUserInfo());
        session.connect(SESSION_TIMEOUT);
    }

    /*
     * 在这里修改访问入口,当然可以把这个方法弄到SSHExecutor外面，这里是方便操作才这么做的
     * */
    public static SSHTest newInstance() throws JSchException {
        SSHInfo sshInfo = new SSHInfo("blocklink","135642789","192.168.20.13",22);
        return new SSHTest(sshInfo);
    }

    /*
     * 注意编码转换
     * */
    public long shell(String cmd, String outputFileName) throws JSchException, IOException, InterruptedException {
        long start = System.currentTimeMillis();
        Channel channel = session.openChannel("shell");
        /*PipedInputStream pipeIn = new PipedInputStream();
        PipedOutputStream pipeOut = new PipedOutputStream(pipeIn );
//        FileOutputStream fileOut = new FileOutputStream(outputFileName, true);
        channel.setInputStream(pipeIn);
//        channel.setOutputStream(fileOut);
        channel.connect(CHANNEL_TIMEOUT);

        pipeOut.write(cmd.getBytes());
        Thread.sleep( INTERVAL );
        pipeOut.close();
        pipeIn.close();
//        fileOut.close();
        channel.disconnect();
        return System.currentTimeMillis() - start;*/

        //相对channel与sshd端，channel输出流是用于输出命令到sshd。
        OutputStream out = channel.getOutputStream();
        PrintStream commander = new PrintStream(out, true);

        channel.setOutputStream(out, true);

        // 可以使用以下注释的代码替代“channel.setOutputStream(System.out, true);”
        //相对channel与sshd端，channel输入流是用于接收sshd输出结果的
        // InputStream outputstream_from_the_channel = channel.getInputStream();
        // BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
        // String line;
        // while ((line = br.readLine()) != null)
        // System.out.print(line+"\n");

        channel.connect();

        out.write("'ll".getBytes());

        commander.println("ll");
//        commander.println("cd ~");
//        commander.println("ls -la");
//        commander.println("exit");
        commander.close();

        //如果输出到结尾，则可以退出等待。
        do {
            Thread.sleep(1000);
        } while (!channel.isEOF());
        return 0L;
    }

    public int exec(String cmd) throws IOException, JSchException, InterruptedException {
        ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
        channelExec.setCommand( cmd );
        channelExec.setInputStream( null );
        channelExec.setErrStream( System.err );
        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        int res = -1;
        StringBuffer buf = new StringBuffer( 1024 );
        byte[] tmp = new byte[ 1024 ];
        while ( true ) {
            while ( in.available() > 0 ) {
                int i = in.read( tmp, 0, 1024 );
                if ( i < 0 ) break;
                buf.append( new String( tmp, 0, i ) );
            }
            if ( channelExec.isClosed() ) {
                res = channelExec.getExitStatus();
                System.out.println( String.format( "Exit-status: %d", res ) );
                break;
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
        System.out.println( buf.toString() );
        channelExec.disconnect();
        return res;
    }

    public Session getSession(){
        return session;
    }

    public void close(){
        getSession().disconnect();
    }

    /*
     * SSH连接信息
     * */
    public static class SSHInfo{
        private String user;
        private String password;
        private String host;
        private int port;

        public SSHInfo(String user, String password, String host, int port) {
            this.user = user;
            this.password = password;
            this.host = host;
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

    /*
     * 自定义UserInfo
     * */
    private static class MyUserInfo implements UserInfo {

        @Override public String getPassphrase() { return null; }

        @Override public String getPassword() { return null; }

        @Override public boolean promptPassword(String s) { return false; }

        @Override public boolean promptPassphrase(String s) { return false; }

        @Override
        public boolean promptYesNo(String s) {
            System.out.println(s);
            System.out.println("true");
            return true;
        }

        @Override public void showMessage(String s) { }
    }

    public static void main(String[] args) throws JSchException, IOException, InterruptedException {
        SSHTest ssh =  SSHTest.newInstance();
//        System.out.println("================");
//        long shell1 = ssh.shell("ls\n","C:\\Users\\hidden\\Desktop\\shell.txt");
//        long shell2 = ssh.shell("pwd\n","C:\\Users\\hidden\\Desktop\\shell.txt");
//        System.out.println("shell 1 执行了"+shell1+"ms");
//        System.out.println("shell 2 执行了"+shell2+"ms");
//        System.out.println("================");
        int cmd1 = ssh.exec("ls -l\n");
        ssh.close();
    }

}
