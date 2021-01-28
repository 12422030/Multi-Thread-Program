package boot.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class EpccfeHttpServerTest {
	void testPost(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");

            OutputStreamWriter out = new OutputStreamWriter(con
                    .getOutputStream());    
            String xmlInfo = getXmlInfo();
            System.out.println("urlStr=" + urlStr);
            System.out.println("xmlInfo=" + xmlInfo);
            out.write(xmlInfo);
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream()));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getXmlInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<videoSend>");
        sb.append("    <header>");
        sb.append("        <sid>1</sid>");
        sb.append("        <type>service</type>");
        sb.append("    </header>");
        sb.append("    <service>");
        sb.append("        <fromNum>0000021000011001</fromNum>");
        sb.append("           <toNum>33647405</toNum>");
        sb.append("        <videoPath>mnt/5.0.217.50/resources/80009.mov</videoPath>");
        sb.append("        <chargeNumber>0000021000011001</chargeNumber>");
        sb.append("    </service>");
        sb.append("</videoSend>");
        return sb.toString();
    }

    public static void main(String[] args) {
        String url = "http://192.168.20.43:8080/VideoSend";
        new EpccfeHttpServerTest().testPost(url);
    }
}
