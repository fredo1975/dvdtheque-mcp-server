package fr.bluechipit.dvdtheque_mcp_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class MCPController {
    private final ChatClient.Builder chatClientBuilder;
    private final InMemoryChatMemory chatMemory = new InMemoryChatMemory();

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String conversationId = request.getOrDefault("conversationId", "default");

        log.info("Traitement du message: {}", userMessage);

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory, conversationId, 10))
                .build();

        String systemPrompt = """
            Tu es un assistant IA spécialisé dans la gestion d'une DVDthèque.
            
            Tu as accès aux fonctions suivantes pour gérer la collection de DVDs:
            - getAllDVDs: récupère la liste complète des DVDs
            - getFilmByTtire: récupère les détails d'un FILM par son titre (paramètre: title)
           
            
            Instructions:
            1. Analyse la demande de l'utilisateur et détermine quelle fonction utiliser
            2. Appelle la ou les fonctions appropriées
            3. Réponds de manière naturelle et conviviale en français
            4. Si des informations manquent pour effectuer une action, demande-les à l'utilisateur
            5. Sois concis mais informatif
            
            Exemples de requêtes et actions:
            - "Montre-moi le film Inception" → getFilmByTtire avec title="Inception"
            """;

        try {
            String response = chatClient.prompt()
                    .system(systemPrompt)
                    .user(userMessage)
                    .call()
                    .content();

            log.info("Réponse générée: {}", response);

            return ResponseEntity.ok(Map.of(
                    "response", response,
                    "conversationId", conversationId
            ));
        } catch (Exception e) {
            log.error("Erreur lors du traitement du message", e);
            return ResponseEntity.ok(Map.of(
                    "response", "Désolé, une erreur s'est produite: " + e.getMessage(),
                    "conversationId", conversationId
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "ai", "Ollama",
                "model", "llama3.2"
        ));
    }

    @DeleteMapping("/conversation/{conversationId}")
    public ResponseEntity<Map<String, String>> clearConversation(@PathVariable String conversationId) {
        chatMemory.clear(conversationId);
        return ResponseEntity.ok(Map.of(
                "message", "Conversation effacée",
                "conversationId", conversationId
        ));
    }
}
