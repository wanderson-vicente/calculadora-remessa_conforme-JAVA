import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ImpostoRemessaConforme extends JFrame {

    private JTextField valorProdutoField;
    private JTextField valorFreteField;
    private JTextField cotacaoDolarField;
    private JTextArea resultadoArea;

    public ImpostoRemessaConforme() {
        createUI();
    }

    private void createUI() {
        setTitle("Calculadora de Impostos Remessa Conforme");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Container com a imagem
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(400, 400));
        imagePanel.setBackground(Color.WHITE);

        // Carregar a imagem usando o ClassLoader
        URL imageUrl = getClass().getClassLoader().getResource("logo.png");
        if (imageUrl != null) {
            JLabel imageLabel = new JLabel(new ImageIcon(imageUrl));
            imagePanel.add(imageLabel);
        } else {
            imagePanel.add(new JLabel("Imagem não encontrada"));
        }

        // Container para os campos de entrada e resultado
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Calculadora de Impostos Remessa Conforme");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel valorProdutoLabel = new JLabel("Preço do produto (em R$):");
        valorProdutoField = new JTextField();

        JLabel valorFreteLabel = new JLabel("Valor do frete (em R$):");
        valorFreteField = new JTextField();

        JLabel cotacaoDolarLabel = new JLabel("Cotação do dólar (em R$):");
        cotacaoDolarField = new JTextField();

        inputPanel.add(valorProdutoLabel);
        inputPanel.add(valorProdutoField);
        inputPanel.add(valorFreteLabel);
        inputPanel.add(valorFreteField);
        inputPanel.add(cotacaoDolarLabel);
        inputPanel.add(cotacaoDolarField);

        JButton calcularButton = new JButton("Calcular");
        calcularButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcularButton.addActionListener(new CalcularImpostosAction());

        JButton limparButton = new JButton("Limpar");
        limparButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        limparButton.addActionListener(new LimparCamposAction());

        JButton sairButton = new JButton("Sair");
        sairButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sairButton.addActionListener(e -> System.exit(0));

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setBorder(BorderFactory.createTitledBorder("Resultado"));

        container.add(titleLabel);
        container.add(Box.createVerticalStrut(20));
        container.add(inputPanel);
        container.add(Box.createVerticalStrut(20));
        container.add(calcularButton);
        container.add(Box.createVerticalStrut(10));
        container.add(limparButton);
        container.add(Box.createVerticalStrut(10));
        container.add(sairButton);
        container.add(Box.createVerticalStrut(20));
        container.add(new JScrollPane(resultadoArea));

        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(container, BorderLayout.CENTER);

        add(mainPanel);
    }

    private class CalcularImpostosAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            calcularImpostos();
        }
    }

    private void calcularImpostos() {
        try {
            double valorProdutoBRL = Double.parseDouble(valorProdutoField.getText());
            double valorFreteBRL = Double.parseDouble(valorFreteField.getText());
            double brlToUsd = Double.parseDouble(cotacaoDolarField.getText());

            double valorTotalBRL = valorProdutoBRL + valorFreteBRL;
            double valorTotalUSD = valorTotalBRL / brlToUsd;

            double valorFinalUSD, total;
            int aliquota;

            if (valorTotalUSD <= 50) {
                aliquota = 20;
                valorFinalUSD = valorTotalUSD * 1.20;
                total = valorFinalUSD / 0.83;
            } else {
                aliquota = 60;
                valorFinalUSD = valorTotalUSD * 1.60;
                total = valorFinalUSD / 0.83;
            }

            String resultado = String.format("Valor total em dólar: US$ %.2f\n", valorTotalUSD);
            resultado += String.format("Cotação do dólar: R$ %.2f\n", brlToUsd);
            resultado += String.format("Com isso, a alíquota de Imposto de Importação a ser paga é de: %d%%\n", aliquota);
            resultado += String.format("Total a pagar em reais: R$ %.2f\n", total * brlToUsd);

            resultadoArea.setText(resultado);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores válidos para o produto, frete e cotação do dólar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class LimparCamposAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            valorProdutoField.setText("");
            valorFreteField.setText("");
            cotacaoDolarField.setText("");
            resultadoArea.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImpostoRemessaConforme app = new ImpostoRemessaConforme();
            app.setVisible(true);
        });
    }
}
