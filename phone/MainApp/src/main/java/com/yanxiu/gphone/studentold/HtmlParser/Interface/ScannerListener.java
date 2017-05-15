package com.yanxiu.gphone.studentold.HtmlParser.Interface;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface ScannerListener {
    void scan(Reader r, ScanHandlerListener h) throws IOException, SAXException;
    void resetDocumentLocator(String publicid, String systemid);
    void startCDATA();
}
