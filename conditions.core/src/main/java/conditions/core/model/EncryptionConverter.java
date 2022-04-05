package conditions.core.model;

import conditions.core.service.EncryptionService;

import javax.persistence.AttributeConverter;

public class EncryptionConverter implements AttributeConverter<String, String> {

    public enum Dependencies {

        INSTANCE;

        private EncryptionService encryptionService;

        public void set(EncryptionService encryptionService) {
            this.encryptionService = encryptionService;
        }

        public EncryptionService getEncryptionService() {
            return encryptionService;
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return Dependencies.INSTANCE.getEncryptionService().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return Dependencies.INSTANCE.getEncryptionService().decrypt(dbData);
    }
}
