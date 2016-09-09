package com.yanxiu.gphone.student.HtmlParser.Interface;

import java.io.InputStream;
import java.io.Reader;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface AutoDetectorListener {
	Reader autoDetectingReader(InputStream i);
}
