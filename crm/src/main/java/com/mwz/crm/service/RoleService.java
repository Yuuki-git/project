package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.ModuleMapper;
import com.mwz.crm.dao.PermissionMapper;
import com.mwz.crm.dao.RoleMapper;
import com.mwz.crm.query.RoleQuery;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.vo.Permission;
import com.mwz.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: yuuki
 * @Date: 2021-06-07 - 06 - 16:31
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;
    /**
     * 通过参数查询角色
     * @param roleQuery
     */
    public Map<String, Object> queryRoleByParams(RoleQuery roleQuery){
        Map<String,Object> map=new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        List<Role> roleList=roleMapper.selectByParams(roleQuery);
        PageInfo<Role> pageInfo=new PageInfo<>(roleList);

        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 查询所有的roles
     * @return
     */
    public List<Map<String,Object>> findRoles(Integer id){return roleMapper.queryAllRoles(id);}

    /**
     * 1、参数校验
     * role_name
     * role_remark
     * 2、设置默认值
     * 3、执行操作判断受影响的行数
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRoles(Role role){
        checkAddRolesParams(role.getRoleName(),role.getRoleRemark());
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role)!=1,"添加操作失败");
    }

    private void checkAddRolesParams(String roleName, String roleRemark) {
        AssertUtil.isTrue(StringUtils.isBlank(roleName),"角色名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(roleRemark),"角色备注不能为空");
    }

    /**
     * 更新角色信息
     * 1.参数校验
     * roleName
     * roleRemake
     * 2设置默认值
     * 更新时间
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Role role){
        checkAddRolesParams(role.getRoleName(),role.getRoleRemark());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)!=1,"更新操作失败");
    }

    /**
     * 更新操作
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer[] ids){
        AssertUtil.isTrue(ids==null||ids.length==0,"数据不存在");
        AssertUtil.isTrue(roleMapper.deleteBatch(ids)<ids.length,"更新操作失败");
    }

    /**
     * 授权
     *
     * @param roleId
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId,Integer[] ids){
        // 通过查询记录返回行数
        Integer count = permissionMapper.selectCountByRoleId(roleId);
        // 若count大于0
        if(count>0){
            // 删除所有的记录
            AssertUtil.isTrue(permissionMapper.deleteByRoleId(roleId)<count,"删除grant失败");
        }
        // 批量添加,通过roleId和ids
        List<Permission> permissionList=new ArrayList<>();
        if(ids.length>0 && ids!=null){
            for(Integer id:ids){
                Permission permission=new Permission();
                permission.setRoleId(roleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(id);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(id).getOptValue());

                System.out.println(id);
                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)<permissionList.size(),"添加操作失败");
        }
    }
}
