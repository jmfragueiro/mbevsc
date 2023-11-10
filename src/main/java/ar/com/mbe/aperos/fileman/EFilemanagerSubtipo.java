package ar.com.mbe.aperos.fileman;

import java.util.Arrays;

public enum EFilemanagerSubtipo {
    FOTO,
    DNI,
    DOC,
    OTROS;

    public static EFilemanagerSubtipo getFromString(String name) {
        return Arrays.stream(values())
                     .filter(v -> v.name().equalsIgnoreCase(name))
                     .findFirst()
                     .orElse(OTROS);
    }
}
