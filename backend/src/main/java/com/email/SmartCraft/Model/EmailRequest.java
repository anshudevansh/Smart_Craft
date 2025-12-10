package com.email.SmartCraft.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    // Core email fields
    private String subject;
    private String emailContent;
    private String tone;
    private boolean variant;

    // Length control fields
    private String lengthPreference; // "one-word", "short", "concise"
    private Integer maxWords;

    // Alternate response customization
    private String alternateInstruction; // Custom instruction for alternate response
    private String alternateTone; // Different tone for alternate (e.g., "more casual", "formal")

    // API generation parameters
    private Double temperature; // 0.0 to 2.0 (creativity)
    private Integer maxOutputTokens; // max tokens in response
    private Double topP; // 0.0 to 1.0 (nucleus sampling)
    private Integer topK; // positive integer (top-k sampling)

    // Post-processing control
    private Boolean strictLengthEnforcement; // default false

    // User query for custom instructions
    private String userQuery; // Custom user instruction/query for email generation

    // Custom constructor for basic fields
    public EmailRequest(String subject, String emailContent, String tone) {
        this.subject = subject;
        this.emailContent = emailContent;
        this.tone = tone;
    }
}