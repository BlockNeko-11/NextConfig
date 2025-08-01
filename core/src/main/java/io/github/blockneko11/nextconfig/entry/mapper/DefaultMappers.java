package io.github.blockneko11.nextconfig.entry.mapper;

import io.github.blockneko11.nextconfig.throwable.ConfigMappingException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class DefaultMappers {
    public static class BigIntegerMapper implements EntryMapper<BigInteger> {
        @NotNull
        @Override
        public Class<BigInteger> getEntryType() {
            return BigInteger.class;
        }

        @Override
        public BigInteger toEntry(@NotNull Object property) throws ConfigMappingException {
            return new BigInteger((String) property);
        }

        @Override
        public Object toProperty(BigInteger entry) throws ConfigMappingException {
            return entry.toString();
        }
    }

    public static class BigDecimalMapper implements EntryMapper<BigDecimal> {
        @NotNull
        @Override
        public Class<BigDecimal> getEntryType() {
            return BigDecimal.class;
        }

        @Override
        public BigDecimal toEntry(@NotNull Object property) throws ConfigMappingException {
            return new BigDecimal((String) property);
        }

        @Override
        public Object toProperty(BigDecimal entry) throws ConfigMappingException {
            return entry.toString();
        }
    }

    public static class ColorMapper implements EntryMapper<Color> {
        private final boolean hasAlpha;

        public ColorMapper(boolean hasAlpha) {
            this.hasAlpha = hasAlpha;
        }

        public ColorMapper() {
            this(false);
        }

        @NotNull
        @Override
        public Class<Color> getEntryType() {
            return Color.class;
        }

        @Override
        public Color toEntry(@NotNull Object property) throws ConfigMappingException {
            java.util.List<Integer> list = (java.util.List<Integer>) property;

            if (list.size() == 3) {
                return new Color(list.get(0), list.get(1), list.get(2));
            }

            if (this.hasAlpha && list.size() == 4) {
                return new Color(list.get(0), list.get(1), list.get(2), list.get(3));
            }

            throw new ConfigMappingException("Invalid color format");
        }

        @Override
        public Object toProperty(Color entry) throws ConfigMappingException {
            List<Integer> list = new ArrayList<>();
            list.add(entry.getRed());
            list.add(entry.getGreen());
            list.add(entry.getBlue());

            if (this.hasAlpha) {
                list.add(entry.getAlpha());
            }

            return list;
        }
    }
}
