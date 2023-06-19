package com.nexr.de.grnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import com.nexr.de.grnd.service.GreeterServiceImpl;

@SpringBootApplication
public class GovernmentRnDDemoApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(GovernmentRnDDemoApplication.class, args);
	}

}
