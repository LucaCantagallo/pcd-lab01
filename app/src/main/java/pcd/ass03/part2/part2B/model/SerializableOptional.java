package pcd.ass03.part2.part2B.model;

import java.io.Serializable;
import java.util.Optional;

//classe che fa da wrapper per Optional, in modo da renderlo serializzabile
public class SerializableOptional<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final T value;

    public SerializableOptional(Optional<T> optional) {
        this.value = optional.orElse(null);
    }

    public Optional<T> getOptional() {
        return Optional.ofNullable(value);
    }
}

