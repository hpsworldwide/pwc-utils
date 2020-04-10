package com.hpsworldwide.powercard.utils.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a wrapper around org.apache.http.impl.client.CloseableHttpClient that makes
 * some operations even more easy
 *
 * @author (c) HPS Solutions
 */
public class HTTP_Client implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(HTTP_Client.class);
    private final String serverUrl;
    private final String login;
    // help on HttpClient : http://hc.apache.org/httpcomponents-client-ga/quickstart.html
    private final CloseableHttpClient httpClient;

    public HTTP_Client(String serverUrl, int connectTimeout, int connectionRequestTimeout, int socketTimeout, Map.Entry<String, String> credentials) {
        this.serverUrl = serverUrl;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout).build();
        HttpClientBuilder clientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig);
        if (credentials != null) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            this.login = credentials.getKey();
            UsernamePasswordCredentials upCredentials = new UsernamePasswordCredentials(login, credentials.getValue());
            provider.setCredentials(AuthScope.ANY, upCredentials);
            clientBuilder.setDefaultCredentialsProvider(provider);
        } else {
            this.login = null;
        }
        this.httpClient = clientBuilder.build();
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getLogin() {
        return login;
    }

    public String postProcess(InputStream requestContent, ContentType contentType) throws IOException {
        return postProcess(new InputStreamEntity(requestContent, contentType), null);
    }

    public String postProcess(InputStream requestContent, ContentType contentType, Header[] headers) throws IOException {
        return postProcess(new InputStreamEntity(requestContent, contentType), headers);
    }

    public String postProcess(byte[] requestContent, ContentType contentType) throws IOException {
        return postProcess(new ByteArrayEntity(requestContent, contentType), null);
    }

    public String postProcess(byte[] requestContent, ContentType contentType, Header[] headers) throws IOException {
        return postProcess(new ByteArrayEntity(requestContent, contentType), headers);
    }

    public String postProcess(HttpEntity entity, Header[] headers) throws IOException {
        HttpPost httpPost = new HttpPost(serverUrl);
        httpPost.setEntity(entity);
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        // inspired from http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                LOG.debug("response received from server");
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.debug("status code: " + statusCode);
                HttpEntity entity = response.getEntity();
                String entityContent = entity != null ? EntityUtils.toString(entity) : null;
                LOG.debug("result content:[" + entityContent + "]");
                if (statusCode >= 200 && statusCode < 300) {
                    return entityContent;
                } else {
                    throw new ClientProtocolException("unexpected response status: " + statusCode + "; reason: " + response.getStatusLine().getReasonPhrase() + "; responseContent: " + entityContent);
                }
            }
        };
        long start;
        String result;
        LOG.debug("sending request to server[" + serverUrl + "]");
        start = System.currentTimeMillis();
        try {
            result = httpClient.execute(httpPost, responseHandler);
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        LOG.debug("request duration: " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }

    @Override
    public String toString() {
        return "HTTP_Client{" + "serverUrl=" + serverUrl + ", httpClient=" + httpClient + '}';
    }

    @Override
    public void close() throws IOException {
        LOG.info("closing httpClient...");
        httpClient.close();
        LOG.info("httpClient closed.");
    }

}
