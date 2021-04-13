package com.mediscreen.notes.config;

import com.mediscreen.notes.persistence.converter.ZonedDateTimeReadConverter;
import com.mediscreen.notes.persistence.converter.ZonedDateTimeWriteConverter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new ZonedDateTimeWriteConverter(),
                new ZonedDateTimeReadConverter()
        ));
    }
}
