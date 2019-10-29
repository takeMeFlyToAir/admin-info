package com.ssd.admin.business.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.StatisticBaseInfoEntity;
import com.ssd.admin.business.mapper.StatisticBaseInfoMapper;
import com.ssd.admin.business.qo.StatisticBaseInfoQO;
import com.ssd.admin.business.service.StatisticBaseInfoService;
import com.ssd.admin.common.BaseService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class StatisticBaseInfoServiceImpl extends BaseService<StatisticBaseInfoEntity> implements StatisticBaseInfoService {


    @Autowired
    private StatisticBaseInfoMapper statisticBaseInfoMapper;

    @Override
    public PagerResultForDT<StatisticBaseInfoEntity> selectPage(PagerForDT<StatisticBaseInfoQO> pager) {
        Example example = new Example(StatisticBaseInfoEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<StatisticBaseInfoEntity> allList = this.selectByExample(example);

        StatisticBaseInfoQO statisticBaseInfoQO = pager.getCondition();
        example = new Example(StatisticBaseInfoEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        example.setOrderByClause("id desc");

        if(statisticBaseInfoQO.getYear() != null){
            criteria.andEqualTo("year", statisticBaseInfoQO.getYear());
        }
        if(statisticBaseInfoQO.getSubject() != null){
            criteria.andEqualTo("subject", statisticBaseInfoQO.getSubject());
        }
        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<StatisticBaseInfoEntity> statisticBaseInfoEntityList = this.selectByExample(example);
        PageInfo<StatisticBaseInfoEntity> pageInfo = new PageInfo<>(statisticBaseInfoEntityList);
        PagerResultForDT<StatisticBaseInfoEntity> pagerResult = new PagerResultForDT<StatisticBaseInfoEntity>(statisticBaseInfoEntityList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }

    @Override
    public Boolean validate(Integer id, Integer year, Integer subject) {
        if(id == null){
            return validate(year, subject);
        }else {
            StatisticBaseInfoEntity statisticBaseInfoEntity = statisticBaseInfoMapper.selectByPrimaryKey(id);
            // id不为空代表修改操作，如果转入的code为原来的code，则返回true
            if (statisticBaseInfoEntity.getYear().toString().equals(year.toString()) && statisticBaseInfoEntity.getSubject().toString().equals(subject.toString())) {
                return true;
            } else { // 如果修改了名字，还是要对名字进行校验
                return validate(year, subject);
            }
        }
    }

    private Boolean validate(Integer year, Integer subject) {
        Example example = new Example(StatisticBaseInfoEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("year",year).andEqualTo("subject",subject);
        List<StatisticBaseInfoEntity> allList = this.selectByExample(example);
        if (CollectionUtils.isEmpty(allList)) {
            return true;
        }
        return false;
    }

    @Override
    public List<StatisticBaseInfoEntity> findAll() {
        Example example = new Example(StatisticBaseInfoEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<StatisticBaseInfoEntity> allList = this.selectByExample(example);
        return allList;
    }

    @Override
    public StatisticBaseInfoEntity findByYearAndSubject(Integer year, Integer subject) {
        Example example = new Example(StatisticBaseInfoEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("year",year).andEqualTo("subject",subject);
        List<StatisticBaseInfoEntity> allList = this.selectByExample(example);
        if (CollectionUtils.isEmpty(allList)) {
            return null;
        }
        return allList.get(0);
    }
}
