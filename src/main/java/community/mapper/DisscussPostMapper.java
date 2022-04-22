package community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisscussPostMapper extends BaseMapper<DiscussPost> {
    //userId默认为0，表示查询所有，当userId不为0，为用户个人主页查询我的帖子
    List<DiscussPost> selectDiscussPosts(int userId, int offSet, int limit);

    //查询帖子总数 如果在sql中需要用到动态条件并且方法只有一个参数，一定需要@Param
    int selectDiscussPostRows(@Param("userId") int userId);
}
