package tolo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;

public class Principal {

    private static JButton[] botoes = new JButton[9];

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            criarJanela();
        });
    }

    public static void criarJanela() {
        JFrame janela = new JFrame("Jogo da Velha");
        janela.setSize(400, 400);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel tabuleiro = criarTabuleiro();
        janela.add(tabuleiro);

        janela.setVisible(true);
    }

    public static JPanel criarTabuleiro() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            JButton botao = new JButton();
            botoes[i] = botao;
            painel.add(botao);

            int indice = i;
            botao.addActionListener(e -> {
                if (botao.getText().isEmpty()) {
                    botao.setText("X");

                    if (checarFimDeJogo())
                        return;

                    
                    int melhorMovimento = melhorJogada();
                    if (melhorMovimento != -1) {
                        botoes[melhorMovimento].setText("O");
                        checarFimDeJogo();
                    }
                }
            });
        }

        return painel;
    }

    private static boolean checarFimDeJogo() {
        char vencedor = verificarVencedor();
        if (vencedor == 'X' || vencedor == 'O') {
            JOptionPane.showMessageDialog(null, "Vitória de " + vencedor + "!");
            bloquearBotoes();
            return true;
        }
        if (tabuleiroCheio()) {
            JOptionPane.showMessageDialog(null, "Empate!");
            bloquearBotoes();
            return true;
        }
        return false;
    }

    private static void bloquearBotoes() {
        for (JButton b : botoes) {
            b.setEnabled(false);
        }
    }

    private static boolean tabuleiroCheio() {
        for (JButton b : botoes) {
            if (b.getText().isEmpty())
                return false;
        }
        return true;
    }

    private static char verificarVencedor() {
        int[][] combinacoes = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };

        for (int[] c : combinacoes) {
            String t1 = botoes[c[0]].getText();
            String t2 = botoes[c[1]].getText();
            String t3 = botoes[c[2]].getText();
            if (!t1.isEmpty() && t1.equals(t2) && t2.equals(t3)) {
                return t1.charAt(0);
            }
        }
        return ' ';
    }

    private static int melhorJogada() {
        int melhorValor = Integer.MIN_VALUE;
        int movimento = -1;

        char[] board = getBoard();

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = 'O';
                int valor = minimax(board, false);
                board[i] = ' ';
                if (valor > melhorValor) {
                    melhorValor = valor;
                    movimento = i;
                }
            }
        }
        return movimento;
    }

    private static int minimax(char[] board, boolean maximizando) {
        char vencedor = checkWinner(board);
        if (vencedor == 'O')
            return 10;
        if (vencedor == 'X')
            return -10;
        if (estaCheio(board))
            return 0;

        if (maximizando) {
            int melhorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == ' ') {
                    board[i] = 'O';
                    int valor = minimax(board, false);
                    board[i] = ' ';
                    melhorValor = Math.max(melhorValor, valor);
                }
            }
            return melhorValor;
        } else {
            int melhorValor = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == ' ') {
                    board[i] = 'X';
                    int valor = minimax(board, true);
                    board[i] = ' ';
                    melhorValor = Math.min(melhorValor, valor);
                }
            }
            return melhorValor;
        }
    }

    private static char[] getBoard() {
        char[] board = new char[9];
        for (int i = 0; i < 9; i++) {
            String t = botoes[i].getText();
            if (t.equals("X"))
                board[i] = 'X';
            else if (t.equals("O"))
                board[i] = 'O';
            else
                board[i] = ' ';
        }
        return board;
    }

    private static boolean estaCheio(char[] board) {
        for (char c : board) {
            if (c == ' ')
                return false;
        }
        return true;
    }

    private static char checkWinner(char[] board) {
        int[][] combinacoes = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };

        for (int[] c : combinacoes) {
            char t1 = board[c[0]];
            char t2 = board[c[1]];
            char t3 = board[c[2]];
            if (t1 != ' ' && t1 == t2 && t2 == t3) {
                return t1;
            }
        }
        return ' ';
    }
}
