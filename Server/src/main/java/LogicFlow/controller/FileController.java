package LogicFlow.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import LogicFlow.Login;
import LogicFlow.repository.UserRepository;

@RestController
public class FileController {

    private void saveFile(String username, String codigo, String filename) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("save", username, filename);
            System.out.println("Caminho do ficheiro: " + path.toAbsolutePath());
            java.nio.file.Files.createDirectories(path.getParent());
            java.nio.file.Files.write(path, codigo.getBytes(), java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Ficheiro guardado com sucesso!");

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String username, String filename) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("save", username, filename);
            System.out.println("Caminho do ficheiro: " + path.toAbsolutePath());
            String codigo = new String(java.nio.file.Files.readAllBytes(path));
            System.out.println("Ficheiro aberto com sucesso!");
            return codigo;
        } catch (java.io.IOException e) {
            return null;
        }
    }

    @PostMapping("/save-file")
    public ResponseEntity<String> processFile(
            @RequestParam String username,
            @RequestParam String token,
            @RequestParam String codigo,
            @RequestParam String filename) {

        if (username.isBlank() || token.isBlank() || codigo.isBlank() || filename.isBlank()) {
            System.out.println("Todos os parâmetros são obrigatórios!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos os parâmetros são obrigatórios!");
        }

        if (filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
            System.out.println("Nome de ficheiro inválido!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome de ficheiro inválido!");
        }

        if (!Login.verificarTokenUsername(username, token)) {
            System.out.println("Token inválido ou inexistente.");
            System.out.println("Username: " + username);
            System.out.println("Token: " + token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou inexistente.");
        }

        System.out.println("Username: " + username);
        System.out.println("Token: " + token);
        System.out.println("Código: " + codigo);

        saveFile(username, codigo, filename);

        System.out.println("Ficheiro guardado com sucesso em /save/" + username + "/" + filename);

        return ResponseEntity.ok("Processamento concluído com sucesso!");
    }

    @PostMapping("/open-file")
    public ResponseEntity<String> openFile(
            @RequestParam String username,
            @RequestParam String token,
            @RequestParam String filename) {

        if (username.isBlank() || token.isBlank() || filename.isBlank()) {
            System.out.println("Todos os parâmetros são obrigatórios!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos os parâmetros são obrigatórios!");
        }

        if (filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
            System.out.println("Nome de ficheiro inválido!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome de ficheiro inválido!");
        }

        if (!Login.verificarTokenUsername(username, token)) {
            System.out.println("Token inválido ou inexistente.");
            System.out.println("Username: " + username);
            System.out.println("Token: " + token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou inexistente.");
        }

        System.out.println("Username: " + username);
        System.out.println("Token: " + token);

        String codigo = readFile(username, filename);
        if (codigo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ficheiro não encontrado!");
        }
        return ResponseEntity.ok(codigo);
    }

    private HashMap<String, ArrayList<String>> mapaCircuitosPartilha = new HashMap<>();

    @PostMapping("/send-circuit")
    public ResponseEntity<String> sendCircuit(
            @RequestParam String username,
            @RequestParam String token,
            @RequestParam String filename,
            @RequestParam String destinatario) {

        if (username.isBlank() || token.isBlank() || filename.isBlank() || destinatario.isBlank()) {
            System.out.println("Todos os parâmetros são obrigatórios!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos os parâmetros são obrigatórios!");
        }

        if (filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
            System.out.println("Nome de ficheiro inválido!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome de ficheiro inválido!");
        }

        if (!Login.verificarTokenUsername(username, token)) {
            System.out.println("Token inválido ou inexistente.");
            System.out.println("Username: " + username);
            System.out.println("Token: " + token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou inexistente.");
        }

        System.out.println("Username: " + username);
        System.out.println("Token: " + token);
        System.out.println("Ficheiro: " + filename);
        System.out.println("Destinatário: " + destinatario);

        if (!mapaCircuitosPartilha.containsKey(destinatario)) {
            mapaCircuitosPartilha.put(destinatario, new ArrayList<>());
        }
        mapaCircuitosPartilha.get(destinatario).add(username + "/" + filename);

        return null;
    }

    @PostMapping("/list-shared-circuits")
    public ResponseEntity<ArrayList<String>> listSharedCircuits(
            @RequestParam String username,
            @RequestParam String token) {

        if (username.isBlank() || token.isBlank()) {
            System.out.println("Todos os parâmetros são obrigatórios!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ArrayList<>());
        }

        if (!Login.verificarTokenUsername(username, token)) {
            System.out.println("Token inválido ou inexistente.");
            System.out.println("Username: " + username);
            System.out.println("Token: " + token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        System.out.println("Username: " + username);
        System.out.println("Token: " + token);

        if (!mapaCircuitosPartilha.containsKey(username)) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        return ResponseEntity.ok(mapaCircuitosPartilha.get(username));
    }

    @PostMapping("/accept-circuit")
    public ResponseEntity<String> acceptCircuit(
            @RequestParam String username,
            @RequestParam String token,
            @RequestParam String filename) {

        if (username.isBlank() || token.isBlank() || filename.isBlank()) {
            System.out.println("Todos os parâmetros são obrigatórios!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos os parâmetros são obrigatórios!");
        }

        if (filename.contains("..")) {
            System.out.println("Nome de ficheiro inválido!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome de ficheiro inválido!");
        }

        if (!Login.verificarTokenUsername(username, token)) {
            System.out.println("Token inválido ou inexistente.");
            System.out.println("Username: " + username);
            System.out.println("Token: " + token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou inexistente.");
        }

        System.out.println("Username: " + username);
        System.out.println("Token: " + token);
        System.out.println("Ficheiro: " + filename);

        if (!mapaCircuitosPartilha.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não existem circuitos partilhados para aceitar!");
        }

        if (!mapaCircuitosPartilha.get(username).contains(filename)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O circuito não foi partilhado consigo!");
        }

        try {
            // problema que nao quero resolver confio fielmente no filename que vem do
            // usuario possivelmente malicioso :D
            String sharedUser = filename.split("/")[0];
            String sharedFilename = filename.split("/")[1];
            String codigo = readFile(sharedUser, sharedFilename);
            saveFile(username, codigo, sharedFilename);
            mapaCircuitosPartilha.get(username).remove(filename);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O circuito não foi partilhado consigo!");
        }

        return ResponseEntity.ok("Circuito aceite com sucesso!");
    }
}
