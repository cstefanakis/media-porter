package org.sda.mediaporter.Servicies.Impl;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.sda.mediaporter.Servicies.FileDownloaderService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDownloaderServiceImpl implements FileDownloaderService {

    @Override
    public List<String> getFiles(String name) {
        List<String> files = new ArrayList<String>();
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            HtmlPage page = webClient.getPage("https://fastshare.cloud/"+name+"/s");

            // Wait for JS to load
            webClient.waitForBackgroundJavaScript(3000);

            List<HtmlListItem> listItems = page.getByXPath("//li");

            for (HtmlListItem li : listItems) {
                files.add(li.asXml());
            }
            return files;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
