package ar.com.mbe.base.filter;

import ar.com.mbe.core.common.C;

import java.time.LocalDateTime;

public record RPredicado(String field, RPredicadoConfig config, String valor) {
    public EPredicadoOperador getOp() {
        return config.op();
    }

    public EPredicadoTipo getTipo() {
        return config.tipo();
    }

    public boolean isIgnoreCase() {
        return config.ignoreCase();
    }

    public Object getParsedValue() {
        return switch (getTipo()) {
            case DATE -> LocalDateTime.parse(valor);
            case INTEGER -> Integer.decode(valor);
            case LONG -> Long.valueOf(valor);
            case BOOLEAN -> (valor.equalsIgnoreCase(C.SYS_CAD_VERDADERO) ? 1 : 0);
            default -> valor;
        };
    }
}
