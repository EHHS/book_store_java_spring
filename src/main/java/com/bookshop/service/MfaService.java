package com.bookshop.service;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;

@Service
public class MfaService {

    private static final int SECRET_SIZE_BYTES = 20;
    private static final int TIME_STEP_SECONDS = 30;
    private static final int CODE_DIGITS = 6;

    public String generateSecret() {
        byte[] buffer = new byte[SECRET_SIZE_BYTES];
        new SecureRandom().nextBytes(buffer);
        Base32 base32 = new Base32();
        return base32.encodeAsString(buffer).replace("=", "");
    }

    public String buildOtpAuthUrl(String issuer, String accountName, String secret) {
        String label = urlEncode(issuer) + ":" + urlEncode(accountName);
        String params = "secret=" + urlEncode(secret) + "&issuer=" + urlEncode(issuer) + "&algorithm=SHA1&digits=" + CODE_DIGITS + "&period=" + TIME_STEP_SECONDS;
        return "otpauth://totp/" + label + "?" + params;
    }

    public boolean verifyCode(String secret, int code) {
        long timeWindow = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
        // allow slight clock drift +/- 1 window
        for (long i = -1; i <= 1; i++) {
            int candidate = generateCode(secret, timeWindow + i);
            if (candidate == code) return true;
        }
        return false;
    }

    private int generateCode(String secret, long timeWindow) {
        try {
            Base32 base32 = new Base32();
            byte[] key = base32.decode(secret);
            byte[] data = ByteBuffer.allocate(8).putLong(timeWindow).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hmac = mac.doFinal(data);

            int offset = hmac[hmac.length - 1] & 0x0F;
            int binary = ((hmac[offset] & 0x7f) << 24)
                    | ((hmac[offset + 1] & 0xff) << 16)
                    | ((hmac[offset + 2] & 0xff) << 8)
                    | (hmac[offset + 3] & 0xff);

            int otp = binary % (int) Math.pow(10, CODE_DIGITS);
            return otp;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate TOTP code", e);
        }
    }

    private String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}


