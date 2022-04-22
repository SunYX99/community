package community.service;

import community.entity.DiscussPost;
import community.mapper.DisscussPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DisscussPostMapper disscussPostMapper;

    public List<DiscussPost> findDisscussPosts(int userId, int offSet, int limit){
        return disscussPostMapper.selectDiscussPosts(userId,offSet,limit);
    }

    public int findDisscussPostRows(int userId){
        return disscussPostMapper.selectDiscussPostRows(userId);
    }

}
