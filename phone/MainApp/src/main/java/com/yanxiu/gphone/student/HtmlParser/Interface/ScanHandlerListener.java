package com.yanxiu.gphone.student.HtmlParser.Interface;

import org.xml.sax.SAXException;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface ScanHandlerListener {
    void adup(char[] buff, int offset, int length) throws SAXException;
    void aname(char[] buff, int offset, int length) throws SAXException;
    void aval(char[] buff, int offset, int length) throws SAXException;
    void cdsect(char[] buff, int offset, int length) throws SAXException;
    void decl(char[] buff, int offset, int length) throws SAXException;
    void entity(char[] buff, int offset, int length) throws SAXException;
    void eof(char[] buff, int offset, int length) throws SAXException;
    void etag(char[] buff, int offset, int length) throws SAXException;
    void gi(char[] buff, int offset, int length) throws SAXException;
    void pcdata(char[] buff, int offset, int length) throws SAXException;
    void pi(char[] buff, int offset, int length) throws SAXException;
    void pitarget(char[] buff, int offset, int length) throws SAXException;
    void stagc(char[] buff, int offset, int length) throws SAXException;
    void stage(char[] buff, int offset, int length) throws SAXException;
    void cmnt(char[] buff, int offset, int length) throws SAXException;
    int getEntity();
}
