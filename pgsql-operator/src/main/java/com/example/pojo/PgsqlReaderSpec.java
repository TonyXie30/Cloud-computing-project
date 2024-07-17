package com.example.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "database")
public class PgsqlReaderSpec {
    private List<DBProperties> secondary;

    public List<DBProperties> getSecondary() {
        return secondary;
    }

    public void setSecondary(List<DBProperties> secondary) {
        this.secondary = secondary;
    }

    public static class DBProperties{
        private String url;
        private String username;
        private String password;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
