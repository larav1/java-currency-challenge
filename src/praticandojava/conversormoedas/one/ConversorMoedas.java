package praticandojava.conversormoedas.one;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConversorMoedas {

    private static final String API_KEY = "8a17ce8ed5804b17f7423417";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;
        System.out.println("Seja bem-vindo/a ao Conversor de Moedas =]");

        while (opcao != 7) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Dólar --> Peso argentino");
            System.out.println("2. Peso argentino --> Dólar");
            System.out.println("3. Dólar --> Real brasileiro");
            System.out.println("4. Real brasileiro --> Dólar");
            System.out.println("5. Dólar --> Peso colombiano");
            System.out.println("6. Peso colombiano --> Dólar");
            System.out.println("7. Sair");

            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            if (opcao == 7) {
                System.out.println("Encerrando o programa. Até logo!");
                break;
            }

            System.out.print("Digite o valor para conversão: ");
            double valor = scanner.nextDouble();

            String moedaOrigem = "";
            String moedaDestino = "";

            switch (opcao) {
                case 1 -> { moedaOrigem = "USD"; moedaDestino = "ARS"; }
                case 2 -> { moedaOrigem = "ARS"; moedaDestino = "USD"; }
                case 3 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
                case 4 -> { moedaOrigem = "BRL"; moedaDestino = "USD"; }
                case 5 -> { moedaOrigem = "USD"; moedaDestino = "COP"; }
                case 6 -> { moedaOrigem = "COP"; moedaDestino = "USD"; }
                default -> {
                    System.out.println("Opção inválida, tente novamente.");
                    continue;
                }
            }

            double taxa = obterTaxaCambio(moedaOrigem, moedaDestino);
            if (taxa != -1) {
                double convertido = valor * taxa;
                System.out.printf("Resultado: %.2f %s = %.2f %s%n",
                        valor, moedaOrigem, convertido, moedaDestino);
            } else {
                System.out.println("Não foi possível obter a taxa de câmbio.");
            }
        }

        scanner.close();
    }

    public static double obterTaxaCambio(String moedaOrigem, String moedaDestino) {
        try {
            URL url = new URL(API_URL + moedaOrigem);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            reader.close();

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(resposta.toString(), JsonObject.class);

            JsonObject taxas = json.getAsJsonObject("conversion_rates");
            return taxas.get(moedaDestino).getAsDouble();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
