package com.yanxiu.gphone.studentold.HtmlParser.Utils;

import com.yanxiu.gphone.studentold.HtmlParser.Moudle.ElementTypeBean;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/2.
 */
public abstract class Schema {

    public static final int M_ANY = 0xFFFFFFFF;
    public static final int M_EMPTY = 0;
    public static final int M_PCDATA = 1 << 30;
    public static final int M_ROOT = 1 << 31;


    public static final int F_RESTART = 1;
    public static final int F_CDATA = 2;
    public static final int F_NOFORCE = 4;

    private HashMap theEntities = new HashMap();
    private HashMap theElementTypes = new HashMap();

    private String theURI = "";
    private String thePrefix = "";
    private ElementTypeBean theRoot = null;

    public void elementType(String name, int model, int memberOf, int flags) {
        ElementTypeBean e = new ElementTypeBean(name, model, memberOf, flags, this);
        theElementTypes.put(name.toLowerCase(), e);
        if (memberOf == M_ROOT) {
            theRoot = e;
        }
    }

    public ElementTypeBean rootElementType() {
        return theRoot;
    }

    public void attribute(String elemName, String attrName, String type, String value) {
        ElementTypeBean e = getElementType(elemName);
        if (e == null) {
            throw new Error("Attribute " + attrName + " specified for unknown element type " + elemName);
        }
        e.setAttribute(attrName, type, value);
    }

    public void parent(String name, String parentName) {
        ElementTypeBean child = getElementType(name);
        ElementTypeBean parent = getElementType(parentName);
        if (child == null) {
            throw new Error("No child " + name + " for parent " + parentName);
        }
        if (parent == null) {
            throw new Error("No parent " + parentName + " for child " + name);
        }
        child.setParent(parent);
    }

    public void entity(String name, int value) {
        theEntities.put(name, new Integer(value));
    }

    public ElementTypeBean getElementType(String name) {
        return (ElementTypeBean) (theElementTypes.get(name.toLowerCase()));
    }

    public int getEntity(String name) {
        Integer ch = (Integer) theEntities.get(name);
        if (ch == null) {
            return 0;
        }else {
            return ch.intValue();
        }
    }

    public String getURI() {
        return theURI;
    }

    public String getPrefix() {
        return thePrefix;
    }

    public void setURI(String uri) {
        theURI = uri;
    }

    public void setPrefix(String prefix) {
        thePrefix = prefix;
    }

}
