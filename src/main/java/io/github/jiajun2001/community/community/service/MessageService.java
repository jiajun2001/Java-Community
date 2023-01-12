package io.github.jiajun2001.community.community.service;

import io.github.jiajun2001.community.community.dao.MessageMapper;
import io.github.jiajun2001.community.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findMessages(String conversationId, int offset, int limit) {
        return messageMapper.selectMessages(conversationId, offset, limit);
    }

    public int findMessageCount(String conversationId) {
        return messageMapper.selectMessageCount(conversationId);
    }

    public int findMessageUnreadCount(int userId, String conversationId) {
        return messageMapper.selectMessageUnreadCount(userId, conversationId);
    }
}
