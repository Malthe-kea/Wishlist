package org.example.wishlist;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WishlistApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(WishlistApplication.class, args);
	}

}
