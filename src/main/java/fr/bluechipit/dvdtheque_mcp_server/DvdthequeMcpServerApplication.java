package fr.bluechipit.dvdtheque_mcp_server;

import fr.bluechipit.dvdtheque_mcp_server.service.DVDthequeService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DvdthequeMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DvdthequeMcpServerApplication.class, args);
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
	@Bean
	public ToolCallbackProvider tools(DVDthequeService dVDthequeService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(dVDthequeService)
				.build();
	}
}