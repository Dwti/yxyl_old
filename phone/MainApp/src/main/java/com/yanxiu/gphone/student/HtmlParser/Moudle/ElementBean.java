package com.yanxiu.gphone.student.HtmlParser.Moudle;

import com.yanxiu.gphone.student.HtmlParser.Html.HtmlAttributes;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ElementBean {

	private ElementTypeBean Type;
	private HtmlAttributes Atts;
	private ElementBean Next;
	private boolean preclosed;

	public ElementBean(ElementTypeBean type, boolean defaultAttributes) {
		Type = type;
		if (defaultAttributes) {
			Atts = new HtmlAttributes(type.atts());
		}else {
			Atts = new HtmlAttributes();
		}
		Next = null;
		preclosed = false;
	}

	public ElementTypeBean type() { return Type; }

	public HtmlAttributes atts() { return Atts; }

	public ElementBean next() { return Next; }

	public void setNext(ElementBean next) { Next = next; }

	public String name() { return Type.name(); }

	public String namespace() { return Type.namespace(); }

	public String localName() { return Type.localName(); }

	public int model() { return Type.model(); }

	public int memberOf() { return Type.memberOf(); }

	public int flags() { return Type.flags(); }

	public ElementTypeBean parent() { return Type.parent(); }

	public boolean canContain(ElementBean other) {
		return Type.canContain(other.Type);
		}

	public void setAttribute(String name, String type, String value) {
		Type.setAttribute(Atts, name, type, value);
	}

	public void anonymize() {
		for (int i = Atts.getLength() - 1; i >= 0; i--) {
			if (Atts.getType(i).equals("ID") ||
			    Atts.getQName(i).equals("name")) {
				Atts.removeAttribute(i);
			}
		}
	}

	public void clean() {
		for (int i = Atts.getLength() - 1; i >= 0; i--) {
			String name = Atts.getLocalName(i);
			if (Atts.getValue(i) == null || name == null ||
					name.length() == 0) {
				Atts.removeAttribute(i);
				continue;
			}
		}
	}

	public void preclose() {
		preclosed = true;
		}

	public boolean isPreclosed() {
		return preclosed;
		}

}
