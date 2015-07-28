package modobox;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import javafx.application.Application;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class App extends AbstractJavaFxApplicationSupport {
    
    @Override
	public void start(Stage stage) throws Exception {
    }

    public static void main(String[] args) {
	/*
        ApplicationContext ctx = SpringApplication.run(App.class, args);        
        System.out.println("modobox is up and running!");        
	*/

	launchApp(App.class, args);
	
    }

}
