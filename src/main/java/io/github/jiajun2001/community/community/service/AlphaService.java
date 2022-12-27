package io.github.jiajun2001.community.community.service;

import io.github.jiajun2001.community.community.dao.AlphaDao;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton") // can be changed by "prototype"
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("Construct AlphaService");
    }

    @PostConstruct
    public void init() {
        System.out.println("Initialize AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Destroy AlphaService");
    }
}
