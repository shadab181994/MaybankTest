package org.maybank;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactEaseApplication implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job importTransactionJob;
    public static void main(String[] args) {
        SpringApplication.run(TransactEaseApplication.class, args);
    }

    @Override
    public void run(String...args){
        try {
            System.out.println("Starting TransactEase Batch Job...");

            JobParameters jobParameter = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

                    jobLauncher.run(importTransactionJob, jobParameter);
                    System.out.println("Batch Job Completed Successfully!");
        }
        catch(Exception e){
            System.err.println("Error While Running Batch Job: " + e.getMessage());
            e.printStackTrace();
        }
    }
}