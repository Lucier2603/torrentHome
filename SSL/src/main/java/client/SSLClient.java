package client;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangchen20
 */
public class SSLClient {

    // todo 信任库 证书库 ?
    // todo 需要在命令行添加如下虚拟机参数，指定信任库和密码：    如果服务端程序 setNeedClientAuth(true) 要求验证客户端身份，则我们还需要指定证书库和密码：
    // https://www.ibm.com/developerworks/cn/java/j-lo-socketkeytool/index.html


    // 本地信任证书库
    KeyStore trustKeyStore = null;

    // 本地客户端证书库
    KeyStore clientKeyStore = null;


    private SSLContext sslContext;

    private String remoteHost;
    private int remotePort;

    public void config(String remoteHost, int remotePort, String trustKSPath, String clientKSPath, String ksPasswd) throws Exception {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;

        // 本地信任证书库
        InputStream trustIn = new FileInputStream(trustKSPath);
        trustKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustKeyStore.load(trustIn, ksPasswd.toCharArray());
        trustIn.close();

        // 本地客户端证书库
        // 如果为null,那么单项认证
        if (StringUtils.isNoneBlank()) {
            InputStream clientIn = new FileInputStream(clientKSPath);
            clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            clientKeyStore.load(clientIn, ksPasswd.toCharArray());
            clientIn.close();
        }

        // 秘钥库
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientKeyStore, ksPasswd.toCharArray());

        // 信任库
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustKeyStore);

        // 初始化ssl context 上下文
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // get socket
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public void connect() throws Exception {

        SSLSocket sslSocket = null;

        try {

            sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(remoteHost, remotePort);

            sslSocket.startHandshake();

            System.out.println("Connected  !");

            Writer writer=new OutputStreamWriter(sslSocket.getOutputStream());
            writer.write("###  Hello World! ");
            writer.flush();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sslSocket.close();
        }
    }





}
