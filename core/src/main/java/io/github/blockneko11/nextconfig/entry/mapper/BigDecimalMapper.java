package io.github.blockneko11.nextconfig.entry.mapper;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class BigDecimalMapper implements EntryMapper<BigDecimal> {
    @NotNull
    @Override
    public Class<BigDecimal> getEntryType() {
        return BigDecimal.class;
    }

    @Override
    public BigDecimal toEntry(@NotNull Object property) {
        return new BigDecimal((String) property);
    }

    @Override
    public Object toProperty(BigDecimal entry) {
        return entry.toString();
    }
}
