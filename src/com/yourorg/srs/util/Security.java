package com.yourorg.srs.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Security {
    // Demo-only obfuscation per requirements
    public static String encodePassword(String plain) {
        return Base64.getEncoder().encodeToString(plain.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean matches(String plain, String encoded) {
        return encodePassword(plain).equals(encoded);
    }
}
