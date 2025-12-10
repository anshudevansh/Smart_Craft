package com.email.SmartCraft.service;

import com.email.SmartCraft.Model.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class emailGeneratorService {

        private final WebClient webClient;

        @Value("${api.url}")
        private String apiUrl;

        @Value("${api.key}")
        private String apiKey;

        public String generateEmailReply(EmailRequest emailRequest) {
                String prompt = buildPrompt(emailRequest);

                Map<String, Object> contentPart = Map.of("parts", List.of(Map.of("text", prompt)));

                // Build request body with optional generation parameters
                Map<String, Object> requestBody;

                if (hasGenerationParameters(emailRequest)) {
                        Map<String, Object> generationConfig = buildGenerationConfig(emailRequest);
                        requestBody = Map.of(
                                        "contents", List.of(contentPart),
                                        "generationConfig", generationConfig);
                } else {
                        requestBody = Map.of("contents", List.of(contentPart));
                }

                String fullUrl = UriComponentsBuilder
                                .fromHttpUrl(apiUrl)
                                .queryParam("key", apiKey)
                                .toUriString();

                String response = webClient.post().uri(fullUrl)
                                .header("Content-Type", "application/json")
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

                String raw = extractResponseContent(response);
                return postProcessReply(raw, emailRequest);
        }

        public String generateAlternateEmailReply(EmailRequest emailRequest) {
                // Build alternate instructions from length/tone preferences in the request
                StringBuilder altInstruction = new StringBuilder();

                // Add custom alternate instruction if provided
                if (emailRequest.getAlternateInstruction() != null
                                && !emailRequest.getAlternateInstruction().isEmpty()) {
                        altInstruction.append(emailRequest.getAlternateInstruction()).append(" ");
                } else {
                        // Default alternate behavior: make it more concise
                        altInstruction.append("Provide a shorter, more concise version of the reply. ");
                }

                if (emailRequest.getLengthPreference() != null) {
                        switch (emailRequest.getLengthPreference().toLowerCase()) {
                                case "one-word":
                                        altInstruction.append("Reply in exactly one word. ");
                                        break;
                                case "short":
                                        altInstruction.append("Keep the reply to one or two short sentences. ");
                                        break;
                                case "concise":
                                        altInstruction.append("Give a concise reply (<= 20 words). ");
                                        break;
                                default:
                                        altInstruction.append(emailRequest.getLengthPreference()).append(" ");
                        }
                }

                if (emailRequest.getMaxWords() != null) {
                        altInstruction.append("Limit the reply to at most ")
                                        .append(emailRequest.getMaxWords())
                                        .append(" words. ");
                }

                // Temporarily modify the tone for alternate response if specified
                String originalTone = emailRequest.getTone();
                if (emailRequest.getAlternateTone() != null && !emailRequest.getAlternateTone().isEmpty()) {
                        emailRequest.setTone(emailRequest.getAlternateTone());
                }

                // Prepend the alt instructions to the prompt
                String basePrompt = buildPrompt(emailRequest);
                String promptWithAlt = altInstruction.toString() + basePrompt;

                // Restore original tone
                emailRequest.setTone(originalTone);

                Map<String, Object> contentPart = Map.of("parts", List.of(Map.of("text", promptWithAlt)));

                // Build request body with optional generation parameters
                Map<String, Object> requestBody;

                if (hasGenerationParameters(emailRequest)) {
                        Map<String, Object> generationConfig = buildGenerationConfig(emailRequest);
                        requestBody = Map.of(
                                        "contents", List.of(contentPart),
                                        "generationConfig", generationConfig);
                } else {
                        requestBody = Map.of("contents", List.of(contentPart));
                }

                String fullUrl = UriComponentsBuilder
                                .fromHttpUrl(apiUrl)
                                .queryParam("key", apiKey)
                                .toUriString();

                String response = webClient.post().uri(fullUrl)
                                .header("Content-Type", "application/json")
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

                String raw = extractResponseContent(response);
                return postProcessReply(raw, emailRequest) + "\n\n(Alternate suggestion)";
        }

        /**
         * NEW METHOD: Generate email reply based on user's custom query/instruction
         * This method incorporates the user's specific requirements into the prompt
         * 
         * @param emailRequest Request containing email content and user query
         * @return Generated email content based on user query
         */
        public String generateEmailWithQuery(EmailRequest emailRequest) {
                // Build prompt with user query incorporated
                StringBuilder prompt = new StringBuilder();
                
                // Add user's custom instruction first
                if (emailRequest.getUserQuery() != null && !emailRequest.getUserQuery().trim().isEmpty()) {
                        prompt.append(emailRequest.getUserQuery()).append("\n\n");
                }
                
                // Add base email generation instruction
                prompt.append("Generate an email reply for the following email content. ");
                
                // Add tone if specified
                if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
                        prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
                }
                
                // Add the original email content
                prompt.append("\nOriginal Email Content: ").append(emailRequest.getEmailContent());

                Map<String, Object> contentPart = Map.of("parts", List.of(Map.of("text", prompt.toString())));

                // Build request body with optional generation parameters
                Map<String, Object> requestBody;

                if (hasGenerationParameters(emailRequest)) {
                        Map<String, Object> generationConfig = buildGenerationConfig(emailRequest);
                        requestBody = Map.of(
                                        "contents", List.of(contentPart),
                                        "generationConfig", generationConfig);
                } else {
                        requestBody = Map.of("contents", List.of(contentPart));
                }

                String fullUrl = UriComponentsBuilder
                                .fromHttpUrl(apiUrl)
                                .queryParam("key", apiKey)
                                .toUriString();

                String response = webClient.post().uri(fullUrl)
                                .header("Content-Type", "application/json")
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

                String raw = extractResponseContent(response);
                return postProcessReply(raw, emailRequest);
        }

        /**
         * Checks if the email request contains any generation parameters
         */
        private boolean hasGenerationParameters(EmailRequest emailRequest) {
                return emailRequest.getTemperature() != null
                                || emailRequest.getMaxOutputTokens() != null
                                || emailRequest.getTopP() != null
                                || emailRequest.getTopK() != null;
        }

        /**
         * Builds the generation config map with all available parameters
         */
        private Map<String, Object> buildGenerationConfig(EmailRequest emailRequest) {
                Map<String, Object> config = new HashMap<>();

                if (emailRequest.getTemperature() != null) {
                        config.put("temperature", emailRequest.getTemperature());
                }
                if (emailRequest.getMaxOutputTokens() != null) {
                        config.put("maxOutputTokens", emailRequest.getMaxOutputTokens());
                }
                if (emailRequest.getTopP() != null) {
                        config.put("topP", emailRequest.getTopP());
                }
                if (emailRequest.getTopK() != null) {
                        config.put("topK", emailRequest.getTopK());
                }

                return config;
        }

        /**
         * Extracts the model's textual content from the raw JSON response returned by
         * the API.
         * Adjust the JSON path if your API response shape differs.
         */
        private String extractResponseContent(String response) {
                if (response == null || response.isEmpty()) {
                        return "No response from API";
                }

                try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode rootNode = mapper.readTree(response);

                        // The JSON path: rootNode.candidates[0].content.parts[0].text
                        JsonNode candidates = rootNode.path("candidates");
                        if (candidates.isArray() && candidates.size() > 0) {
                                JsonNode contentNode = candidates.get(0).path("content");
                                JsonNode parts = contentNode.path("parts");
                                if (parts.isArray() && parts.size() > 0) {
                                        return parts.get(0).path("text").asText();
                                }
                        }

                        // Fallback: try a common alternative path
                        JsonNode text = rootNode.path("text");
                        if (!text.isMissingNode()) {
                                return text.asText();
                        }

                        return "Could not parse response content";
                } catch (Exception e) {
                        return "Error processing request: " + e.getMessage();
                }
        }

        /**
         * Builds the prompt for the email generation
         */
        private String buildPrompt(EmailRequest emailRequest) {
                StringBuilder prompt = new StringBuilder();
                prompt.append("Generate an email reply for the following email content. Do not generate a subject line. ");
                if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
                        prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
                }
                prompt.append("\nOriginal Email Content: ").append(emailRequest.getEmailContent());
                return prompt.toString();
        }

        /**
         * Post-processes the raw reply from the model according to fields in
         * EmailRequest.
         * Only applies strict formatting if strictLengthEnforcement is enabled.
         */
        private String postProcessReply(String rawReply, EmailRequest req) {
                if (rawReply == null || rawReply.isEmpty()) {
                        return "";
                }

                // If strict enforcement is not enabled, return with minimal processing
                if (req.getStrictLengthEnforcement() == null || !req.getStrictLengthEnforcement()) {
                        // Just trim and normalize excessive whitespace while preserving structure
                        return rawReply.trim()
                                        .replaceAll("[ \\t]+", " ") // normalize spaces/tabs
                                        .replaceAll("\\n{3,}", "\n\n"); // max 2 consecutive newlines
                }

                // Strict enforcement mode - apply aggressive processing
                String reply = rawReply.trim().replaceAll("\\s+", " ");
                if (reply.isEmpty()) {
                        return reply;
                }

                // Helper: limit to N words
                java.util.function.Function<Integer, String> limitToWords = (limit) -> {
                        if (limit == null || limit <= 0) {
                                return reply;
                        }
                        String[] words = reply.split("\\s+");
                        if (words.length <= limit) {
                                return reply;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < limit; i++) {
                                if (i > 0) {
                                        sb.append(" ");
                                }
                                sb.append(words[i]);
                        }
                        return sb.toString();
                };

                // Enforce explicit lengthPreference
                if (req.getLengthPreference() != null) {
                        String pref = req.getLengthPreference().trim().toLowerCase();
                        switch (pref) {
                                case "one-word":
                                case "one word":
                                        String first = reply.split("\\s+")[0];
                                        first = first.replaceAll("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$", "");
                                        return first.isEmpty() ? "" : first;

                                case "short":
                                        String[] sentences = reply.split("(?<=[.!?])\\s+");
                                        StringBuilder sb = new StringBuilder();
                                        int count = 0;
                                        for (String s : sentences) {
                                                String trimmed = s.trim();
                                                if (trimmed.isEmpty()) {
                                                        continue;
                                                }
                                                if (sb.length() > 0) {
                                                        sb.append(" ");
                                                }
                                                sb.append(trimmed);
                                                count++;
                                                if (count >= 2) {
                                                        break;
                                                }
                                        }
                                        String shortResult = sb.toString();
                                        if (shortResult.isEmpty()) {
                                                shortResult = limitToWords.apply(20);
                                        }
                                        return shortResult;

                                case "concise":
                                        return limitToWords.apply(20);

                                default:
                                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)")
                                                        .matcher(pref);
                                        if (m.find()) {
                                                try {
                                                        int n = Integer.parseInt(m.group(1));
                                                        return limitToWords.apply(n);
                                                } catch (NumberFormatException ignored) {
                                                }
                                        }
                                        break;
                        }
                }

                // Enforce maxWords if set
                if (req.getMaxWords() != null && req.getMaxWords() > 0) {
                        return limitToWords.apply(req.getMaxWords());
                }

                return reply;
        }
}
