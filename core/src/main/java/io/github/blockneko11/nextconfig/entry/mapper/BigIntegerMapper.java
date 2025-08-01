package io.github.blockneko11.nextconfig.entry.mapper;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class BigIntegerMapper implements EntryMapper<BigInteger> {
    @Override
    public BigInteger toEntry(@NotNull Object property) {
        return new BigInteger((String) property);
    }

    @NotNull
    @Override
    public Object toProperty(BigInteger entry) {
        return entry.toString();
    }
}
