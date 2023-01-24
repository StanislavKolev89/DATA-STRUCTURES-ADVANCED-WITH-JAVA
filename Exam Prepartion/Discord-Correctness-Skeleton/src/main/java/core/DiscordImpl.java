package core;

import models.Message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscordImpl implements Discord {

    private final Map<String, Message> allMessages;
    private final Map<String, List<Message>> messagesByChanel;

    public DiscordImpl() {
        this.allMessages = new LinkedHashMap<>();
        this.messagesByChanel = new LinkedHashMap<>();

    }

    @Override
    public void sendMessage(Message message) {
        this.allMessages.put(message.getId(), message);
        this.messagesByChanel.putIfAbsent(message.getChannel(), new ArrayList<>());
        this.messagesByChanel.get(message.getChannel()).add(message);

    }

    @Override
    public boolean contains(Message message) {
        return this.allMessages.containsKey(message.getId());
    }

    @Override
    public int size() {
        return this.allMessages.size();
    }

    @Override
    public Message getMessage(String messageId) {
        Message message = this.allMessages.get(messageId);

        if (message == null) {
            throw new IllegalArgumentException();
        }
        return message;
    }

    @Override
    public void deleteMessage(String messageId) {
        Message msgToRemove = this.allMessages.remove(messageId);

        if (msgToRemove == null) {
            throw new IllegalArgumentException();
        }

        this.messagesByChanel.get(msgToRemove.getChannel()).remove(msgToRemove);

    }

    @Override
    public void reactToMessage(String messageId, String reaction) {
        Message message = this.getMessage(messageId);

        message.getReactions().add(reaction);


    }

    @Override
    public Iterable<Message> getChannelMessages(String channel) {

        if (!this.messagesByChanel.containsKey(channel)) {
            throw new IllegalArgumentException();
        }

        List<Message> messages = this.messagesByChanel.get(channel);

        if (messages.isEmpty() ) {
            throw new IllegalStateException();
        }
        return messages;
    }

    @Override
    public Iterable<Message> getMessagesByReactions(List<String> reactions) {
        return this.allMessages.values().stream()
                .filter(m -> m.getReactions().containsAll(reactions))
                .sorted((f, s) -> {
                    if (f.getReactions().size() != s.getReactions().size()) {
                        return s.getReactions().size() - f.getReactions().size();
                    }
                    return f.getTimestamp() - s.getTimestamp();
                }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getMessageInTimeRange(Integer lowerBound, Integer upperBound) {

        return this.allMessages.values().stream()
                .filter(m -> m.getTimestamp() >= lowerBound && m.getTimestamp() <= upperBound)
                .sorted((f, s) -> {
                    int fChanelCountOfMessages = this.messagesByChanel.get(f.getChannel()).size();
                    int sChanelCountOfMessages = this.messagesByChanel.get(s.getChannel()).size();
                    return sChanelCountOfMessages - fChanelCountOfMessages;
                }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getTop3MostReactedMessages() {
        return this.allMessages.values().stream()
                .sorted((f, s) -> Integer.compare(s.getReactions().size(), f.getReactions().size()))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getAllMessagesOrderedByCountOfReactionsThenByTimestampThenByLengthOfContent() {
        return this.allMessages.values().stream()
                .sorted((f, s) -> {
                    if (f.getReactions().size() != s.getReactions().size()) {
                        return s.getReactions().size() - f.getReactions().size();
                    }
                    if (f.getTimestamp() != s.getTimestamp()) {
                        return f.getTimestamp() - s.getTimestamp();
                    }

                    return f.getContent().length() - s.getContent().length();
                }).collect(Collectors.toList());

    }
}
