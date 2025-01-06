package LogicFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Login {
    private static final Map<String, String> mapaTokens = new HashMap<>();

    public static String login(String username) {
        // Gera um token único
        String token = UUID.randomUUID().toString();

        // Associa o token ao nome de utilizador
        mapaTokens.put(token, username);

        System.out.println("Login bem-sucedido! Token gerado: " + token);
        return token;
    }

    public static boolean tokenValido(String token) {
        return mapaTokens.containsKey(token);
    }

    public static String obterUtilizadorPorToken(String token) {
        return mapaTokens.get(token);
    }

    public static void logout(String token) {
        if (mapaTokens.containsKey(token)) {
            mapaTokens.remove(token);
            System.out.println("Logout efetuado com sucesso para o token: " + token);
        } else {
            System.out.println("Token inválido ou inexistente.");
        }
    }

    public static boolean verificarTokenUsername(String username, String token) {
        if (!tokenValido(token)) {
            return false;
        }

        return mapaTokens.get(token).equals(username);
    }
}
