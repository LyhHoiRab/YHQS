package com.yhqs.core.permission.service;

import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.dao.MenuDao;
import com.yhqs.core.permission.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.utils.RedisUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ShardedJedisPool pool;

    @Override
    @Transactional(readOnly = false)
    public void save(Menu menu){
        Assert.notNull(menu, "菜单信息不能为空");

        menuDao.saveOrUpdate(menu);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Menu menu){
        Assert.notNull(menu, "菜单信息不能为空");
        Assert.hasText(menu.getId(), "菜单ID不能为空");

        menuDao.saveOrUpdate(menu);
    }

    @Override
    public Menu getById(String id){
        Assert.hasText(id, "菜单ID不能为空");

        return menuDao.getById(id);
    }

    @Override
    public Page<Menu> page(PageRequest pageRequest, String id, String name, String url, String parentId, Boolean isParent){
        Assert.notNull(pageRequest, "分页信息不能为空");

        return menuDao.page(pageRequest, id, name, url, parentId, isParent);
    }

    @Order
    public List<Menu> getByTicket(String ticket){
        ShardedJedis jedis = pool.getResource();

        Set<Menu> menus = RedisUtils.smembers(jedis, CacheName.USER_MENU + ticket, Menu.class);
        RedisUtils.close(jedis);

        //排序
        List<Menu> target = new ArrayList<Menu>(menus);

        Menu temp;
        for(int i = 0; i < target.size() - 1; i++){
            for(int j = target.size() - 1; j > i; j--){
                if(target.get(j).getSorted() < target.get(j - 1).getSorted()){
                    temp = target.get(j);
                    target.set(j, target.get(j - 1));
                    target.set(j - 1, temp);
                }
            }
        }

        return target;
    }
}
