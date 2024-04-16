package com.example.demo.spider;

/**
 * @ClassName Spider.java
 * @author wuting
 * @Description TODO
 * @createTime 2024年04月15日 11:55:00
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

public class Spider {

    private static String value = "https://npm.onmicrosoft.cn/browse/pinia@2.0.22/,\n" + "https://npm.onmicrosoft.cn/browse/vue-demi@0.13.11/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/tiny-engine-builtin-component@1.0.1/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/tiny-engine-i18n-host@1.0.3/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/tiny-engine-webcomponent-core@1.0.4/,\n" + "https://npm.onmicrosoft.cn/browse/axios-mock-adapter@1.21.1/,\n" + "https://npm.onmicrosoft.cn/browse/axios@1.0.0-alpha.1/,\n" + "https://npm.onmicrosoft.cn/browse/@vueuse/shared@9.6.0/,\n" + "https://npm.onmicrosoft.cn/browse/@vueuse/core@9.6.0/,\n" + "https://npm.onmicrosoft.cn/browse/@vue/devtools-api@6.5.1/,\n" + "https://npm.onmicrosoft.cn/browse/vue-router@4.0.16/,\n" + "https://npm.onmicrosoft.cn/browse/vue-i18n@9.2.0-beta.36/,\n" + "https://npm.onmicrosoft.cn/browse/@vue/server-renderer@3.2.36/,\n" + "https://npm.onmicrosoft.cn/browse/vue@3.2.36/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue-renderless@3.11.8/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue-theme-mobile@3.11.1/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue-theme@3.11.2/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue@3.11.2/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue-design-smb@3.11.0/,\n" + "https://npm.onmicrosoft.cn/browse/vue@3.4.21/,\n" + "https://npm.onmicrosoft.cn/browse/prettier@2.7.1/";

    private static String value1 = "https://npm.onmicrosoft.cn/browse/@opentiny/vue-theme-mobile@3.11.1/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue-theme@3.11.2/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue@3.11.2/,\n" + "https://npm.onmicrosoft.cn/browse/@opentiny/vue-design-smb@3.11.0/,\n" + "https://npm.onmicrosoft.cn/browse/vue@3.4.21/,\n" + "https://npm.onmicrosoft.cn/browse/prettier@2.7.1/";

    private static String value2 = "https://npm.onmicrosoft.cn/browse/@opentiny/vue-theme@3.11.2/";

    public static void main(String[] args) throws IOException {
        String filepath = "d:/browse/";
        String baseUrl = "https://npm.onmicrosoft.cn/";
        String[] strS = value2.split(",");
        try {
            for (String url : strS) {
                JsonNode rootNode = executeFun(url);
                if (rootNode == null) {
                    return;
                }
                executeSpider(rootNode, filepath, baseUrl, url, true);
            }
            System.out.println("全部执行完成!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonNode executeFun(String url) throws Exception {
        trustEveryone();
        Connection connection = Jsoup.connect(url);
        Document doc = connection.timeout(60 * 1000).get();
        Element script = doc.select("script").stream().filter(e -> e.data().contains("window.__DATA__")).findFirst().orElse(null);
        if (script != null) {
            String data = script.data().replaceAll("window.__DATA__ = ", "");
            // 去掉末尾的分号
            System.out.println("Data extracted: " + data);
            // 使用Jackson解析JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(data);
            // 打印结果
            System.out.println(rootNode.toString());
            return rootNode;
        }
        return null;
    }

    public static void executeSpider(JsonNode rootNode, String filepath, String baseUrl, String url, boolean flag) throws Exception {
        if (flag) {
            String packageName = String.valueOf(rootNode.get("packageName")).replaceAll("\"", "");
            String packageVersion = String.valueOf(rootNode.get("packageVersion")).replaceAll("\"", "");
            String version = packageName + "@" + packageVersion;
            filepath = filepath + version;
            baseUrl = baseUrl + version;
        }
        JsonNode targetObjectNode = rootNode.get("target");
        Iterator<JsonNode> jsonNodeIterator = targetObjectNode.get("details").elements();
        for (Iterator<JsonNode> it = jsonNodeIterator; it.hasNext(); ) {
            JsonNode value = it.next();
            String type = String.valueOf(value.get("type")).replaceAll("\"", "");
            String fileName = String.valueOf(value.get("path")).replaceAll("\"", "");
            if (type.equals("file")) {
                urlOperation(baseUrl + fileName, filepath, fileName);
            } else {
                JsonNode rootNodeFun = executeFun(url.substring(0, url.lastIndexOf("/")) + fileName + "/");
                //type -> {TextNode@2622} ""directory""
                executeSpider(rootNodeFun, filepath, baseUrl, url, false);
            }
        }
    }

    public static void urlOperation(String urlValue, String filepath, String filename) throws Exception {
        URL url = null;
        try {
            url = new URL(urlValue);
        } catch (MalformedURLException e) {
            System.out.println("当前urlValue:" + urlValue);
            System.out.println("当前filepath:" + filepath);
            System.out.println("当前filename:" + filename);
            e.printStackTrace();
        }
        String charset = "utf-8";
        int sec_cont = 1000;
        URLConnection url_con = url.openConnection();
        url_con.setDoOutput(true);
        url_con.setReadTimeout(60 * sec_cont);
        url_con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        InputStream htm_in = url_con.getInputStream();
        String htm_str = InputStream2String(htm_in, charset);
        saveHtml(filename, filepath, htm_str);
    }

    /**
     * Method: saveHtml
     * Description: save String to file
     *
     * @param filepath file path which need to be saved
     * @param str      string saved
     */
    public static void saveHtml(String filename, String filepath, String str) {
        try {
            if (countOccurrences(filename, "/")) {
                filepath = filepath + filename.substring(0, filename.lastIndexOf("/"));
                filename = filename.substring(filename.lastIndexOf("/"), filename.length());
            }
            File file = new File(filepath);
            file.mkdirs();
            file = new File(filepath, filename);
            file.createNewFile();
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath + filename, true), "utf-8");
            outs.write(str);
            System.out.println(filepath + filename + "--------》已完成");
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }

    public static boolean countOccurrences(String str, String target) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(target, index)) != -1) {
            count++;
            index += target.length();
        }
        return count > 1;
    }

    /**
     * Method: InputStream2String
     * Description: make InputStream to String
     *
     * @param in_st   inputstream which need to be converted
     * @param charset encoder of value
     * @throws IOException if an error occurred
     */
    public static String InputStream2String(InputStream in_st, String charset) throws IOException {
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while ((line = buff.readLine()) != null) {
            res.append(line);
        }
        return res.toString();
    }

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

