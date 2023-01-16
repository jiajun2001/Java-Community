package io.github.jiajun2001.community.community.controller;

import io.github.jiajun2001.community.community.entity.Message;
import io.github.jiajun2001.community.community.entity.Page;
import io.github.jiajun2001.community.community.entity.User;
import io.github.jiajun2001.community.community.service.MessageService;
import io.github.jiajun2001.community.community.service.UserService;
import io.github.jiajun2001.community.community.util.CommunityUtil;
import io.github.jiajun2001.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/message/list", method = RequestMethod.GET)
    public String getMessageList(Model model, Page page) {
        // Get the user
        User user = hostHolder.getUser();

        // Set page information
        page.setLimit(5);
        page.setPath("/message/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        // Get the conversation
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("messageCount", messageService.findMessageCount(message.getConversationId()));
                map.put("unreadCount", messageService.findMessageUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));
                conversations.add(map);
            }
            model.addAttribute("conversations", conversations);
        }

        // Get the number of unread message
        int messageUnreadCount = messageService.findMessageUnreadCount(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);

        return "/site/letter";
    }

    @RequestMapping(path = "/message/detail/{conversationId}", method = RequestMethod.GET)
    public String getMessageDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // Set the page information
        page.setLimit(5);
        page.setPath("/message/detail/" + conversationId);
        page.setRows(messageService.findMessageCount(conversationId));

        List<Message> messageList = messageService.findMessages(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messages = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messageList) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                messages.add(map);
            }
        }
        model.addAttribute("messages", messages);

        // Find the target of the conversation
        model.addAttribute("target", getMessageTarget(conversationId));

        // Set the status of messages to be 'read'
        List<Integer> ids = getMessageIds(messageList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";
    }

    private List<Integer> getMessageIds(List<Message> messageList) {
        List<Integer> ids = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messageList) {
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    private User getMessageTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);
        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    @RequestMapping(path = "/message/send", method = RequestMethod.POST)
    @ResponseBody
    public String sentMessage(String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "The user does not exist!");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }

    @RequestMapping(path = "/message/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMessage(String messageId) {
        messageService.deleteMessage(Integer.parseInt(messageId));
        return CommunityUtil.getJSONString(0);
    }
}

