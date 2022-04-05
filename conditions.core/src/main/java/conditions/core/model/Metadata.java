package conditions.core.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class Metadata {

    @Transient
    private Condition condition;

    @ElementCollection
    @MapKeyColumn(name = "KEY")
    @Column(name = "VALUE")
    @CollectionTable(name = "CONDITION_METADATA", joinColumns = @JoinColumn(name = "CONDITION_ID"))
    private List<Data> datas = new LinkedList<>();

    Metadata() {
    }

    public Set<Map.Entry<String, ClientIdentifier>> getClients() {
        return this.datas.stream()
                .filter(v -> v.type == Type.CLIENT)
                .map(v -> Map.entry(v.key, new ClientIdentifier(v.value)))
                .collect(Collectors.toSet());
    }

    public List<Data> getData() {
        return List.copyOf(this.datas);
    }

    public void set(String key, Type type, String value) {
        if (this.condition.getStatus() != Condition.Status.DRAFT
                && this.condition.getStatus() != Condition.Status.PENDING) {
            throw new IllegalStateException("cannot change metadata");
        }
        this.datas.add(new Data(key, type, value));
    }

    @Embeddable
    public static class Data {

        private String key;
        @Enumerated(EnumType.STRING)
        private Type type;
        @Convert(converter = EncryptionConverter.class)
        private String value;

        Data() {
        }

        Data(
                String key,
                Type type,
                String value
        ) {
            this.key = key;
            this.type = type;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Type getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Type {
        CLIENT
    }

    void setCondition(Condition condition) {
        this.condition = condition;
    }
}
