package rw.bnr.javane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JavaNeApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaNeApplication.class, args);
    }

    //  Welcome message
    @GetMapping("/")
    public String welcome () {
        return "Welcome to 2024 JavaNE!";
    }
}
