package br.com.votorantim.arqt.poc.writer;

import br.com.votorantim.arqt.poc.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class JdbcBatchItemWriterConfig {

    private final DataSource dataSource;

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Message> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<Message> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(this.dataSource);
        writer.setSql("INSERT INTO message (data) VALUES (:data)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.afterPropertiesSet();
        return writer;
    }

}
