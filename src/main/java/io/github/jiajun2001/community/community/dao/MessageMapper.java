package io.github.jiajun2001.community.community.dao;

import io.github.jiajun2001.community.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    // Get the latest message for each conversation of the user
    List<Message> selectConversations(int userId, int offset, int limit);

    // Get the number of conversations of the user
    int selectConversationCount(int userId);

    // Get all messages of a particular conversation
    List<Message> selectMessages(String conversationId, int offset, int limit);

    // Get the number of messages of a particular conversation
    int selectMessageCount(String conversationId);

    // Get the number of unread messages (from a particular conversation or all conversations)
    int selectMessageUnreadCount(int userId, String conversationId);

    // Insert a message
    int insertMessage(Message message);

    // Update message status (unread -> read)
    int updateStatus(List<Integer> ids, int status);

    int deleteMessage(int messageId);

    // Get the latest notification for one topic
    Message selectLatestNotice(int userId, String topic);

    // Get the number of notifications for one topic
    int selectNoticeCount(int userId, String topic);

    // Get the number of unread notifications for one topic
    int selectNoticeUnreadCount(int userId, String topic);
}
