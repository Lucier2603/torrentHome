package server;

import javax.net.ssl.SSLContext;

/**
 * @author zhangchen20
 */
public class SSLServerFactory {

    /**
     * 默认信息
     */
    private static final String DEFAULT_PROTOCAOL = "TLS";

    public static SSLServerFactory create() {
        return new SSLServerFactory();
    }

    private SSLServerFactory() {
        this.protocol = DEFAULT_PROTOCAOL;
    }

    /**
     * 必要信息
     */
    private String protocol;
    private String listenPort;
    private boolean needClientAuth;

    // todo not void
    public SSLServer build() {

        SSLServer server = null;

        try {
            SSLContext sslc= SSLContext.getInstance(this.protocol);



        } catch (Exception e) {
            // todo 区分Exception
        }

        return server;
    }
}
