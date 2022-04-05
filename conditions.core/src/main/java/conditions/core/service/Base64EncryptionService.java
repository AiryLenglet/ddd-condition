package conditions.core.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64EncryptionService implements EncryptionService {

    @Override
    public String encrypt(String aString) {
        return Base64.getEncoder().encodeToString(aString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decrypt(String encryptedString) {
        return new String(Base64.getDecoder().decode(encryptedString));
    }
}
