package com.portfolio.japnet.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.japnet.javafx.src.application.JAP;

@RestController
public class JavaFxController {

	@GetMapping("/run-javafx")
    public ResponseEntity<String> runJavaFxApp() {
		new Thread(()->{
			JAP.jap();
		}).start();
		
        return ResponseEntity.ok("JavaFX application started.");
    }
}
