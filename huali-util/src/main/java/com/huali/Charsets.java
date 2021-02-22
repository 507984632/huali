package com.huali;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Yang_my
 * @since 2020/12/20 - 14:56
 */
public interface Charsets {
    Charset UTF_8 = StandardCharsets.UTF_8;
    Charset US_ASCII = StandardCharsets.US_ASCII;
    Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    Charset GB18030 = Charset.forName("GB18030");
    String UTF_8_NAME = UTF_8.name();
    String ASCII_NAME = US_ASCII.name();
    String ISO_8859_1_NAME = ISO_8859_1.name();
    String GB18030_NAME = GB18030.name();
}

