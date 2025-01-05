package LogicFlow.controller;

import LogicFlow.Login;
import LogicFlow.model.User;
import LogicFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create-user")
    public ResponseEntity<String> create(@RequestParam String username, @RequestParam String password) {
        User userExist = userRepository.findByUsername(username);
        if (userExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        User user = new User();
        user.setUsername(username);

        String hashedPassword = passwordEncoder.encode(password);
        user.setHash(hashedPassword);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getHash())) {
            String token = Login.login(username);
            return ResponseEntity.ok(new LoginResponse(user.getUsername(), token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        }
    }

    private static class LoginResponse {
        private String user;
        private String token;

        public LoginResponse(String user, String token) {
            this.user = user;
            this.token = token;
        }

        public String getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }

    public static ArrayList<String> getAllFiles(String username) {
        ArrayList<String> files = new ArrayList<>();
        java.nio.file.Path path = java.nio.file.Paths.get("save", username);
        try {
            java.nio.file.Files.walk(path, 1).forEach(filePath -> {
                if (java.nio.file.Files.isRegularFile(filePath)) {
                    files.add(filePath.getFileName().toString());
                }
            });
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    @PostMapping("/list-files")
    public ResponseEntity<?> listFiles(@RequestParam String username, @RequestParam String token) {
        if (!Login.verificarTokenUsername(username, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou inexistente.");
        }
        ArrayList<String> files = getAllFiles(username);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok("pong");
    }
}
