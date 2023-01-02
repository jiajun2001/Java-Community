package io.github.jiajun2001.community.community.controller;

import io.github.jiajun2001.community.community.entity.DiscussPost;
import io.github.jiajun2001.community.community.entity.User;
import io.github.jiajun2001.community.community.service.DiscussPostService;
import io.github.jiajun2001.community.community.util.CommunityUtil;
import io.github.jiajun2001.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        System.out.println(title);
        System.out.println(content);
        System.out.println("+++++++++++++++++++++++++");
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "Please login!");
        }
        // Create a post entity
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        return CommunityUtil.getJSONString(0, "Success in posting!");
    }
}
