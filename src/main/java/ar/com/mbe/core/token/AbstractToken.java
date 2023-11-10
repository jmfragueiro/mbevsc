package ar.com.mbe.core.token;

import ar.com.mbe.core.common.F;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class AbstractToken<K, U extends ITokenPayload> implements IToken<K, U> {
    private final Long default_min_term = 60L;
    private final U payload;
    private final LocalDateTime issuedAt = LocalDateTime.now();
    private LocalDateTime vencimiento;

    public AbstractToken(U payload) {
        this.payload = payload;
        this.vencimiento = this.nextTerm(issuedAt, default_min_term);
        payload.reinitAuthorities();
    }

    private LocalDateTime nextTerm(LocalDateTime ini, Long plazo) {
        return ini.plus(Math.max(default_min_term, (plazo == null ? default_min_term : plazo)), ChronoUnit.SECONDS);
    }

    @Override
    public abstract K getId();

    @Override
    public U getPayload() {
        return payload;
    }

    @Override
    public IToken<K, U> reinitTerm(Long plazo) {
        this.vencimiento = nextTerm(LocalDateTime.now(), plazo);
        return this;
    }

    @Override
    public boolean isExpired() {
        return F.esFechaPasada(this.vencimiento);
    }

    @Override
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
}
