package com.yhqs.core.permission.service;

import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.MenuDao;
import com.yhqs.core.permission.dao.PermissionDao;
import com.yhqs.core.permission.dao.RoleDao;
import com.yhqs.core.permission.entity.Menu;
import com.yhqs.core.permission.entity.Permission;
import com.yhqs.core.permission.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wah.doraemon.entity.User;
import org.wah.doraemon.security.exception.TicketAuthenticationException;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.utils.ObjectUtils;
import org.wah.doraemon.utils.RedisUtils;
import org.wah.ferryman.utils.AccountUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RoleDao roleDao;

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
    public List<Menu> getByTicket(String ticket) throws Exception{
        ShardedJedis jedis = pool.getResource();

        try{
            //查询用户
            User user = RedisUtils.get(jedis, CacheName.USER_INFO + ticket, User.class);
            if(user == null){
                user = AccountUtils.getUser(ticket);
                //缓存
                RedisUtils.save(jedis, CacheName.USER_INFO + ticket, user, CacheName.LOGIN_EXPIRE);
            }

            //查询权限
            Set<Permission> permissions = new HashSet<Permission>();
            permissions.addAll(permissionDao.findByAccountId(user.getAccountId(), ResourceType.MENU));

            List<Role> roles = roleDao.findByAccountId(user.getAccountId());
            if(roles != null && !roles.isEmpty()){
                permissions.addAll(permissionDao.findByRoleIds(ObjectUtils.ids(roles), ResourceType.MENU));
            }

            //所有功能
            Set<String> menuIds = new HashSet<String>();
            if(permissions != null && !permissions.isEmpty()){
                for(Permission permission : permissions){
                    menuIds.add(permission.getResourceId());
                }
            }

            //排序
            List<Menu> list = menuDao.find(null, null, null, null, null, new ArrayList<String>(menuIds));

            Menu temp;
            for(int i = 0; i < list.size() - 1; i++){
                for(int j = list.size() - 1; j > i; j--){
                    if(list.get(j).getSorted() < list.get(j - 1).getSorted()){
                        temp = list.get(j);
                        list.set(j, list.get(j - 1));
                        list.set(j - 1, temp);
                    }
                }
            }

            return list;
        }finally{
            RedisUtils.close(jedis);
        }
    }
}
