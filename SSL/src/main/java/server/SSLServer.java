package server;

import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

/**
 * @author zhangchen20
 */
public class SSLServer {


    // server端证书
    protected KeyStore serverKeyStore = null;
    // server端可信任证书
    protected KeyStore trustKeyStore = null;


    /**
     * server socket
     */
    private SSLContext sslContext;

    private int listenPort;
    private boolean needClientAuth;


    public void config(int listenPort, boolean needClientAuth, String trustKSPath, String serverKSPath, String ksPasswd) throws Exception {
        this.listenPort = listenPort;
        this.needClientAuth = needClientAuth;

        // 本地信任证书库
        if (StringUtils.isNotBlank(trustKSPath)) {
            InputStream trustIn = new FileInputStream(trustKSPath);
            trustKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustKeyStore.load(trustIn, ksPasswd.toCharArray());
            trustIn.close();
        }

        // 本地客户端证书库
        InputStream serverIn = new FileInputStream(serverKSPath);
        serverKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        serverKeyStore.load(serverIn, ksPasswd.toCharArray());
        serverIn.close();

        // 秘钥库
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(serverKeyStore, ksPasswd.toCharArray());

        // 信任库
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustKeyStore);

        // 初始化ssl context 上下文
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    }


    public void start() throws IOException {

        SSLServerSocket serverSocket = null;

        try {
            serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(listenPort);

            serverSocket.setNeedClientAuth(needClientAuth);

            while (true) {

                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();

                // todo readHandle
                handlerClientSocket(clientSocket);

            }

        } catch (Exception e) {
            // todo config exception
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }



    private void handlerClientSocket(SSLSocket clientSocket) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line = reader.readLine();
        System.out.println(line);
        reader.close();
    }

}
