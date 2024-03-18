package com.jiawei.service.impl;
import com.jiawei.domain.entity.Tag;
import com.jiawei.mapper.TagMapper;
import com.jiawei.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-03-18 19:33:40
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
