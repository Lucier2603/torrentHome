import client.SSLClient;
import server.SSLServer;

/**
 * @author zhangchen20
 */
public class Startup {

    public static void main(String[] args) {

        int port = 8902;

        try {
            SSLServer server = new SSLServer();
            server.config(port, false, null, "/Users/baidu/code/Lucifer2603/keystore/server/sslserverkeys", "11223344");

            ServerRunner runner = new ServerRunner();
            runner.sslServer = server;

            new Thread(runner).start();


            Thread.sleep(5 * 1000);


            SSLClient client = new SSLClient();
            client.config("127.0.0.1", port, "/Users/baidu/code/Lucifer2603/keystore/sslclienttrust", null, "11223344");
            client.connect();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class ServerRunner implements Runnable {

        SSLServer sslServer;

        public void run() {
            try {
                sslServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
