package com.mwz.crm.service;

import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.ModuleMapper;
import com.mwz.crm.dao.PermissionMapper;
import com.mwz.crm.model.TreeModel;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.vo.Module;
import com.mwz.crm.vo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-08 - 06 - 13:39
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private  PermissionMapper permissionMapper;


    public List<TreeModel> queryAllModule(Integer roleId){
        // 查询所有的资源列表
        List<TreeModel> treeModelList=moduleMapper.queryAllModules();
        // 查询指定角色已授权的资源列表（查询角色拥有的资源列表
        List<Integer> permissionIds= permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        // 判断角色是否拥有资源Id
        if(permissionIds != null && permissionIds.size()>0){
            //设置将相关属性(打开页面时默认选中)   循环
            /*for (Integer moduleId : moduleIdList){
                for (TreeModel treeModel:treeModelList){
                    if(treeModel.getId().equals(moduleId)){
                        treeModel.setChecked(true);
                    }
                }
            }*/
            // 循环所有资源列表，判断用户拥有的资源ID是否有匹配的，若果有，则设置checked属性为true
            for(TreeModel treeModel:treeModelList){
               treeModelList.forEach(treeModel1 -> {
                   // 判断角色拥有的资源ID中是否有遍历的资源ID
                   if(permissionIds.contains(treeModel.getId())){
                       // 如果包含，说明角色受全过，设置checked为true
                       treeModel.setChecked(true);
                   }
               });
            }
        }
        return treeModelList;
    }
    /**
     * 查询
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,Object> queryModuleList(){
        Map<String,Object> map=new HashMap<>();
        List<Module> moduleList=moduleMapper.queryModuleList();
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",moduleList.size());
        map.put("data",moduleList);
        return map;
    }

    /**
     * 添加资源
     * 参数校验
     *          1、模块名称
     *              非空，且该层级唯一
     *          2、地址url
     *              二级菜单    非空且不能重复
     *           3、父级菜单
     *                若为一级菜单    parentId 为null
     *                若不为一级菜单，parentId 不能为空 ,且父级菜单存在
     *           4、层级grade
     *                  非空012
     *            5、权限码非空不可重复
     *
     * 设置默认值
     * 执行操作返回受影响的行数
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
            /* 参数校验 */
        // 得到菜单等级
        Integer grade=module.getGrade();
        AssertUtil.isTrue(null==grade||!(grade==0||grade==1||grade==2),"非法等级");
        // 模块名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        AssertUtil.isTrue(null!=moduleMapper.selectByGradeAndModule(grade,module.getModuleName()),"模块名称重复");
        // 地址url
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url不能为空");
            // 不重复
            AssertUtil.isTrue(null!=moduleMapper.selectByGradeAndUrl(grade,module.getUrl()),"url不能重复");
        }
        if(grade==0){
            module.setParentId(-1);
        }
        if(grade==1||grade==2){
            AssertUtil.isTrue(null==module.getParentId(),"父级菜单不能为空");
            AssertUtil.isTrue(null==moduleMapper.selectByPrimaryKey(module.getParentId()),"父级菜单不存在");
        }

        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        AssertUtil.isTrue(null!=moduleMapper.selectByOptValue(module.getOptValue()),"权限码不能重复");
        module.setIsValid((byte) 1);
        module.setUpdateDate(new Date());
        module.setCreateDate(new Date());
        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"添加操作失败");
    }

    /**
     * 更新
     * 参数校验
     * 设置默认值
     * 执行操作，判断受影响的行数
     * @param module
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        // 判断id非空且存在
        Integer id=module.getId();
        AssertUtil.isTrue(null==id||null==moduleMapper.selectByPrimaryKey(id),"待更新记录不存在");
        // 得到菜单等级
        Integer grade=module.getGrade();
        AssertUtil.isTrue(null==grade||!(grade==0||grade==1||grade==2),"非法等级");
        // 模块名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        AssertUtil.isTrue(!(moduleMapper.selectByGradeAndModule(grade,module.getModuleName()).getId().equals(module.getId())),"模块名称重复");
        // 地址url
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url不能为空");
            // 不重复
            AssertUtil.isTrue(!(moduleMapper.selectByGradeAndUrl(grade,module.getUrl()).getId().equals(module.getId())),"url不能重复");
        }
        if(grade==0){
            module.setParentId(-1);
        }
        if(grade==1||grade==2){
            AssertUtil.isTrue(null==module.getParentId(),"父级菜单不能为空");
            AssertUtil.isTrue(null==moduleMapper.selectByPrimaryKey(module.getParentId()),"父级菜单不存在");
        }

        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        AssertUtil.isTrue(!(moduleMapper.selectByOptValue(module.getOptValue()).getId().equals(module.getId())),"权限码不能重复");
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module)<1,"更新操作失败");

    }

    /**
     * 删除操作
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer ids) {
        // 判断ids是否为空
        AssertUtil.isTrue(null==ids,"待删除记录不存在");
        //通过查询得到资源对象
        Module module= moduleMapper.selectByPrimaryKey(ids);
        AssertUtil.isTrue(null==selectByPrimaryKey(ids),"待删除记录不存在");
        //查询目录下是否有子目录
        Integer count=moduleMapper.selectByParentId(ids)+1;
        if(count>0){
            AssertUtil.isTrue(moduleMapper.deleteByPrimaryKeyAndParentId(ids)<count,"删除操作失败");
        }
        // 查询是否赋予了角色权限
        Integer count1=permissionMapper.selectByModuleId(ids);
        // 删除对应的角色权限
        if(count1>0){
            AssertUtil.isTrue(permissionMapper.deleteByModuleId(ids)<count1,"删除操作2失败");
        }

    }
}
