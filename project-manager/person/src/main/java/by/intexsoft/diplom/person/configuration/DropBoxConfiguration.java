package by.intexsoft.diplom.person.configuration;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropBoxConfiguration {

        @Value("${dropbox.access.token}")
        private String ACCESS_TOKEN;

        @Bean
        public DbxClientV2 dbxClient() {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            return new DbxClientV2(config, ACCESS_TOKEN);
        }
}