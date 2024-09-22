import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static String[] subestacoes = {"Planalto", "Aurora", "Litoral", "Horizonte", "Progresso"};
    static String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
    static int[][] consumo = new int[5][12];

    public static void main(String[] args) {
        try {
            lerArquivoCSV("Consumo//consumos_20.csv");
            gerarRelatorio("resultados.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para ler o arquivo CSV e preencher a matriz de consumo
    public static void lerArquivoCSV(String nomeArquivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));
        String linha;

        reader.readLine();

        while ((linha = reader.readLine()) != null) {
            String[] dados = linha.split(",");
            String mes = dados[0].trim();
            String subestacao = dados[1].trim();
            int consumoMes = Integer.parseInt(dados[2].trim());

            int indiceSubestacao = getIndiceSubestacao(subestacao);
            int indiceMes = getIndiceMes(mes);

            if (indiceSubestacao != -1 && indiceMes != -1) {
                consumo[indiceSubestacao][indiceMes] += consumoMes; // Somar consumo
            }
        }
        reader.close();
    }

    public static int getIndiceSubestacao(String subestacao) {
        for (int i = 0; i < subestacoes.length; i++) {
            if (subestacoes[i].equals(subestacao)) {
                return i;
            }
        }
        return -1;
    }


    public static int getIndiceMes(String mes) {
        for (int i = 0; i < meses.length; i++) {
            if (mes.contains(meses[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void gerarRelatorio(String nomeArquivo) throws IOException {
        FileWriter writer = new FileWriter(nomeArquivo);
        int cf = 10;
        writer.write("Matriz de Consumo por Subestação\n");
        writer.write(String.format("%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" +
                                   cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s%-" + cf + "s\n",
        " ", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"));
            
        for (int i = 0; i < 5; i++) {
            writer.write(String.format("%-" + cf + "s", subestacoes[i]));
            for (int j = 0; j < 12; j++) {
                writer.write(String.format("%-" + cf + "d", consumo[i][j]));
            }
            writer.write("\n"); 
        }

        int maiorConsumo = 0, menorConsumo = Integer.MAX_VALUE;
        String maiorSubestacao = "", menorSubestacao = "";
        String maiorMes = "", menorMes = "";

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                if (consumo[i][j] > maiorConsumo) {
                    maiorConsumo = consumo[i][j];
                    maiorSubestacao = subestacoes[i];
                    maiorMes = meses[j];
                }
                if (consumo[i][j] < menorConsumo && consumo[i][j] > 0) {
                    menorConsumo = consumo[i][j];
                    menorSubestacao = subestacoes[i];
                    menorMes = meses[j];
                }
            }
        }

        writer.write("\nSubestação com maior consumo mensal\n" + maiorSubestacao + " - " + maiorMes + "\n");
        writer.write("Subestação com menor consumo mensal\n" + menorSubestacao + " - " + menorMes + "\n");

        int totalConsumo = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                totalConsumo += consumo[i][j];
            }
        }
        writer.write("\nTotal geral de consumo: " + totalConsumo + "\n");

        writer.write("\nMédia de consumo mensal por subestação\n");
        for (int i = 0; i < 5; i++) {
            double totalSubestacao = 0;
            for (int j = 0; j < 12; j++) {
                totalSubestacao += consumo[i][j];
            }
            writer.write(subestacoes[i] + ": " + mostrarMedia(totalSubestacao / 12.0) + "\n");
        }

        writer.write("\nMeses e total de energia consumida ordenada por consumo (decrescente)\n");
        int[] totalMeses = new int[12];
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i < 5; i++) {
                totalMeses[j] += consumo[i][j];
            }
        }

        for (int i = 0; i < 12; i++) {
            for (int j = i + 1; j < 12; j++) {
                if (totalMeses[i] < totalMeses[j]) {
                    int temp = totalMeses[i];
                    totalMeses[i] = totalMeses[j];
                    totalMeses[j] = temp;

                    String tempMes = meses[i];
                    meses[i] = meses[j];
                    meses[j] = tempMes;
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            writer.write(meses[i] + ": " + totalMeses[i] + "\n");
        }

        writer.close();
    }

    public static String mostrarMedia(double totalSubestacao){
        return String.format("%.2f", totalSubestacao);
    }
}

