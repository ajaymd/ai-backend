package io.javab.aibackend;

import io.javab.aibackend.conversations.ChatMessage;
import io.javab.aibackend.conversations.Conversation;
import io.javab.aibackend.conversations.ConversationRepository;
import io.javab.aibackend.profiles.Gender;
import io.javab.aibackend.profiles.Profile;
import io.javab.aibackend.profiles.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class AiBackendApplication implements CommandLineRunner {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public static void main(String[] args) {
        SpringApplication.run(AiBackendApplication.class, args);
    }

    public void run(String... args){
        System.out.println("My app is running");
        Profile profile = new Profile(
                "1",
                "Ajay",
                "Madkaiker",
                40,
                "Indian",
                Gender.MALE,
                "Software Developer",
                "foo.jpg",
                "INFP"

        );
        profileRepository.save(profile);
        profileRepository.findAll().forEach(System.out::println);

        Conversation conversation = new Conversation(
                "1",
                profile.id(),
                List.of(
                        new ChatMessage("Hello", profile.id(),LocalDateTime.now())
                ));

        conversationRepository.save(conversation);
        conversationRepository.findAll().forEach(System.out::println);
    }

}
