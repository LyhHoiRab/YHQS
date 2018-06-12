package com.yhqs.core.account.service;

import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.FunctionDao;
import com.yhqs.core.permission.dao.PermissionDao;
import com.yhqs.core.permission.dao.RoleDao;
import com.yhqs.core.permission.entity.Function;
import com.yhqs.core.permission.entity.Permission;
import com.yhqs.core.permission.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.wah.doraemon.entity.User;
import org.wah.doraemon.entity.consts.Sex;
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
public class AccountServiceImpl implements AccountService{

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private FunctionDao functionDao;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Override
    public User register(String username, String password, Boolean isInternal, String name, String nickname, String headImgUrl, Sex sex) throws Exception{
        Assert.hasText(username, "账户名称不能为空");
        Assert.hasText(password, "账户密码不能为空");
        Assert.notNull(isInternal, "账户域标识不能为空");

        User user = AccountUtils.register(username, password, isInternal, name, nickname, headImgUrl, sex);

        return user;
    }

    @Override
    public String login(String username, String password) throws Exception{
        Assert.hasText(username, "账户名不能为空");
        Assert.hasText(password, "账户密码不能为空");

        //缓存
        ShardedJedis jedis = shardedJedisPool.getResource();

        try{
            String ticket = AccountUtils.login(username, password);
            User   user   = AccountUtils.getUser(ticket);

            //缓存用户信息
            RedisUtils.save(jedis, CacheName.USER_INFO + ticket, user, CacheName.LOGIN_EXPIRE);

            //查询权限
            Set<Permission> permissions = new HashSet<Permission>();
            permissions.addAll(permissionDao.findByAccountId(user.getAccountId(), ResourceType.FUNCTION));

            List<Role> roles = roleDao.findByAccountId(user.getAccountId());
            if(roles != null && !roles.isEmpty()){
                permissions.addAll(permissionDao.findByRoleIds(ObjectUtils.ids(roles), ResourceType.FUNCTION));
            }

            //所有功能
            Set<String> functionIds = new HashSet<String>();
            if(permissions != null && !permissions.isEmpty()){
                for(Permission permission : permissions){
                    functionIds.add(permission.getResourceId());
                }
            }

            if(functionIds != null && !functionIds.isEmpty()){
                List<Function> functions = functionDao.find(null, null, null, new ArrayList<String>(functionIds));
                //缓存
                if(functions != null && !functions.isEmpty()){
                    RedisUtils.save(jedis, CacheName.USER_FUNCTION + ticket, functions, CacheName.LOGIN_EXPIRE);
                }
            }

            //返回Ticket
            return ticket;
        }finally{
            RedisUtils.close(jedis);
        }
    }

    @Order
    public void logout(String ticket) throws Exception{
        Assert.hasText(ticket, "票据凭证不能为空");

        if(AccountUtils.logout(ticket)){
            ShardedJedis jedis = shardedJedisPool.getResource();

            RedisUtils.delete(jedis, CacheName.USER_INFO + ticket);
            RedisUtils.delete(jedis, CacheName.USER_FUNCTION + ticket);
            RedisUtils.close(jedis);
        }
    }
}
