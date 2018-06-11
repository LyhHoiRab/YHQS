package com.yhqs.core.init;

import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.MenuDao;
import com.yhqs.core.permission.dao.PermissionDao;
import com.yhqs.core.permission.entity.Menu;
import com.yhqs.core.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wah.doraemon.utils.IDGenerator;
import org.wah.doraemon.utils.ObjectUtils;

import java.util.Arrays;

@Component
public class MenuInit{

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private PermissionDao permissionDao;

    @Transactional
    public void init(){
        String roleId = "0";

        //基础菜单
        Menu organizationMenu = new Menu();
        organizationMenu.setId(IDGenerator.uuid32());
        organizationMenu.setName("企业架构");
        organizationMenu.setIconClass("fa-sitemap");
        organizationMenu.setSorted(0);

        Menu companyMenu = new Menu();
        companyMenu.setId(IDGenerator.uuid32());
        companyMenu.setName("公司管理");
        companyMenu.setParentId(organizationMenu.getId());
        companyMenu.setUrl("/page/backstage/company/index");
        companyMenu.setRouterUrl("/company/index");
        companyMenu.setIconClass("fa-store-alt");
        companyMenu.setSorted(0);

        Menu permissionMenu = new Menu();
        permissionMenu.setId(IDGenerator.uuid32());
        permissionMenu.setName("权限配置");
        permissionMenu.setIconClass("fa-exclamation-circle");
        permissionMenu.setSorted(1);

        Menu roleMenu = new Menu();
        roleMenu.setId(IDGenerator.uuid32());
        roleMenu.setName("角色管理");
        roleMenu.setParentId(permissionMenu.getId());
        roleMenu.setUrl("/page/backstage/role/index");
        roleMenu.setRouterUrl("/role/index");
        roleMenu.setIconClass("fa-user-lock");
        roleMenu.setSorted(0);

        Menu functionMenu = new Menu();
        functionMenu.setId(IDGenerator.uuid32());
        functionMenu.setName("功能管理");
        functionMenu.setParentId(permissionMenu.getId());
        functionMenu.setUrl("/page/backstage/function/index");
        functionMenu.setRouterUrl("/function/index");
        functionMenu.setIconClass("fa-archive");
        functionMenu.setSorted(1);

        Menu menuMenu = new Menu();
        menuMenu.setId(IDGenerator.uuid32());
        menuMenu.setName("菜单管理");
        menuMenu.setParentId(permissionMenu.getId());
        menuMenu.setUrl("/page/backstage/menu/index");
        menuMenu.setRouterUrl("/menu/index");
        menuMenu.setIconClass("fa-server");
        menuMenu.setSorted(2);

        Menu accountMenu = new Menu();
        accountMenu.setId(IDGenerator.uuid32());
        accountMenu.setName("账户管理");
        accountMenu.setIconClass("fa-user-cog");
        accountMenu.setSorted(2);

        Menu accountSecondMenu = new Menu();
        accountSecondMenu.setId(IDGenerator.uuid32());
        accountSecondMenu.setName("账户管理");
        accountSecondMenu.setParentId(accountMenu.getId());
        accountSecondMenu.setUrl("/page/backstage/account/index");
        accountSecondMenu.setRouterUrl("/account/index");
        accountSecondMenu.setIconClass("fa-user-cog");
        accountSecondMenu.setSorted(0);

        menuDao.saveList(Arrays.asList(organizationMenu, companyMenu, permissionMenu, roleMenu,
                                       functionMenu, menuMenu, accountMenu, accountSecondMenu));

        //创建超级管理员权限
        Permission organizationMenuPermission = new Permission();
        organizationMenuPermission.setType(ResourceType.MENU);
        organizationMenuPermission.setResourceId(organizationMenu.getId());

        Permission companyMenuPermission = new Permission();
        companyMenuPermission.setType(ResourceType.MENU);
        companyMenuPermission.setResourceId(companyMenu.getId());

        Permission permissionMenuPermission = new Permission();
        permissionMenuPermission.setType(ResourceType.MENU);
        permissionMenuPermission.setResourceId(permissionMenu.getId());

        Permission roleMenuPermission = new Permission();
        roleMenuPermission.setType(ResourceType.MENU);
        roleMenuPermission.setResourceId(roleMenu.getId());

        Permission functionMenuPermission = new Permission();
        functionMenuPermission.setType(ResourceType.MENU);
        functionMenuPermission.setResourceId(functionMenu.getId());

        Permission menuMenuPermission = new Permission();
        menuMenuPermission.setType(ResourceType.MENU);
        menuMenuPermission.setResourceId(menuMenu.getId());

        Permission accountMenuPermission = new Permission();
        accountMenuPermission.setType(ResourceType.MENU);
        accountMenuPermission.setResourceId(accountMenu.getId());

        Permission accountSecondMenuPermission = new Permission();
        accountSecondMenuPermission.setType(ResourceType.MENU);
        accountSecondMenuPermission.setResourceId(accountSecondMenu.getId());

        permissionDao.saveList(Arrays.asList(organizationMenuPermission, companyMenuPermission, permissionMenuPermission,
                                             roleMenuPermission, functionMenuPermission, menuMenuPermission,
                                             accountMenuPermission, accountSecondMenuPermission));

        //角色配置权限
        permissionDao.updateMenusToRole(ObjectUtils.ids(Arrays.asList(organizationMenuPermission, companyMenuPermission,
                                                                      permissionMenuPermission, roleMenuPermission,
                                                                      functionMenuPermission, menuMenuPermission,
                                                                      accountMenuPermission, accountSecondMenuPermission)), roleId);
    }
}
