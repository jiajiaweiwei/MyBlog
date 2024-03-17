package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.BlogCategory;
import generator.service.BlogCategoryService;
import generator.mapper.BlogCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘佳伟
* @description 针对表【blog_category(分类表)】的数据库操作Service实现
* @createDate 2024-03-10 23:27:46
*/
@Service
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory>
    implements BlogCategoryService{

}




