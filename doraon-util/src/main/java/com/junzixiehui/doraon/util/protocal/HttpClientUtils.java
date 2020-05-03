package com.junzixiehui.doraon.util.protocal;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.junzixiehui.doraon.util.constant.SymbolConstant;
import com.junzixiehui.doraon.util.exception.HttpCallException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: qulb2
 * @description:
 * @date: 18:54 2017/12/8
 * @modify：
 */
public class HttpClientUtils {
    /**
     * 调用外部接口专用日志
     */
    private static final Logger CONSUME_LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    /**
     * 默认编码
     */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * 默认最大请求超时时间10秒
     */
    public static final int DEFAULT_REQUEST_TIMEOUT = 6000;
    /**
     * 默认keepalive时间1分钟
     */
    public static final long DEFAULT_KEEPALIVE_TIME = 60000L;
    /**
     * http连接池管理器
     */
    private final PoolingClientConnectionManager poolingClientConnectionManager;
    /**
     * http客户端
     */
    private final DefaultHttpClient defaultHttpClient;
    /**
     * 定期回收过期链接线程
     */
    private static final IdleConnectionMonitorThread idleThread;

    static {
        idleThread = new IdleConnectionMonitorThread();
        idleThread.setDaemon(true);
        idleThread.start();
    }

    public HttpClientUtils() {
        /**
         * 创建链接管理器
         */
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        poolingClientConnectionManager = new PoolingClientConnectionManager(schemeRegistry);
        /**
         * 创建httpClient
         */
        defaultHttpClient = new DefaultHttpClient(poolingClientConnectionManager);
        /**
         * 设置keep-alive连接保持活动的策略
         */
        defaultHttpClient.setKeepAliveStrategy(new HttpConnectionKeepAliveStrategy());
        /**
         * 设置请求重试Handler
         */
        defaultHttpClient.setHttpRequestRetryHandler(new HttpRequestRetryHandler());
        HttpParams params = defaultHttpClient.getParams();
        /**
         * 设置默认协议参数
         */
        HttpProtocolParams.setContentCharset(params, this.getContentCharset());
        HttpProtocolParams.setUserAgent(params, this.getUserAgent());
        /**
         * 设置默认链接参数
         */
        HttpConnectionParams.setTcpNoDelay(params, this.isTcpNodelay());
        HttpConnectionParams.setSoTimeout(params, this.getSoTimeout());
        HttpConnectionParams.setLinger(params, this.getSoLinger());
        HttpConnectionParams.setSoReuseaddr(params, this.isSoReuseaddr());
        HttpConnectionParams.setSocketBufferSize(params, this.getSocketBufferSize());
        HttpConnectionParams.setConnectionTimeout(params, this.getConnectionTimeout());
        HttpConnectionParams.setStaleCheckingEnabled(params, this.isStaleConnectionCheck());
        HttpConnectionParams.setSoKeepalive(params, this.isKeepAlive());
        /**
         * 设置httpClient行为参数
         */
        HttpClientParams.setRedirecting(params, this.isHandleRedirects());
        params.setIntParameter(ClientPNames.MAX_REDIRECTS, this.getMaxRedirects());
        /**
         * 设置链接管理器参数
         */
        HttpClientParams.setConnectionManagerTimeout(params, this.getConnManagerTimeout());
        poolingClientConnectionManager.setMaxTotal(this.getMaxTotal());
        poolingClientConnectionManager.setDefaultMaxPerRoute(this.getMaxPerRoute());
        /**
         * 设置动态调整链接管理器池大小
         */
        // defaultHttpClient.setConnectionBackoffStrategy(new DefaultBackoffStrategy());
        // defaultHttpClient.setBackoffManager(new AIMDBackoffManager(poolingClientConnectionManager));

        /**
         * 定期回收连接池中失效链接线程
         */
        idleThread.addConnMgr(poolingClientConnectionManager);
    }

    public void shutdown() {
        idleThread.shutdown();
        defaultHttpClient.getConnectionManager().shutdown();
    }

    /**
     * get请求文本
     *
     * @param url - 请求URL
     * @return string
     * @throws IllegalArgumentException
     * @throws
     */
    public String httpGet(String url) {
        return httpGet(url, null, DEFAULT_REQUEST_TIMEOUT, DEFAULT_ENCODING);
    }

    /**
     * get请求文本
     *
     * @param url         - 请求URL
     * @param queryParams - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @return string
     * @throws IllegalArgumentException
     * @throws
     */
    public String httpGet(String url, Map<String, String> queryParams) {
        return httpGet(url, queryParams, DEFAULT_REQUEST_TIMEOUT, DEFAULT_ENCODING);
    }

    /**
     * get请求文本
     *
     * @param url            - 请求URL
     * @param queryParams    - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @param requestTimeout - 请求超时时间,毫秒数
     * @return string
     * @throws IllegalArgumentException
     * @throws
     */
    public String httpGet(String url, Map<String, String> queryParams, int requestTimeout) {
        return httpGet(url, queryParams, requestTimeout, DEFAULT_ENCODING);
    }

    /**
     * get请求文本
     *
     * @param url            - 请求URL
     * @param queryParams    - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @param requestTimeout - 请求超时时间,毫秒数
     * @param encoding       字符编码
     * @return string
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpGet(String url, Map<String, String> queryParams, int requestTimeout, String encoding) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "Param url must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

        String urlToSend = url;
        String result = null;

        /**
         * 消费者日志
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(urlToSend).append(SymbolConstant.BAR);
        // 参数打印控制1000字符以内
        if (queryParams != null) {
            sb.append(queryParams.toString()).append(SymbolConstant.BAR);
        }
        /**
         * 创建查询参数,如果设置了queryParams
         */
        if (queryParams != null && !queryParams.isEmpty()) {
            List<NameValuePair> qparams = Lists.newArrayList();
            for (Entry<String, String> entry : queryParams.entrySet()) {
                qparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            String queryParamStr = URLEncodedUtils.format(qparams, encoding);
            if (url.endsWith(SymbolConstant.QUESTION)) {
                urlToSend = urlToSend + queryParamStr;
            } else {
                urlToSend = urlToSend + SymbolConstant.QUESTION + queryParamStr;
            }
        }

        try {
            /**
             * 创建HttpGet实例
             */
            HttpGet httpGet = new HttpGet(urlToSend);

            if (requestTimeout > 0) {
                /**
                 * 设置request级别参数,覆盖httpClient默认参数
                 */
                httpGet.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, requestTimeout);
            }

            /**
             * 发送请求
             */
            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
            /**
             * 获取响应
             */
//            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
            // EntityUtils.toString自动关闭输入流
            result = EntityUtils.toString(httpResponse.getEntity(), encoding);
//            } else {
//                EntityUtils.consume(httpResponse.getEntity());
//            }
        } catch (Exception ex) {
            CONSUME_LOGGER.error("HttpClientUtils.httpGet error.", ex);
            sb.append("Exception:").append(ex.getMessage()).append(SymbolConstant.BAR);
            throw new HttpCallException("HttpClientUtils.httpGet error.", ex);
        } finally {
            // 结果打印控制1000字符以内
            sb.append(result).append(SymbolConstant.BAR);
            sb.append(System.currentTimeMillis() - start);
            CONSUME_LOGGER.info(sb.toString());
        }
        return result;
    }

    /**
     * post请求上传form表单
     *
     * @param url        - 请求URL
     * @param postParams - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @return
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpPostForm(String url, Map<String, String> postParams) {
        return httpPostForm(url, postParams, DEFAULT_REQUEST_TIMEOUT, DEFAULT_ENCODING);
    }

    /**
     * post请求上传form表单
     *
     * @param url            - 请求URL
     * @param postParams     - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @param requestTimeout - 请求超时时间,毫秒数
     * @return
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpPostForm(String url, Map<String, String> postParams, int requestTimeout) {
        return httpPostForm(url, postParams, requestTimeout, DEFAULT_ENCODING);
    }

    /**
     * post请求上传form表单
     *
     * @param url            - 请求URL
     * @param postParams     - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @param requestTimeout - 请求超时时间,毫秒数
     * @param encoding
     * @return
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpPostForm(String url, Map<String, String> postParams, int requestTimeout, String encoding) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "Param url must be not null and empty");
        Preconditions.checkArgument(postParams != null && !postParams.isEmpty(), "Param postParams must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

        String urlToSend = url;
        String result = null;

        /**
         * 消费者日志
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(urlToSend).append(SymbolConstant.BAR);
        // 参数打印控制1000字符以内
        sb.append(postParams.toString()).append(SymbolConstant.BAR);

        try {
            /**
             * 创建HttpPost实例
             */
            HttpPost httpPost = new HttpPost(urlToSend);
            /**
             * 创建请求参数
             */
            List<NameValuePair> qparams = Lists.newArrayList();
            for (Entry<String, String> entry : postParams.entrySet()) {
                qparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(qparams, encoding);

            httpPost.setEntity(entity);

            if (requestTimeout > 0) {
                /**
                 * 设置request级别参数,覆盖httpClient默认参数
                 */
                httpPost.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, requestTimeout);
            }

            /**
             * 发送请求
             */
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            /**
             * 获取响应
             */
//            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
            // EntityUtils.toString自动关闭输入流
            result = EntityUtils.toString(httpResponse.getEntity(), encoding);
//            } else {
//                EntityUtils.consume(httpResponse.getEntity());
//            }
        } catch (Exception ex) {
            CONSUME_LOGGER.error("HttpClientUtils.httpPostForm error.", ex);
            sb.append("Exception:").append(ex.getMessage()).append(SymbolConstant.BAR);
            throw new HttpCallException("HttpClientUtils.httpPostForm error.", ex);
        } finally {
            // 结果打印控制1000字符以内
            sb.append(result).append(SymbolConstant.BAR);
            sb.append(System.currentTimeMillis() - start);
            CONSUME_LOGGER.info(sb.toString());
        }
        return result;
    }

    /**
     * post请求上传json
     *
     * @param url        - 请求URL
     * @param jsonParams - json参数
     * @return
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpPostJson(String url, String jsonParams) {
        return httpPostJson(url, jsonParams, DEFAULT_REQUEST_TIMEOUT, DEFAULT_ENCODING);
    }

    /**
     * post请求上传json
     *
     * @param url            - 请求URL
     * @param jsonParams     - json参数
     * @param requestTimeout - 请求超时时间,毫秒数
     * @return
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpPostJson(String url, String jsonParams, int requestTimeout) {
        return httpPostJson(url, jsonParams, requestTimeout, DEFAULT_ENCODING);
    }

    /**
     * post请求上传json
     *
     * @param url            - 请求URL
     * @param jsonParams     - 查询参数,如果要保证参数顺序请使用LinkedHashMap
     * @param requestTimeout - 请求超时时间,毫秒数
     * @param encoding
     * @return
     * @throws IllegalArgumentException
     * @throws
     */
    public final String httpPostJson(String url, String jsonParams, int requestTimeout, String encoding) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "Param url must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jsonParams), "Param jsonParams must be not null and empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

        String urlToSend = url;
        String result = null;

        /**
         * 消费者日志
         */
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(urlToSend).append(SymbolConstant.BAR);
        // 参数打印控制1000字符以内
        sb.append(jsonParams).append(SymbolConstant.BAR);

        try {
            /**
             * 创建HttpPost实例
             */
            HttpPost httpPost = new HttpPost(urlToSend);
            /**
             * 创建请求参数
             */
            StringEntity entity = new StringEntity(jsonParams, encoding);
            // entity.setContentType("application/json;charset=" + encoding);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            if (requestTimeout > 0) {
                /**
                 * 设置request级别参数,覆盖httpClient默认参数
                 */
                httpPost.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, requestTimeout);
            }

            /**
             * 发送请求
             */
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            /**
             * 获取响应
             */
//            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
            // EntityUtils.toString自动关闭输入流
            result = EntityUtils.toString(httpResponse.getEntity(), encoding);
//            } else {
//                EntityUtils.consume(httpResponse.getEntity());
//            }
        } catch (Exception ex) {
            CONSUME_LOGGER.error("HttpClientUtils.httpPostJson error.", ex);
            sb.append("Exception:" + ex.getMessage()).append(SymbolConstant.BAR);
            throw new HttpCallException("HttpClientUtils.httpPostJson error.", ex);
        } finally {
            // 返回结果打印控制1000字符以内
            sb.append(result).append(SymbolConstant.BAR);
            sb.append(System.currentTimeMillis() - start);
            CONSUME_LOGGER.info(sb.toString());
        }
        return result;
    }

    /**
     *********************** 默认客户端级别参数,request级别可覆盖<br>
     * 注意：这些变量的set方法中会修改客户端级别参数且是线程安全的,不需要额外同步,因为DefaultHttpClient默认使用的是SyncBasicHttpParams
     */
    /**
     * 协议相关
     *
     * @See org.apache.http.params.CoreProtocolPNames
     */
    private String protocolVersion = "HTTP/1.1";
    private String contentCharset = DEFAULT_ENCODING;
    private String userAgent = "Apache-HttpClient";
    /**
     * 链接相关
     *
     * @See org.apache.http.params.CoreConnectionPNames
     */
    private boolean tcpNodelay = true;
    private int soTimeout = 5000;// 毫秒
    private int soLinger = -1;
    private boolean soReuseaddr = true;
    private int socketBufferSize = 8 * 1024;// 8K
    private int connectionTimeout = 2000;// 毫秒
    private boolean staleConnectionCheck = false;
    private boolean keepAlive = true;
    /**
     * HttpClient实现行为相关
     *
     * @See org.apache.http.params.ClientPNames
     */
    private boolean handleRedirects = true;// 自动处理重定向
    private int maxRedirects = 10;// 最多允许服务端重定向10次
    /**
     * 链接管理相关
     */
    private long connManagerTimeout = 1000;// 链接池中检索链接毫秒
    private int maxPerRoute = 50;// 每个路由连接的最大数量
    private int maxTotal = 200;// 所有路由连接的最大数量

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getContentCharset() {
        return contentCharset;
    }

    public void setContentCharset(String contentCharset) {
        this.contentCharset = contentCharset;
        HttpProtocolParams.setContentCharset(defaultHttpClient.getParams(), this.getContentCharset());
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        HttpProtocolParams.setUserAgent(defaultHttpClient.getParams(), this.getUserAgent());
    }

    public boolean isTcpNodelay() {
        return tcpNodelay;
    }

    public void setTcpNodelay(boolean tcpNodelay) {
        this.tcpNodelay = tcpNodelay;
        HttpConnectionParams.setTcpNoDelay(defaultHttpClient.getParams(), this.isTcpNodelay());
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
        HttpConnectionParams.setSoTimeout(defaultHttpClient.getParams(), soTimeout);
    }

    public int getSoLinger() {
        return soLinger;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
        HttpConnectionParams.setLinger(defaultHttpClient.getParams(), soLinger);
    }

    public boolean isSoReuseaddr() {
        return soReuseaddr;
    }

    public void setSoReuseaddr(boolean soReuseaddr) {
        this.soReuseaddr = soReuseaddr;
        HttpConnectionParams.setSoReuseaddr(defaultHttpClient.getParams(), soReuseaddr);
    }

    public int getSocketBufferSize() {
        return socketBufferSize;
    }

    public void setSocketBufferSize(int socketBufferSize) {
        this.socketBufferSize = socketBufferSize;
        HttpConnectionParams.setSocketBufferSize(defaultHttpClient.getParams(), socketBufferSize);
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        HttpConnectionParams.setConnectionTimeout(defaultHttpClient.getParams(), connectionTimeout);
    }

    public boolean isStaleConnectionCheck() {
        return staleConnectionCheck;
    }

    public void setStaleConnectionCheck(boolean staleConnectionCheck) {
        this.staleConnectionCheck = staleConnectionCheck;
        HttpConnectionParams.setStaleCheckingEnabled(defaultHttpClient.getParams(), staleConnectionCheck);
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        HttpConnectionParams.setSoKeepalive(defaultHttpClient.getParams(), keepAlive);
    }

    public boolean isHandleRedirects() {
        return handleRedirects;
    }

    public void setHandleRedirects(boolean handleRedirects) {
        this.handleRedirects = handleRedirects;
        HttpClientParams.setRedirecting(defaultHttpClient.getParams(), handleRedirects);
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
        defaultHttpClient.getParams().setIntParameter(ClientPNames.MAX_REDIRECTS, maxRedirects);
    }

    public long getConnManagerTimeout() {
        return connManagerTimeout;
    }

    public void setConnManagerTimeout(long connManagerTimeout) {
        this.connManagerTimeout = connManagerTimeout;
        HttpClientParams.setConnectionManagerTimeout(defaultHttpClient.getParams(), connManagerTimeout);

    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        poolingClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        poolingClientConnectionManager.setMaxTotal(maxTotal);
    }

    /**
     * 定期回收连接池中失效链接线程<br>
     * <p/>
     * 虽然连接池有了，但是由于http连接的特殊性（只有在通讯正在进行（block）时才能够对IO事件做出反应）<br>
     * 一旦连接被放回连接池后，我们无从知道该连接是否还是keepalive的，<br>
     * 且此时也无法监控当前socket的状态（即服务器主动关闭了连接，但客户端没有通讯时是不知道当前连接的状态是怎样的）。<br>
     * 怎么办呢？httpClient采用了一个折中的方案来检查连接的“状态”， 就是由客户端自己通过配置去主动关闭其认为是失效的连接。<br>
     * <p/>
     */
    private static class IdleConnectionMonitorThread extends Thread {
        private final Set<ClientConnectionManager> connMgrSet = Sets.newHashSet();
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread() {
            super("HttpClientUtils$IdleConnectionMonitorThread");
        }

        public void addConnMgr(ClientConnectionManager connectionManager) {
            connMgrSet.add(connectionManager);
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        // 如果在3秒内有其他线程持有this锁且不释放,将不会自动唤醒
                        wait(3000L);
                        // 关闭过期连接
                        for (ClientConnectionManager connMgr : connMgrSet) {
                            connMgr.closeExpiredConnections();
                            // 可选地，关闭空闲超过60秒的连接
                            connMgr.closeIdleConnections(DEFAULT_KEEPALIVE_TIME, TimeUnit.MILLISECONDS);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                // 终止
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * 连接保持活动的策略<br>
     * HTTP规范没有确定一个持久连接可能或应该保持活动多长时间。<br>
     * 一些HTTP服务器使用非标准的头部信息Keep-Alive来告诉客户端它们想在服务器端保持连接活动的周期秒数。<br>
     * 如果这个信息可用，HttClient就会利用这个它。如果头部信息Keep-Alive在响应中不存在，HttpClient假设连接无限期的保持活动。<br>
     * 然而许多现实中的HTTP服务器配置了在特定不活动周期之后丢掉持久连接来保存系统资源，往往这是不通知客户端的。<br>
     * 如果默认的策略证明是过于乐观的，那么就会有人想提供一个定制的保持活动策略。 <br>
     * 为了使connMgr.closeExpiredConnections()起到作用需要指定连接keep alive策略告诉httpClient哪些连接大概什么时候会过期,可以关闭<br>
     */
    private static class HttpConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {

        @Override
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            /**
             * 兑现服务器返回的keep-alive头部信息
             */
            long keepAlive = super.getKeepAliveDuration(response, context);
            if (keepAlive == -1) {
                /**
                 * 不同目标主机采取不同的keepalive策略
                 */
                HttpHost target = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
                    keepAlive = 5 * 1000;// 保持活动5秒
                } else {
                    keepAlive = DEFAULT_KEEPALIVE_TIME;// 保持活动60秒
                }
            }

            return keepAlive;
        }
    }

    /**
     * 请求重试的策略<br>
     * 继承默认的重试策略,针对httpClietn4.2.1的bug:<br>
     * DefaultHttpResponseParser.parseHead 95行<br>
     * 偶尔会有NoHttpResponseException("The target server failed to respond")抛出
     */
    private static class HttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {

        public HttpRequestRetryHandler() {
            super();
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            boolean retry = super.retryRequest(exception, executionCount, context);

            if (exception instanceof NoHttpResponseException) {
                CONSUME_LOGGER.info("HttpClientUtils.retryRequest detected NoHttpResponseException,sleep 1 ml,then retry");
                try {
                    Thread.sleep(1L);// 等待1毫秒
                } catch (InterruptedException e) {
                }
                if (executionCount > super.getRetryCount()) {
                    retry = false;
                } else {
                    retry = true;
                }
            }
            return retry;
        }
    }
}
