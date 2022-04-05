package conditions.core.service;

public interface EncryptionService {

    String encrypt(String aString);

    String decrypt(String encryptedString);
}
