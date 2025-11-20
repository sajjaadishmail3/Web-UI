package saj.techie.managementbe.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class JacksonConfig {

    private static final List<DateTimeFormatter> IN_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE
    );

    @Bean
    public Module javaTimeLenientModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                String text = p.getValueAsString();
                if (text == null || text.trim().isEmpty()) return null;
                for (DateTimeFormatter f : IN_FORMATS) {
                    try {
                        return LocalDate.parse(text, f);
                    } catch (Exception ignored) { }
                }
                return LocalDate.parse(text);
            }
        });
        return module;
    }
}
