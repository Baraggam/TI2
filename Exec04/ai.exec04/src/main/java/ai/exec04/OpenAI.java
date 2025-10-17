package ai.exec04;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.azure.core.credential.AzureKeyCredential;

public final class OpenAI {
	public static void main(String[] args) {
		String apiKey = ""; // Para fazer upload no git, deletei a chave por segurança
		String endpoint = ""; // Para fazer upload no git, deletei o endpoint por segurança
		String deploymentName = "gpt-4o";

		// Ler pergunta do terminal
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite sua pergunta: ");
		String pergunta = scanner.nextLine();
		scanner.close();

		// Criar cliente
		OpenAIClient client = new OpenAIClientBuilder()
				.credential(new AzureKeyCredential(apiKey))
				.endpoint(endpoint)
				.buildClient();

		// Mensagens do chat
		List<ChatRequestMessage> chatMessages = Arrays.asList(
				new ChatRequestSystemMessage("Você é um assistente útil. Responda de forma sucinta."),
				new ChatRequestUserMessage(pergunta));

		// Configurações da requisição
		ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
		chatCompletionsOptions.setTemperature(0.7d);
		chatCompletionsOptions.setTopP(1d);

		// Obter resposta
		ChatCompletions chatCompletions = client.getChatCompletions(deploymentName, chatCompletionsOptions);

		// Exibir resposta
		for (ChatChoice choice : chatCompletions.getChoices()) {
			ChatResponseMessage message = choice.getMessage();
			System.out.println("Resposta:\n" + message.getContent());
		}
	}
}