import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LoginRegisterPanel extends JPanel {

    private CardLayout cardLayout;

    // Painéis distintos
    private JPanel loginPanel;
    private JPanel registerPanel;

    // Componentes do painel de Login
    private JTextField loginUserField;
    private JPasswordField loginPasswordField;

    // Componentes do painel de Registo
    private JTextField registerUserField;
    private JPasswordField registerPasswordField;
    private JPasswordField registerConfirmPasswordField;

    public static String username;
    public static String token;

    public static String DOMAIN;

    /**
     * Construtor do painel principal
     */
    public LoginRegisterPanel() {
        DOMAIN = "http://marquesserver.freeddns.org:2020";
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Inicializar e configurar cada painel

        loginPanel = criarPainelLogin();
        registerPanel = criarPainelRegisto();

        // Adicionar os dois painéis ao CardLayout com identificadores
        add(loginPanel, "LOGIN_PANEL");
        add(registerPanel, "REGISTER_PANEL");

        // Iniciar no painel de Login
        cardLayout.show(this, "LOGIN_PANEL");

    }

    /**
     * Método para criar o Painel de Login com UI mais apelativa
     */
    private JPanel criarPainelLogin() {
        // Painel "container" principal
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(245, 245, 245));

        // Título do painel de Login
        JLabel titulo = new JLabel("Iniciar Sessão", JLabel.CENTER);
        titulo.setFont(obterFonte(24, Font.BOLD));
        titulo.setForeground(new Color(54, 54, 54));
        painel.add(titulo, BorderLayout.NORTH);

        // Painel central para formulário
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false); // para “herdar” a cor de fundo do painel pai

        // Campo "Utilizador"
        JPanel linhaUser = new JPanel(new BorderLayout());
        linhaUser.setOpaque(false);
        JLabel lblUser = new JLabel("Utilizador: ");
        lblUser.setFont(obterFonte(16, Font.PLAIN));
        linhaUser.add(lblUser, BorderLayout.WEST);
        loginUserField = new JTextField(20);
        estilizarCampoTexto(loginUserField);
        linhaUser.add(loginUserField, BorderLayout.CENTER);
        linhaUser.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Campo "Palavra-passe"
        JPanel linhaPass = new JPanel(new BorderLayout());
        linhaPass.setOpaque(false);
        JLabel lblPass = new JLabel("Palavra-passe: ");
        lblPass.setFont(obterFonte(16, Font.PLAIN));
        linhaPass.add(lblPass, BorderLayout.WEST);
        loginPasswordField = new JPasswordField(20);
        estilizarCampoTexto(loginPasswordField);
        linhaPass.add(loginPasswordField, BorderLayout.CENTER);
        linhaPass.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        centro.add(linhaUser);
        centro.add(linhaPass);

        // Painel dos botões em baixo
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botoes.setOpaque(false);

        // Botão de Login
        JButton loginButton = new JButton("Entrar");
        estilizarBotao(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginUserField.getText();
                String password = new String(loginPasswordField.getPassword());
                login(username, password);
            }
        });
        botoes.add(loginButton);

        // Botão para ir para o painel de Registo
        JButton goToRegisterButton = new JButton("Registar");
        estilizarBotao(goToRegisterButton);
        goToRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(LoginRegisterPanel.this, "REGISTER_PANEL");
            }
        });
        botoes.add(goToRegisterButton);

        // Adicionar o painel centro e o painel de botões
        painel.add(centro, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    /**
     * Método para criar o Painel de Registo com UI mais apelativa
     */
    private JPanel criarPainelRegisto() {
        // Painel "container" principal
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(245, 245, 245));

        // Título do painel de Registo
        JLabel titulo = new JLabel("Criar Nova Conta", JLabel.CENTER);
        titulo.setFont(obterFonte(24, Font.BOLD));
        titulo.setForeground(new Color(54, 54, 54));
        painel.add(titulo, BorderLayout.NORTH);

        // Painel central para formulário
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        // Campo "Utilizador"
        JPanel linhaUser = new JPanel(new BorderLayout());
        linhaUser.setOpaque(false);
        JLabel lblUser = new JLabel("Utilizador: ");
        lblUser.setFont(obterFonte(16, Font.PLAIN));
        linhaUser.add(lblUser, BorderLayout.WEST);
        registerUserField = new JTextField(20);
        estilizarCampoTexto(registerUserField);
        linhaUser.add(registerUserField, BorderLayout.CENTER);
        linhaUser.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Campo "Palavra-passe"
        JPanel linhaPass = new JPanel(new BorderLayout());
        linhaPass.setOpaque(false);
        JLabel lblPass = new JLabel("Palavra-passe: ");
        lblPass.setFont(obterFonte(16, Font.PLAIN));
        linhaPass.add(lblPass, BorderLayout.WEST);
        registerPasswordField = new JPasswordField(20);
        estilizarCampoTexto(registerPasswordField);
        linhaPass.add(registerPasswordField, BorderLayout.CENTER);
        linhaPass.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Campo "Confirmar palavra-passe"
        JPanel linhaConfirmPass = new JPanel(new BorderLayout());
        linhaConfirmPass.setOpaque(false);
        JLabel lblConfPass = new JLabel("Confirmar palavra-passe: ");
        lblConfPass.setFont(obterFonte(16, Font.PLAIN));
        linhaConfirmPass.add(lblConfPass, BorderLayout.WEST);
        registerConfirmPasswordField = new JPasswordField(20);
        estilizarCampoTexto(registerConfirmPasswordField);
        linhaConfirmPass.add(registerConfirmPasswordField, BorderLayout.CENTER);
        linhaConfirmPass.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        centro.add(linhaUser);
        centro.add(linhaPass);
        centro.add(linhaConfirmPass);

        // Painel dos botões em baixo
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botoes.setOpaque(false);

        // Botão de Registo
        JButton registerButton = new JButton("Registar");
        estilizarBotao(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = registerUserField.getText();
                String password = new String(registerPasswordField.getPassword());
                String confirmPass = new String(registerConfirmPasswordField.getPassword());

                if (!password.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(LoginRegisterPanel.this,
                            "As palavras-passe não coincidem!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                register(username, password);
            }
        });
        botoes.add(registerButton);

        // Botão para voltar ao painel de Login
        JButton goToLoginButton = new JButton("Voltar");
        estilizarBotao(goToLoginButton);
        goToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(LoginRegisterPanel.this, "LOGIN_PANEL");
            }
        });
        botoes.add(goToLoginButton);

        // Adicionar o painel centro e o painel de botões
        painel.add(centro, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    public static void drawTela() {
        JFrame frame = new JFrame("Login / Registo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null); // Centra a janela no ecrã

        // Adicionar o painel principal
        frame.setContentPane(new LoginRegisterPanel());

        frame.setVisible(true);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (LoginRegisterPanel.token != null) {
                frame.dispose();
                break;
            }
        }
    }

    private Font obterFonte(int tamanho, int estilo) {
        // Podes personalizar trocando a fonte, por exemplo "Arial", "Verdana", etc.
        // e ajustando o tamanho e o estilo (Font.PLAIN, Font.BOLD, Font.ITALIC).
        return new Font("SansSerif", estilo, tamanho);
    }

    private void estilizarBotao(JButton botao) {
        botao.setBackground(new Color(70, 130, 180));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(obterFonte(14, Font.BOLD));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(obterFonte(14, Font.PLAIN));
        campo.setMargin(new Insets(5, 5, 5, 5));
    }

    private void login(String username, String password) {
        System.out.println("[LOGIN] Utilizador: " + username + " | Pass: " + password);
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/login");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                    + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) { // Sucesso
                // Ler a resposta JSON
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line.trim());
                    }

                    // Processar o JSON da resposta
                    String jsonResponse = response.toString();
                    System.out.println("Response: " + jsonResponse);

                    // Extrair o `user` e o `token` (exemplo simples)
                    if (jsonResponse.contains("\"user\":") && jsonResponse.contains("\"token\":")) {
                        String user = jsonResponse.split("\"user\":")[1].split(",")[0].replaceAll("[\"{}]", "").trim();
                        String token = jsonResponse.split("\"token\":")[1].replaceAll("[\"{}]", "").trim();
                        System.out.println("User: " + user);
                        System.out.println("Token: " + token);
                        LoginRegisterPanel.username = user;
                        LoginRegisterPanel.token = token;
                    }
                }
            } else if (responseCode == 401) {
                JOptionPane.showMessageDialog(null, "Credenciais inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                ProgCircuito.ErrorBox("Erro: Ao fazer login!");
                System.out.println("Erro: O servidor retornou o código " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(String username, String password) {
        System.out.println("[REGISTO] Novo Utilizador: " + username + " | Pass: " + password);
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/create-user");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + username + "&password=" + password;

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Obter a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == 201) {
                JOptionPane.showMessageDialog(null, "Utilizador criado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                ProgCircuito.ErrorBox("Erro: Ao criar utilizador!");
                JOptionPane.showMessageDialog(null, "Erro ao criar utilizador!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOnServer(String codigo, String filename) {
        System.out.println("[SAVE FILE] Código: " + codigo + " | Filename: " + filename);
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/save-file");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + LoginRegisterPanel.token);
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(LoginRegisterPanel.username, StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(LoginRegisterPanel.token, StandardCharsets.UTF_8)
                    + "&codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8)
                    + "&filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                System.out.println("Ficheiro guardado com sucesso no servidor.");
            } else if (responseCode == 401) {
                ProgCircuito.ErrorBox("Erro: Autorização falhou. Token inválido ou expirado.");
                System.out.println("Erro: Autorização falhou. Token inválido ou expirado.");
            } else {
                ProgCircuito.ErrorBox("Erro: O servidor retornou o código " + responseCode);
                System.out.println("Erro: O servidor retornou o código " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> openSave(String filename) {
        System.out.println("[OPEN FILE] Filename: " + filename);
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/open-file");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + LoginRegisterPanel.token);
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(LoginRegisterPanel.username, StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(LoginRegisterPanel.token, StandardCharsets.UTF_8)
                    + "&filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                // Ler a resposta JSON
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    ArrayList<String> response = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.add(line.trim());
                    }
                    return response;
                }
            } else if (responseCode == 401) {
                ProgCircuito.ErrorBox("Erro: Autorização falhou. Token inválido ou expirado.");
                System.out.println("Erro: Autorização falhou. Token inválido ou expirado.");
            } else {
                ProgCircuito.ErrorBox("Erro: O servidor retornou o código " + responseCode + "\n Provavelmente o " + filename
                        + " não existe.");
                System.out.println("Erro: O servidor retornou o código " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> convertToArrayList(String jsonString) {
        // Remove the brackets and quotes
        jsonString = jsonString.replace("[", "")
                .replace("]", "")
                .replace("\"", "");
        // Split the string by commas and trim any extra spaces
        String[] items = jsonString.split(",");
        ArrayList<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item.trim()); // Add trimmed strings to the list
        }
        return list;
    }

    public static ArrayList<String> ListFiles() {
        System.out.println("[LIST FILES]");
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/list-files");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + LoginRegisterPanel.token);
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(LoginRegisterPanel.username, StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(LoginRegisterPanel.token, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                // Ler a resposta JSON
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response += line.trim();
                    }
                    return convertToArrayList(response);
                }
            } else if (responseCode == 401) {
                ProgCircuito.ErrorBox("Erro: Autorização falhou. Token inválido ou expirado.");
                System.out.println("Erro: Autorização falhou. Token inválido ou expirado.");
            } else {
                ProgCircuito.ErrorBox("Erro: O servidor retornou o código " + responseCode);
                System.out.println("Erro: O servidor retornou o código " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendCircuit(String username, String filename) {
        System.out.println("[SEND CIRCUIT] Username: " + username + " | Filename: " + filename);
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/send-circuit");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + LoginRegisterPanel.token);
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(LoginRegisterPanel.username, StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(LoginRegisterPanel.token, StandardCharsets.UTF_8)
                    + "&filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8)
                    + "&destinatario=" + URLEncoder.encode(username, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                System.out.println("Circuito enviado com sucesso.");
                return "Circuito enviado com sucesso.";
            } else if (responseCode == 401) {
                ProgCircuito.ErrorBox("Erro: Autorização falhou. Token inválido ou expirado.");
                System.out.println("Erro: Autorização falhou. Token inválido ou expirado.");
                return "Erro: Autorização falhou. Token inválido ou expirado.";
            } else {
                ProgCircuito.ErrorBox("Erro: O servidor retornou o código " + responseCode);
                System.out.println("Erro: O servidor retornou o código " + responseCode);
                return "Erro: O servidor retornou o código " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }
    }

    public static ArrayList<String> ListSharedFiles() {
        System.out.println("[LIST SHARED FILES]");
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/list-shared-circuits");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + LoginRegisterPanel.token);
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(LoginRegisterPanel.username, StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(LoginRegisterPanel.token, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                // Ler a resposta JSON
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response += line.trim();
                    }
                    return convertToArrayList(response);
                }
            } else if (responseCode == 401) {
                System.out.println("Erro: Autorização falhou. Token inválido ou expirado.");
            } else {
                System.out.println("Erro: O servidor retornou o código " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void AcceptCircuit(String filename) {
        System.out.println("[ACCEPT CIRCUIT] Filename: " + filename);
        try {
            // URL do endpoint
            URL url = new URL(LoginRegisterPanel.DOMAIN + "/accept-circuit");

            // Abrir a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + LoginRegisterPanel.token);
            conn.setDoOutput(true);

            // Codificar os dados no formato x-www-form-urlencoded
            String urlEncodedData = "username=" + URLEncoder.encode(LoginRegisterPanel.username, StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(LoginRegisterPanel.token, StandardCharsets.UTF_8)
                    + "&filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8);

            // Enviar os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlEncodedData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                System.out.println("Circuito aceite com sucesso.");
            } else if (responseCode == 401) {
                System.out.println("Erro: Autorização falhou. Token inválido ou expirado.");
            } else {
                System.out.println("Erro: O servidor retornou o código " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
