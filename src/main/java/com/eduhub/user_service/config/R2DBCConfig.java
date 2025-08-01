//package com.eduhub.user_service.config;
//
//import com.eduhub.user_service.model.enums.Role;
//import com.eduhub.user_service.model.enums.UserStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.convert.CustomConversions;
//import org.springframework.data.convert.ReadingConverter;
//import org.springframework.data.convert.WritingConverter;
//import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
//
//import io.r2dbc.spi.ConnectionFactory;
//
//import org.springframework.data.r2dbc.dialect.DialectResolver;
//import org.springframework.data.r2dbc.dialect.R2dbcDialect;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//import java.util.ArrayList;
//import java.util.List;
//@Configuration
//public class R2DBCConfig {
//
//    @Bean
//    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
//        R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);
//        List<Converter<?, ?>> converters = new ArrayList<>();
//        converters.add(new EnumToPgObjectConverter());
//        converters.add(new PgObjectToEnumConverter());
//        return new R2dbcCustomConversions(
//                CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder(), converters),
//                converters
//        );
//    }
//
//    @WritingConverter
//    static class EnumToPgObjectConverter implements Converter<Enum<?>, Object> {
//        @Override
//        public Object convert(Enum<?> source) {
//            return source.name(); // Convert enum to its name (string)
//        }
//    }
//
//    @ReadingConverter
//    static class PgObjectToEnumConverter implements Converter<Object, Enum<?>> {
//        @Override
//        public Enum<?> convert(Object source) {
//            // This is a simplified version - you'll need to implement
//            // specific enum type resolution based on your needs
//            if (source instanceof String) {
//                try {
//                    return Role.valueOf((String) source);
//                } catch (IllegalArgumentException e) {
//                    return UserStatus.valueOf((String) source);
//                }
//            }
//            throw new IllegalArgumentException("Cannot convert " + source + " to enum");
//        }
//    }
//}