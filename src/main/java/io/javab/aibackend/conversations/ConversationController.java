package io.javab.aibackend.conversations;

import io.javab.aibackend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;

    public ConversationController(ConversationRepository conversationRepository,ProfileRepository profileRepository) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
    }

    @PostMapping("/conversations")
    public Conversation createNewConversion(@RequestBody CreateConversionRequest request) {

        profileRepository.findById(request.profileId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Unable to find a profile " + request.profileId()));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                new ArrayList<>()
                );

        conversationRepository.save(conversation);
        return conversation;
    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversation(
            @PathVariable String conversationId
    ){
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Unable to find conversation" + conversationId));
    }

    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessagetoConversation(
            @PathVariable String conversationId, @RequestBody ChatMessage chatMessage) {

       Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Unable to find conversation" + conversationId));

       profileRepository.findById(chatMessage.authorId()).orElseThrow(()-> new ResponseStatusException(
               HttpStatus.NOT_FOUND,
               "Unable to find a profile with ID" + chatMessage.authorId()
       ));

       //TODO to validate author of message to be only the profile associated with the chat message

       ChatMessage messageWithTime = new ChatMessage(
               chatMessage.messageText(), chatMessage.authorId(), LocalDateTime.now() );

       conversation.messages().add(messageWithTime);
       conversationRepository.save(conversation);
        return conversation;
    }

    public record CreateConversionRequest(
            String profileId
    ){}
}
