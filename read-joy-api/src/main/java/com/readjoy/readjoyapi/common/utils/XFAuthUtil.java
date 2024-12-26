package com.readjoy.readjoyapi.common.utils;

import com.readjoy.readjoyapi.common.config.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class XFAuthUtil {

    @Value("${xunfei.apiKey}")
    private String apiKey;

    @Value("${xunfei.apiSecret}")
    private String apiSecret;

    @Value("${xunfei.host}")
    private String host;

    @Value("${xunfei.method}")
    private String method;

    @Value("${xunfei.path}")
    private String path;

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private String generateDateHeader() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.CHINA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date());
    }

    private String generateSignature() {
        try {
            String stringToSign = "host: " + host + "\n" +
                    "date: " + generateDateHeader() + "\n" +
                    method + " " + path + " HTTP/1.1";
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM));
            byte[] signatureBytes = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new BusinessException("Failed to generate signature");
        }
    }

    private String generateAuthorizationHeader(String signature) {
        return "api_key=\"" + apiKey + "\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"" + signature + "\"";
    }

    private String generateAuthorization() {
        String signature = generateSignature();
        String authorizationHeader = generateAuthorizationHeader(signature);
        return Base64.getEncoder().encodeToString(authorizationHeader.getBytes(StandardCharsets.UTF_8));
    }

    public String getAuthorizationUrl() {
        String date = generateDateHeader();
        String authorization = generateAuthorization();
        try {
            return "wss://" + host + path + "?authorization=" + URLEncoder.encode(authorization, "UTF-8")
                    + "&date=" + URLEncoder.encode(date, StandardCharsets.UTF_8)
                    + "&host=" + URLEncoder.encode(host, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BusinessException("Failed to generate authorization URL");
        }
    }
}