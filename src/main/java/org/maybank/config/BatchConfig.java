package org.maybank.config;

import org.maybank.entity.Transaction;
import org.maybank.repository.TransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {



    @Bean
    public FlatFileItemReader<Transaction> reader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("dataSource.txt"));
        reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper<Transaction>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter("|");
                setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType((Transaction.class));
            }});
        }});
        return reader;
    }

    @Bean
    public RepositoryItemWriter<Transaction> writer(TransactionRepository repository){
        RepositoryItemWriter<Transaction> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return  writer;
    }

    @Bean
    public  Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                       FlatFileItemReader<Transaction> reader,
                       RepositoryItemWriter<Transaction> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step1){
        return new JobBuilder("transaction-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .incrementer(new org.springframework.batch.core.launch.support.RunIdIncrementer())
                .build();
    }
}
