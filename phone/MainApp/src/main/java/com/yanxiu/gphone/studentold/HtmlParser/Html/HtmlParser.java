package com.yanxiu.gphone.studentold.HtmlParser.Html;

import com.yanxiu.gphone.studentold.HtmlParser.Interface.AutoDetectorListener;
import com.yanxiu.gphone.studentold.HtmlParser.Interface.ScanHandlerListener;
import com.yanxiu.gphone.studentold.HtmlParser.Interface.ScannerListener;
import com.yanxiu.gphone.studentold.HtmlParser.Moudle.ElementBean;
import com.yanxiu.gphone.studentold.HtmlParser.Moudle.ElementTypeBean;
import com.yanxiu.gphone.studentold.HtmlParser.Utils.Schema;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/2.
 */
public class HtmlParser extends DefaultHandler implements ScanHandlerListener, XMLReader, LexicalHandler {

    private ContentHandler theContentHandler = this;
    private LexicalHandler theLexicalHandler = this;
    private DTDHandler theDTDHandler = this;
    private ErrorHandler theErrorHandler = this;
    private EntityResolver theEntityResolver = this;
    private Schema theSchema;
    private ScannerListener theScannerListener;
    private AutoDetectorListener theAutoDetector;

    private ElementBean theNewElementBean = null;
    private String theAttributeName = null;
    private boolean theDoctypeIsPresent = false;
    private String theDoctypePublicId = null;
    private String theDoctypeSystemId = null;
    private String theDoctypeName = null;
    private String thePITarget = null;
    private ElementBean theStack = null;
    private ElementBean theSaved = null;
    private ElementBean thePCDATA = null;
    private int theEntity = 0;

    private static char[] etagchars = {'<', '/', '>'};
    private boolean virginStack = true;
    private char[] theCommentBuffer = new char[2000];

    private static boolean DEFAULT_NAMESPACES = true;
    private static boolean DEFAULT_IGNORE_BOGONS = false;
    private static boolean DEFAULT_BOGONS_EMPTY = false;
    private static boolean DEFAULT_ROOT_BOGONS = true;
    private static boolean DEFAULT_DEFAULT_ATTRIBUTES = true;
    private static boolean DEFAULT_TRANSLATE_COLONS = false;
    private static boolean DEFAULT_RESTART_ELEMENTS = true;
    private static boolean DEFAULT_IGNORABLE_WHITESPACE = false;
    private static boolean DEFAULT_CDATA_ELEMENTS = true;

    private boolean namespaces = DEFAULT_NAMESPACES;
    private boolean ignoreBogons = DEFAULT_IGNORE_BOGONS;
    private boolean bogonsEmpty = DEFAULT_BOGONS_EMPTY;
    private boolean rootBogons = DEFAULT_ROOT_BOGONS;
    private boolean defaultAttributes = DEFAULT_DEFAULT_ATTRIBUTES;
    private boolean translateColons = DEFAULT_TRANSLATE_COLONS;
    private boolean restartElements = DEFAULT_RESTART_ELEMENTS;
    private boolean ignorableWhitespace = DEFAULT_IGNORABLE_WHITESPACE;
    private boolean CDATAElements = DEFAULT_CDATA_ELEMENTS;

    private static String legal ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-'()+,./:=?;!*#@$_%";

    public final static String namespacesFeature ="http://xml.org/sax/features/namespaces";
    public final static String namespacePrefixesFeature ="http://xml.org/sax/features/namespace-prefixes";
    public final static String externalGeneralEntitiesFeature ="http://xml.org/sax/features/external-general-entities";
    public final static String externalParameterEntitiesFeature ="http://xml.org/sax/features/external-parameter-entities";
    public final static String isStandaloneFeature ="http://xml.org/sax/features/is-standalone";
    public final static String lexicalHandlerParameterEntitiesFeature ="http://xml.org/sax/features/lexical-handler/parameter-entities";
    public final static String resolveDTDURIsFeature ="http://xml.org/sax/features/resolve-dtd-uris";
    public final static String stringInterningFeature ="http://xml.org/sax/features/string-interning";
    public final static String useAttributes2Feature ="http://xml.org/sax/features/use-attributes2";
    public final static String useLocator2Feature ="http://xml.org/sax/features/use-locator2";
    public final static String useEntityResolver2Feature ="http://xml.org/sax/features/use-entity-resolver2";
    public final static String validationFeature ="http://xml.org/sax/features/validation";
    public final static String unicodeNormalizationCheckingFeature ="http://xml.org/sax/features/unicode-normalization-checking";
    public final static String xmlnsURIsFeature ="http://xml.org/sax/features/xmlns-uris";
    public final static String XML11Feature ="http://xml.org/sax/features/xml-1.1";
    public final static String ignoreBogonsFeature ="http://www.ccil.org/~cowan/tagsoup/features/ignore-bogons";
    public final static String bogonsEmptyFeature ="http://www.ccil.org/~cowan/tagsoup/features/bogons-empty";
    public final static String rootBogonsFeature ="http://www.ccil.org/~cowan/tagsoup/features/root-bogons";
    public final static String defaultAttributesFeature ="http://www.ccil.org/~cowan/tagsoup/features/default-attributes";
    public final static String translateColonsFeature ="http://www.ccil.org/~cowan/tagsoup/features/translate-colons";
    public final static String restartElementsFeature ="http://www.ccil.org/~cowan/tagsoup/features/restart-elements";
    public final static String ignorableWhitespaceFeature ="http://www.ccil.org/~cowan/tagsoup/features/ignorable-whitespace";
    public final static String CDATAElementsFeature ="http://www.ccil.org/~cowan/tagsoup/features/cdata-elements";
    public final static String lexicalHandlerProperty ="http://xml.org/sax/properties/lexical-handler";
    public final static String scannerProperty ="http://www.ccil.org/~cowan/tagsoup/properties/scanner";
    public final static String schemaProperty ="http://www.ccil.org/~cowan/tagsoup/properties/schema";
    public final static String autoDetectorProperty ="http://www.ccil.org/~cowan/tagsoup/properties/auto-detector";
    private HashMap theFeatures = new HashMap();

    {
        theFeatures.put(namespacesFeature, truthValue(DEFAULT_NAMESPACES));
        theFeatures.put(namespacePrefixesFeature, Boolean.FALSE);
        theFeatures.put(externalGeneralEntitiesFeature, Boolean.FALSE);
        theFeatures.put(externalParameterEntitiesFeature, Boolean.FALSE);
        theFeatures.put(isStandaloneFeature, Boolean.FALSE);
        theFeatures.put(lexicalHandlerParameterEntitiesFeature, Boolean.FALSE);
        theFeatures.put(resolveDTDURIsFeature, Boolean.TRUE);
        theFeatures.put(stringInterningFeature, Boolean.TRUE);
        theFeatures.put(useAttributes2Feature, Boolean.FALSE);
        theFeatures.put(useLocator2Feature, Boolean.FALSE);
        theFeatures.put(useEntityResolver2Feature, Boolean.FALSE);
        theFeatures.put(validationFeature, Boolean.FALSE);
        theFeatures.put(xmlnsURIsFeature, Boolean.FALSE);
        theFeatures.put(xmlnsURIsFeature, Boolean.FALSE);
        theFeatures.put(XML11Feature, Boolean.FALSE);
        theFeatures.put(ignoreBogonsFeature, truthValue(DEFAULT_IGNORE_BOGONS));
        theFeatures.put(bogonsEmptyFeature, truthValue(DEFAULT_BOGONS_EMPTY));
        theFeatures.put(rootBogonsFeature, truthValue(DEFAULT_ROOT_BOGONS));
        theFeatures.put(defaultAttributesFeature, truthValue(DEFAULT_DEFAULT_ATTRIBUTES));
        theFeatures.put(translateColonsFeature, truthValue(DEFAULT_TRANSLATE_COLONS));
        theFeatures.put(restartElementsFeature, truthValue(DEFAULT_RESTART_ELEMENTS));
        theFeatures.put(ignorableWhitespaceFeature, truthValue(DEFAULT_IGNORABLE_WHITESPACE));
        theFeatures.put(CDATAElementsFeature, truthValue(DEFAULT_CDATA_ELEMENTS));
    }
    private static Boolean truthValue(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean getFeature(String name)throws SAXNotRecognizedException, SAXNotSupportedException {
        Boolean b = (Boolean) theFeatures.get(name);
        if (b == null) {
            throw new SAXNotRecognizedException("Unknown feature " + name);
        }
        return b.booleanValue();
    }

    public void setFeature(String name, boolean value)throws SAXNotRecognizedException, SAXNotSupportedException {
        Boolean b = (Boolean) theFeatures.get(name);
        if (b == null) {
            throw new SAXNotRecognizedException("Unknown feature " + name);
        }
        if (value) theFeatures.put(name, Boolean.TRUE);
        else theFeatures.put(name, Boolean.FALSE);

        if (name.equals(namespacesFeature)) {
            namespaces = value;
        }else if (name.equals(ignoreBogonsFeature)) {
            ignoreBogons = value;
        }else if (name.equals(bogonsEmptyFeature)) {
            bogonsEmpty = value;
        }else if (name.equals(rootBogonsFeature)) {
            rootBogons = value;
        }else if (name.equals(defaultAttributesFeature)) {
            defaultAttributes = value;
        }else if (name.equals(translateColonsFeature)) {
            translateColons = value;
        }else if (name.equals(restartElementsFeature)) {
            restartElements = value;
        }else if (name.equals(ignorableWhitespaceFeature)) {
            ignorableWhitespace = value;
        }else if (name.equals(CDATAElementsFeature)) {
            CDATAElements = value;
        }
    }

    public Object getProperty(String name)throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals(lexicalHandlerProperty)) {
            return theLexicalHandler == this ? null : theLexicalHandler;
        } else if (name.equals(scannerProperty)) {
            return theScannerListener;
        } else if (name.equals(schemaProperty)) {
            return theSchema;
        } else if (name.equals(autoDetectorProperty)) {
            return theAutoDetector;
        } else {
            throw new SAXNotRecognizedException("Unknown property " + name);
        }
    }

    public void setProperty(String name, Object value)throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals(lexicalHandlerProperty)) {
            if (value == null) {
                theLexicalHandler = this;
            } else if (value instanceof LexicalHandler) {
                theLexicalHandler = (LexicalHandler) value;
            } else {
                throw new SAXNotSupportedException("Your lexical handler is not a LexicalHandler");
            }
        } else if (name.equals(scannerProperty)) {
            if (value instanceof ScannerListener) {
                theScannerListener = (ScannerListener) value;
            } else {
                throw new SAXNotSupportedException("Your scanner is not a ScannerListener");
            }
        } else if (name.equals(schemaProperty)) {
            if (value instanceof Schema) {
                theSchema = (Schema) value;
            } else {
                throw new SAXNotSupportedException("Your schema is not a Schema");
            }
        } else if (name.equals(autoDetectorProperty)) {
            if (value instanceof AutoDetectorListener) {
                theAutoDetector = (AutoDetectorListener) value;
            } else {
                throw new SAXNotSupportedException("Your auto-detector is not an AutoDetectorListener");
            }
        } else {
            throw new SAXNotRecognizedException("Unknown property " + name);
        }
    }

    public void setEntityResolver(EntityResolver resolver) {
        theEntityResolver = (resolver == null) ? this : resolver;
    }

    public EntityResolver getEntityResolver() {
        return (theEntityResolver == this) ? null : theEntityResolver;
    }

    public void setDTDHandler(DTDHandler handler) {
        theDTDHandler = (handler == null) ? this : handler;
    }

    public DTDHandler getDTDHandler() {
        return (theDTDHandler == this) ? null : theDTDHandler;
    }

    public void setContentHandler(ContentHandler handler) {
        theContentHandler = (handler == null) ? this : handler;
    }

    public ContentHandler getContentHandler() {
        return (theContentHandler == this) ? null : theContentHandler;
    }

    public void setErrorHandler(ErrorHandler handler) {
        theErrorHandler = (handler == null) ? this : handler;
    }

    public ErrorHandler getErrorHandler() {
        return (theErrorHandler == this) ? null : theErrorHandler;
    }

    public void parse(InputSource input) throws IOException, SAXException {
        setup();
        Reader r = getReader(input);
        theContentHandler.startDocument();
        theScannerListener.resetDocumentLocator(input.getPublicId(), input.getSystemId());
        if (theScannerListener instanceof Locator) {
            theContentHandler.setDocumentLocator((Locator) theScannerListener);
        }
        if (!(theSchema.getURI().equals(""))) {
            theContentHandler.startPrefixMapping(theSchema.getPrefix(), theSchema.getURI());
        }
        theScannerListener.scan(r, this);
    }

    public void parse(String systemid) throws IOException, SAXException {
        parse(new InputSource(systemid));
    }

    private void setup() {
        if (theSchema == null) theSchema = new HtmlSchema();
        if (theScannerListener == null) theScannerListener = new HtmlScannerListener();
        if (theAutoDetector == null) {
            theAutoDetector = new AutoDetectorListener() {
                public Reader autoDetectingReader(InputStream i) {
                    return new InputStreamReader(i);
                }
            };
        }
        theStack = new ElementBean(theSchema.getElementType("<root>"), defaultAttributes);
        thePCDATA = new ElementBean(theSchema.getElementType("<pcdata>"), defaultAttributes);
        theNewElementBean = null;
        theAttributeName = null;
        thePITarget = null;
        theSaved = null;
        theEntity = 0;
        virginStack = true;
        theDoctypeName = theDoctypePublicId = theDoctypeSystemId = null;
    }

    private Reader getReader(InputSource s) throws SAXException, IOException {
        Reader r = s.getCharacterStream();
        InputStream i = s.getByteStream();
        String encoding = s.getEncoding();
        String publicid = s.getPublicId();
        String systemid = s.getSystemId();
        if (r == null) {
            if (i == null) i = getInputStream(publicid, systemid);
            if (encoding == null) {
                r = theAutoDetector.autoDetectingReader(i);
            } else {
                try {
                    r = new InputStreamReader(i, encoding);
                } catch (UnsupportedEncodingException e) {
                    r = new InputStreamReader(i);
                }
            }
        }
        return r;
    }

    private InputStream getInputStream(String publicid, String systemid) throws IOException, SAXException {
        URL basis = new URL("file", "", System.getProperty("user.dir") + "/.");
        URL url = new URL(basis, systemid);
        URLConnection c = url.openConnection();
        return c.getInputStream();
    }

    public void adup(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean == null || theAttributeName == null) return;
        theNewElementBean.setAttribute(theAttributeName, null, theAttributeName);
        theAttributeName = null;
    }

    public void aname(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean != null) {
            theAttributeName = makeName(buff, offset, length).toLowerCase();
        }
    }

    public void aval(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean == null || theAttributeName == null) {
            return;
        }else {
            String value = new String(buff, offset, length);
            value = expandEntities(value);
            theNewElementBean.setAttribute(theAttributeName, null, value);
            theAttributeName = null;
        }
    }

    private String expandEntities(String src) {
        int refStart = -1;
        int len = src.length();
        char[] dst = new char[len];
        int dstlen = 0;
        for (int i = 0; i < len; i++) {
            char ch = src.charAt(i);
            dst[dstlen++] = ch;
            if (ch == '&' && refStart == -1) {
                refStart = dstlen;
            } else if (refStart == -1) {

            } else if (Character.isLetter(ch) || Character.isDigit(ch) ||ch == '#') {

            } else if (ch == ';') {
                int ent = lookupEntity(dst, refStart, dstlen - refStart - 1);
                if (ent > 0xFFFF) {
                    ent -= 0x10000;
                    dst[refStart - 1] = (char) ((ent >> 10) + 0xD800);
                    dst[refStart] = (char) ((ent & 0x3FF) + 0xDC00);
                    dstlen = refStart + 1;
                } else if (ent != 0) {
                    dst[refStart - 1] = (char) ent;
                    dstlen = refStart;
                }
                refStart = -1;
            } else {
                refStart = -1;
            }
        }
        return new String(dst, 0, dstlen);
    }

    public void entity(char[] buff, int offset, int length) throws SAXException {
        theEntity = lookupEntity(buff, offset, length);
    }

    private int lookupEntity(char[] buff, int offset, int length) {
        int result = 0;
        if (length < 1) return result;
        if (buff[offset] == '#') {
            if (length > 1 && (buff[offset + 1] == 'x'|| buff[offset + 1] == 'X')) {
                try {
                    return Integer.parseInt(new String(buff, offset + 2, length - 2), 16);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
            try {
                return Integer.parseInt(new String(buff, offset + 1, length - 1), 10);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return theSchema.getEntity(new String(buff, offset, length));
    }

    public void eof(char[] buff, int offset, int length) throws SAXException {
        if (virginStack) {
            rectify(thePCDATA);
        }
        while (theStack.next() != null) {
            pop();
        }
        if (!(theSchema.getURI().equals(""))) {
            theContentHandler.endPrefixMapping(theSchema.getPrefix());
        }
        theContentHandler.endDocument();
    }

    public void etag(char[] buff, int offset, int length) throws SAXException {
        if (etag_cdata(buff, offset, length)) {
            return;
        }else {
            etag_basic(buff, offset, length);
        }
    }

    public boolean etag_cdata(char[] buff, int offset, int length) throws SAXException {
        String currentName = theStack.name();
        if (CDATAElements && (theStack.flags() & Schema.F_CDATA) != 0) {
            boolean realTag = (length == currentName.length());
            if (realTag) {
                for (int i = 0; i < length; i++) {
                    if (Character.toLowerCase(buff[offset + i]) != Character.toLowerCase(currentName.charAt(i))) {
                        realTag = false;
                        break;
                    }
                }
            }
            if (!realTag) {
                theContentHandler.characters(etagchars, 0, 2);
                theContentHandler.characters(buff, offset, length);
                theContentHandler.characters(etagchars, 2, 1);
                theScannerListener.startCDATA();
                return true;
            }
        }
        return false;
    }

    public void etag_basic(char[] buff, int offset, int length) throws SAXException {
        theNewElementBean = null;
        String name;
        if (length != 0) {
            name = makeName(buff, offset, length);
            ElementTypeBean type = theSchema.getElementType(name);
            if (type == null) {
                return;
            }
            name = type.name();
        } else {
            name = theStack.name();
        }

        ElementBean sp;
        boolean inNoforce = false;
        for (sp = theStack; sp != null; sp = sp.next()) {
            if (sp.name().equals(name)) {
                break;
            }
            if ((sp.flags() & Schema.F_NOFORCE) != 0) {
                inNoforce = true;
            }
        }

        if (sp == null) {
            return;
        }
        if (sp.next() == null || sp.next().next() == null) {
            return;
        }
        if (inNoforce) {
            sp.preclose();
        } else {
            while (theStack != sp) {
                restartablyPop();
            }
            pop();
        }
        while (theStack.isPreclosed()) {
            pop();
        }
        restart(null);
    }

    private void restart(ElementBean e) throws SAXException {
        while (theSaved != null && theStack.canContain(theSaved) &&(e == null || theSaved.canContain(e))) {
            ElementBean next = theSaved.next();
            push(theSaved);
            theSaved = next;
        }
    }

    private void pop() throws SAXException {
        if (theStack == null) {
            return;
        }
        String name = theStack.name();
        String localName = theStack.localName();
        String namespace = theStack.namespace();
        String prefix = prefixOf(name);

        if (!namespaces) {
            namespace = localName = "";
        }
        theContentHandler.endElement(namespace, localName, name);
        if (foreign(prefix, namespace)) {
            theContentHandler.endPrefixMapping(prefix);
        }
        Attributes atts = theStack.atts();
        for (int i = atts.getLength() - 1; i >= 0; i--) {
            String attNamespace = atts.getURI(i);
            String attPrefix = prefixOf(atts.getQName(i));
            if (foreign(attPrefix, attNamespace)) {
                theContentHandler.endPrefixMapping(attPrefix);
            }
        }
        theStack = theStack.next();
    }

    private void restartablyPop() throws SAXException {
        ElementBean popped = theStack;
        pop();
        if (restartElements && (popped.flags() & Schema.F_RESTART) != 0) {
            popped.anonymize();
            popped.setNext(theSaved);
            theSaved = popped;
        }
    }

    private void push(ElementBean e) throws SAXException {
        String name = e.name();
        String localName = e.localName();
        String namespace = e.namespace();
        String prefix = prefixOf(name);

        e.clean();
        if (!namespaces) {
            namespace = localName = "";
        }
        if (virginStack && localName.equalsIgnoreCase(theDoctypeName)) {
            try {
                theEntityResolver.resolveEntity(theDoctypePublicId, theDoctypeSystemId);
            } catch (IOException ew) {
                ew.printStackTrace();
            }
        }
        if (foreign(prefix, namespace)) {
            theContentHandler.startPrefixMapping(prefix, namespace);
        }
        Attributes atts = e.atts();
        int len = atts.getLength();
        for (int i = 0; i < len; i++) {
            String attNamespace = atts.getURI(i);
            String attPrefix = prefixOf(atts.getQName(i));
            if (foreign(attPrefix, attNamespace)) {
                theContentHandler.startPrefixMapping(attPrefix, attNamespace);
            }
        }
        theContentHandler.startElement(namespace, localName, name, e.atts());
        e.setNext(theStack);
        theStack = e;
        virginStack = false;
        if (CDATAElements && (theStack.flags() & Schema.F_CDATA) != 0) {
            theScannerListener.startCDATA();
        }
    }

    private String prefixOf(String name) {
        int i = name.indexOf(':');
        String prefix = "";
        if (i != -1) {
            prefix = name.substring(0, i);
        }
        return prefix;
    }

    private boolean foreign(String prefix, String namespace) {
        boolean foreign = !(prefix.equals("") || namespace.equals("") ||namespace.equals(theSchema.getURI()));
        return foreign;
    }

    public void decl(char[] buff, int offset, int length) throws SAXException {
        String s = new String(buff, offset, length);
        String name = null;
        String systemid = null;
        String publicid = null;
        String[] v = split(s);
        if (v.length > 0 && "DOCTYPE".equalsIgnoreCase(v[0])) {
            if (theDoctypeIsPresent) return;
            theDoctypeIsPresent = true;
            if (v.length > 1) {
                name = v[1];
                if (v.length > 3 && "SYSTEM".equals(v[2])) {
                    systemid = v[3];
                } else if (v.length > 3 && "PUBLIC".equals(v[2])) {
                    publicid = v[3];
                    if (v.length > 4) {
                        systemid = v[4];
                    } else {
                        systemid = "";
                    }
                }
            }
        }
        publicid = trimquotes(publicid);
        systemid = trimquotes(systemid);
        if (name != null) {
            publicid = cleanPublicid(publicid);
            theLexicalHandler.startDTD(name, publicid, systemid);
            theLexicalHandler.endDTD();
            theDoctypeName = name;
            theDoctypePublicId = publicid;
            if (theScannerListener instanceof Locator) {
                theDoctypeSystemId = ((Locator) theScannerListener).getSystemId();
                try {
                    theDoctypeSystemId = new URL(new URL(theDoctypeSystemId), systemid).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String trimquotes(String in) {
        if (in == null) return in;
        int length = in.length();
        if (length == 0) return in;
        char s = in.charAt(0);
        char e = in.charAt(length - 1);
        if (s == e && (s == '\'' || s == '"')) {
            in = in.substring(1, in.length() - 1);
        }
        return in;
    }

    private static String[] split(String val) throws IllegalArgumentException {
        val = val.trim();
        if (val.length() == 0) {
            return new String[0];
        } else {
            ArrayList l = new ArrayList();
            int s = 0;
            int e = 0;
            boolean sq = false;
            boolean dq = false;
            char lastc = 0;
            int len = val.length();
            for (e = 0; e < len; e++) {
                char c = val.charAt(e);
                if (!dq && c == '\'' && lastc != '\\') {
                    sq = !sq;
                    if (s < 0) {
                        s = e;
                    }
                } else if (!sq && c == '\"' && lastc != '\\') {
                    dq = !dq;
                    if (s < 0) {
                        s = e;
                    }
                } else if (!sq && !dq) {
                    if (Character.isWhitespace(c)) {
                        if (s >= 0) {
                            l.add(val.substring(s, e));
                        }
                        s = -1;
                    } else if (s < 0 && c != ' ') {
                        s = e;
                    }
                }
                lastc = c;
            }
            l.add(val.substring(s, e));
            return (String[]) l.toArray(new String[0]);
        }
    }

    private String cleanPublicid(String src) {
        if (src == null) {
            return null;
        }else {
            int len = src.length();
            StringBuffer dst = new StringBuffer(len);
            boolean suppressSpace = true;
            for (int i = 0; i < len; i++) {
                char ch = src.charAt(i);
                if (legal.indexOf(ch) != -1) {
                    dst.append(ch);
                    suppressSpace = false;
                } else if (suppressSpace) {
                    ;
                } else {
                    dst.append(' ');
                    suppressSpace = true;
                }
            }
            return dst.toString().trim();
        }
    }

    public void gi(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean == null) {
            String name = makeName(buff, offset, length);
            if (name != null) {
                ElementTypeBean type = theSchema.getElementType(name);
                if (type == null) {
                    if (ignoreBogons) {
                        return;
                    }else {
                        int bogonModel = bogonsEmpty ? Schema.M_EMPTY : Schema.M_ANY;
                        int bogonMemberOf = rootBogons ? Schema.M_ANY : (Schema.M_ANY & ~Schema.M_ROOT);
                        theSchema.elementType(name, bogonModel, bogonMemberOf, 0);
                        if (!rootBogons) {
                            theSchema.parent(name, theSchema.rootElementType().name());
                        }
                        type = theSchema.getElementType(name);
                    }
                }
                theNewElementBean = new ElementBean(type, defaultAttributes);
            }
        }
    }

    public void cdsect(char[] buff, int offset, int length) throws SAXException {
        theLexicalHandler.startCDATA();
        pcdata(buff, offset, length);
        theLexicalHandler.endCDATA();
    }

    public void pcdata(char[] buff, int offset, int length) throws SAXException {
        if (length != 0) {
            boolean allWhite = true;
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(buff[offset + i])) {
                    allWhite = false;
                }
            }
            if (allWhite && !theStack.canContain(thePCDATA)) {
                if (ignorableWhitespace) {
                    theContentHandler.ignorableWhitespace(buff, offset, length);
                }
            } else {
                rectify(thePCDATA);
                theContentHandler.characters(buff, offset, length);
            }
        }
    }

    public void pitarget(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean == null) {
            thePITarget = makeName(buff, offset, length).replace(':', '_');
        }
    }

    public void pi(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean != null || thePITarget == null) {
            return;
        }else {
            if (!"xml".equalsIgnoreCase(thePITarget)) {
                if (length > 0 && buff[length - 1] == '?') {
                    length--;
                }
                theContentHandler.processingInstruction(thePITarget, new String(buff, offset, length));
                thePITarget = null;
            }
        }
    }

    public void stagc(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean != null) {
            rectify(theNewElementBean);
            if (theStack.model() == Schema.M_EMPTY) {
                etag_basic(buff, offset, length);
            }
        }
    }

    public void stage(char[] buff, int offset, int length) throws SAXException {
        if (theNewElementBean != null) {
            rectify(theNewElementBean);
            etag_basic(buff, offset, length);
        }
    }

    public void cmnt(char[] buff, int offset, int length) throws SAXException {
        theLexicalHandler.comment(buff, offset, length);
    }

    private void rectify(ElementBean e) throws SAXException {
        ElementBean sp;
        while (true) {
            for (sp = theStack; sp != null; sp = sp.next()) {
                if (sp.canContain(e)) {
                    break;
                }
            }
            if (sp != null) {
                break;
            }
            ElementTypeBean parentType = e.parent();
            if (parentType == null) {
                break;
            }
            ElementBean parent = new ElementBean(parentType, defaultAttributes);
            parent.setNext(e);
            e = parent;
        }
        if (sp == null) {
            return;
        }
        while (theStack != sp) {
            if (theStack == null || theStack.next() == null ||theStack.next().next() == null) {
                break;
            }
            restartablyPop();
        }
        while (e != null) {
            ElementBean nexte = e.next();
            if (!e.name().equals("<pcdata>")) {
                push(e);
            }
            e = nexte;
            restart(e);
        }
        theNewElementBean = null;
    }

    public int getEntity() {
        return theEntity;
    }

    private String makeName(char[] buff, int offset, int length) {
        StringBuffer dst = new StringBuffer(length + 2);
        boolean seenColon = false;
        boolean start = true;
        for (; length-- > 0; offset++) {
            char ch = buff[offset];
            if (Character.isLetter(ch) || ch == '_') {
                start = false;
                dst.append(ch);
            } else if (Character.isDigit(ch) || ch == '-' || ch == '.') {
                if (start) {
                    dst.append('_');
                }
                start = false;
                dst.append(ch);
            } else if (ch == ':' && !seenColon) {
                seenColon = true;
                if (start) {
                    dst.append('_');
                }
                start = true;
                dst.append(translateColons ? '_' : ch);
            }
        }
        int dstLength = dst.length();
        if (dstLength == 0 || dst.charAt(dstLength - 1) == ':') {
            dst.append('_');
        }
        return dst.toString().intern();
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
    }

    public void endCDATA() throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    public void endEntity(String name) throws SAXException {
    }

    public void startCDATA() throws SAXException {
    }

    public void startDTD(String name, String publicid, String systemid) throws SAXException {
    }

    public void startEntity(String name) throws SAXException {
    }

}
