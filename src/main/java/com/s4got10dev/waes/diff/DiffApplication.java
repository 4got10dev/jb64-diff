package com.s4got10dev.waes.diff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.s4got10dev.waes.diff.storage.eventsourcing.DiffPartRepository;
import com.s4got10dev.waes.diff.storage.eventsourcing.DiffResultRepository;

/**
 * @author Serhii Homeniuk
 */
@SpringBootApplication
public class DiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffApplication.class, args);
    }
}
