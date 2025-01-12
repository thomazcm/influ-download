package br.com.businesstec.download_service.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Value("${download.datasource.url}")
    private String databaseUrl;

    @Bean
    @Profile("dev")
    public DataSource devDataSource(@Value("${download.datasource.dev_url}") String localDatabaseUrl) throws URISyntaxException {
        return getHikariDataSource(localDatabaseUrl);
    }

    @Bean
    @Profile("!dev")
    public DataSource cloudDataSource() throws URISyntaxException {
        return getHikariDataSource(databaseUrl);
    }

    private static HikariDataSource getHikariDataSource(String url) throws URISyntaxException {
        final URI dbUri = new URI(url);

        final String jdbcUrl = String.format("jdbc:postgresql://%s:%s%s", dbUri.getHost(), dbUri.getPort(), dbUri.getPath());
        final String username = dbUri.getUserInfo().split(":")[0];
        final String password = dbUri.getUserInfo().split(":")[1];

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        return new HikariDataSource(hikariConfig);
    }

}
