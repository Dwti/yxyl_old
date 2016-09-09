package com.yanxiu.gphone.student.HtmlParser.Moudle;

import com.yanxiu.gphone.student.HtmlParser.Html.HtmlAttributes;
import com.yanxiu.gphone.student.HtmlParser.Utils.Schema;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ElementTypeBean {

    private String Name;
    private String Namespace;
    private String LocalName;
    private int Model;
    private int MemberOf;
    private int Flags;
    private HtmlAttributes Atts;
    private ElementTypeBean Parent;
    private Schema Schema;

    public ElementTypeBean(String name, int model, int memberOf, int flags, Schema schema) {
        Name = name;
        Model = model;
        MemberOf = memberOf;
        Flags = flags;
        Atts = new HtmlAttributes();
        Schema = schema;
        Namespace = namespace(name, false);
        LocalName = localName(name);
    }

    public String namespace(String name, boolean attribute) {
        int colon = name.indexOf(':');
        if (colon == -1) {
            return attribute ? "" : Schema.getURI();
        }
        String prefix = name.substring(0, colon);
        if (prefix.equals("xml")) {
            return "http://www.w3.org/XML/1998/namespace";
        } else {
            return ("urn:x-prefix:" + prefix).intern();
        }
    }

    public String localName(String name) {
        int colon = name.indexOf(':');
        if (colon == -1) {
            return name;
        } else {
            return name.substring(colon + 1).intern();
        }
    }

    public String name() {
        return Name;
    }

    public String namespace() {
        return Namespace;
    }

    public String localName() {
        return LocalName;
    }

    public int model() {
        return Model;
    }

    public int memberOf() {
        return MemberOf;
    }

    public int flags() {
        return Flags;
    }

    public HtmlAttributes atts() {
        return Atts;
    }

    public ElementTypeBean parent() {
        return Parent;
    }

    public Schema schema() {
        return Schema;
    }

    public boolean canContain(ElementTypeBean other) {
        return (Model & other.MemberOf) != 0;
    }

    public void setAttribute(HtmlAttributes atts, String name, String type, String value) {
        if (name.equals("xmlns") || name.startsWith("xmlns:")) {
            return;
        }
        ;
        String namespace = namespace(name, true);
        String localName = localName(name);
        int i = atts.getIndex(name);
        if (i == -1) {
            name = name.intern();
            if (type == null) {
                type = "CDATA";
            }
            if (!type.equals("CDATA")) {
                value = normalize(value);
            }
            atts.addAttribute(namespace, localName, name, type, value);
        } else {
            if (type == null) {
                type = atts.getType(i);
            }
            if (!type.equals("CDATA")) {
                value = normalize(value);
            }
            atts.setAttribute(i, namespace, localName, name, type, value);
        }
    }

    public static String normalize(String value) {
        if (value == null) {
            return value;
        }
        value = value.trim();
        if (value.indexOf("  ") == -1) {
            return value;
        }
        boolean space = false;
        int len = value.length();
        StringBuffer b = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            char v = value.charAt(i);
            if (v == ' ') {
                if (!space) b.append(v);
                space = true;
            } else {
                b.append(v);
                space = false;
            }
        }
        return b.toString();
    }

    public void setAttribute(String name, String type, String value) {
        setAttribute(Atts, name, type, value);
    }

    public void setModel(int model) {
        Model = model;
    }

    public void setMemberOf(int memberOf) {
        MemberOf = memberOf;
    }

    public void setFlags(int flags) {
        Flags = flags;
    }

    public void setParent(ElementTypeBean parent) {
        Parent = parent;
    }

}
