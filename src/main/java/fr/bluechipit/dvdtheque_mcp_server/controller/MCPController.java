package fr.bluechipit.dvdtheque_mcp_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/mcp")
public class MCPController {
    private final ChatClient chatClient;
    private final InMemoryChatMemory chatMemory = new InMemoryChatMemory();

    public MCPController(ChatClient.Builder chat, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chat.defaultTools(toolCallbackProvider)
                .build();
    }
    //@RolesAllowed("user")
    @PostMapping("/chat")
    public Mono<ResponseEntity<Map<String, String>>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String conversationId = request.getOrDefault("conversationId", "default");
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String username = auth.getName();

        //System.out.println("Message reçu de " + username + ": " + userMessage);
        //log.info("Traitement du message: {} reçu de {}", userMessage,username);
        log.info("Traitement du message: {}", userMessage);
        PromptTemplate pt = new PromptTemplate(userMessage);
        return Mono.fromCallable(() -> {
                    // --> remplacer par votre appel existant bloquant
                    // par ex: var resp = chatClient.call(...).content(...);
                    // ici on simule la construction de la réponse à partir du client
                    var response = this.chatClient.prompt(pt.create())
                            .call()
                            .content();
                    log.info(response);
                    // NE PAS faire resp.block() ici ; on retourne la représentation synchrone produite
                    return Map.of(
                            "response", response,
                            "conversationId", conversationId
                    );
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity.ok(Map.of(
                                        "response", "Désolé, une erreur s'est produite: " + ex.getMessage(),
                                        "conversationId", conversationId
                                )))
                        );

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
