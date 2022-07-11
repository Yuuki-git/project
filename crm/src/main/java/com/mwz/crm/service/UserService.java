package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.UserMapper;
import com.mwz.crm.dao.UserRoleMapper;
import com.mwz.crm.model.UserModel;
import com.mwz.crm.query.UserQuery;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.utils.Md5Util;
import com.mwz.crm.utils.PhoneUtil;
import com.mwz.crm.utils.UserIDBase64;
import com.mwz.crm.vo.User;
import com.mwz.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.*;

/**
 * @author: yuuki
 * @Date: 2021-04-06 - 04 - 0:35
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;
    /**
     *         1.判断参数是否为空 (姓名、密码)
     *             如果为空，抛出异常
     *         2.通过用户名查询用户对象
     *         3.判断用户对象是否为空
     *             如果为空，用户不存在，抛出异常
     *         4.将前台传递的密码与数据库中查询的密码作比较(先将前合密码加密后，再比较)
     *             如果密码不相等，密码错误，抛出异常
     *         5.登录成功，返回成功的封装类（ResultInfo对象，将用户信息封装到UserModel中，设置封装类）
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName, String userPwd){
        // 验证参数是否为空
        checkUserParams(userName,userPwd);
        // 调用dao层的方法通过用户名查询用户对象
       User user= userMapper.queryUserByUserName(userName);
       // 判断用户是否为空
       AssertUtil.isTrue(user==null,"用户不存在");
       // 对比密码
        checkUserPwd(userPwd, user.getUserPwd());
        // 构建用户模型并返回
        return buildUserModel(user);
    }

    private UserModel buildUserModel(User user) {
        UserModel userModel=new UserModel();
        // userModel.setUserId(user.getId());
        String userIdStr = UserIDBase64.encoderUserID(user.getId());
        userModel.setUserIdStr(userIdStr);
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 将前台传递的密码与数据库中查询的密码作比较(先将前合密码加密后，再比较)
     * @param userPwd 密码
     * @param pwd 查询到的密码
     */
    private void checkUserPwd(String userPwd, String pwd) {
        // 通过md5加密
        userPwd= Md5Util.encode(userPwd);
        // 将加密过的比较
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确");
    }

    /**
     * 验证参数是否为空
     * @param userName
     * @param userPwd
     */
    private void checkUserParams(String userName,String userPwd){
        // 判断用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");

    }

    /**
     *         1、从cookie中拿到用户id
     *         2、用户非空且用户对象存在（通过id查询用户对象）
     *         3、原始密码非空且与数据库中的密码一致
     *         4、新密码非空且不能与原始密码相同
     *         5、重复密码非空且与新密码保持一致
     *         6 执行修改密码操作，判断受影响的行数
     * @param userId    用户id
     * @param userPwd   原始密码
     * @param newPwd    新密码
     * @param repeatPwd 重复新密码
     */
    public void updateUserPwd(Integer userId,String userPwd,String newPwd,String repeatPwd){
        // 从cookie中拿到用户id（）在controller层完成
        // 通关id查询用户对象
        User user=userMapper.selectByPrimaryKey(userId);
        // 判断用户对象非空
        AssertUtil.isTrue(user==null,"用户对象不能为空");
        // 判断参数是否正确
        checkUserPwdParams(userPwd,newPwd,repeatPwd,user.getUserPwd());
        // 修改密码
        user.setUserPwd(Md5Util.encode(newPwd));
        user.setUpdateDate(new Date());
        // 判断修改的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");
    }

    /**
     *         3、原始密码非空且与数据库中的密码一致
     *         4、新密码非空且不能与原始密码相同
     *         5、重复密码非空且与新密码保持一致
     * @param userPwd   原始密码
     * @param newPwd    新密码
     * @param repeatPwd 重复新密码
     * @param pwd       数据库中的密码
     */

    private void checkUserPwdParams(String userPwd, String newPwd, String repeatPwd,String pwd) {
        // 原始密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"原始密码不能为空");
        // 验证是否与数据库中的密码一致
        AssertUtil.isTrue(!pwd.equals(Md5Util.encode(userPwd)),"原始密码不正确");
        //  新密码非空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        // 不与原始密码相同
        AssertUtil.isTrue(newPwd.equals(userPwd),"新密码不能与原始密码相同");
        // 重复密码非空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"重复密码不能为空");
        // 重复面膜是否与新密码保持一致
        AssertUtil.isTrue(!repeatPwd.equals(newPwd),"重复密码需与原密码保持一致");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String, Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     *
     * @param userQuery
     * @return map
     */
    public Map<String,Object> queryUser(UserQuery userQuery){
        Map<String,Object> map=new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        List<User> userList=userMapper.selectByParams(userQuery);

        PageInfo<User> pageInfo=new PageInfo<>(userList);

        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;

    }

    /**
     *添加用户操作
     *  1、参数校验
     *       userName 非空
     *       TrueName 非空
     *       phone  非空且格式正确
     *       email  非空
     *  2、设置默认值
     *      userPwd
     *      is_valid
     *      createDate
     *      updateDate
     *  3、执行添加操作，判断受影响的行数
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone(),user.getEmail());
        // 设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        String PWD=Md5Util.encode("123456");
        user.setUserPwd(PWD);
        AssertUtil.isTrue(userMapper.insertSelective(user)!=1,"添加操作失败");
    //    用户和角色关联
        relationUserRole(user.getId(),user.getRoleIds());

    }

    private void relationUserRole(Integer userId, String roleIds) {
        Integer count=userRoleMapper.countUserRoleByUserId(userId);
        // 判断角色记录是否存在
        if(count>0){
        //    如果角色记录存在，删除所有角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)<count,"用户角色关联操作失败");
        }
        // 判断角色id是否存在，如果存在，添加该用户对应的角色记录
        if(StringUtils.isNotBlank(roleIds)){
            // 将用户设置到集合中，批量添加
            List<UserRole> userRoleList=new ArrayList<>();
            // 将角色id字符串转换成数组
            String[] roleIdsArray=roleIds.split(",");
            // 遍历数组,得到对应的用户角色对象，将其设置到list中
            for(String roleId:roleIdsArray){
                UserRole userRole=new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                // 设置到集合中
                userRoleList.add(userRole);
            }
            // 批量添加记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList)!=userRoleList.size(),"用户关联操作失败");
        }
    }

    private void checkParams(String userName, String trueName, String phone, String email) {
            AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
            AssertUtil.isTrue(StringUtils.isBlank(trueName),"真实姓名不能为空");
            AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
            AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
            AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
    }

    /**
     *更新用户操作
     *          1.参数校验
     *           userName 非空
     *           TrueName 非空
     *           phone  非空且格式正确
     *           email  非空
     *          2.设置默认值
     *              updateDate
     *          3.执行更新操作，判断受影响的行数
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        Integer id=user.getId();
        AssertUtil.isTrue(null==id||userMapper.selectByPrimaryKey(id)==null,"待更新数据不存在");
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone(),user.getEmail());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)!=1,"更新操作失败");
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 批量删除用户（更新）
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        //判断ids是否为空 , 因为是从请求域中拿的ids，所以本身就是存在于数据库中的，不需要在判断是否有记录
        AssertUtil.isTrue(ids==null||ids.length==0,"数据不存在");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<ids.length,"更新操作失败");

        // 遍历用户id的数组
        for(Integer userId:ids){
            Integer count=userRoleMapper.countUserRoleByUserId(userId);
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户关联信息删除失败");
            }
        }
    }

    /**
     * 1、添加操作
     *      原始角色不存在
     *          1.没有添加
     *              不添加新的角色新的记录 不操作用户角色表
     *              添加新的角色记录    给指定用户绑定相关的记录
     *   更新操作
     *      原始角色不存在
     *         不添加新的角色新的记录 不操作用户角色表
     *      *              添加新的角色记录    给指定用户绑定相关的记录
     *      原始角色存在
     *      1.添加新的橘色记录 已有的角色不添加，添加没有的角色记录
     *      2.清空所有的角色记录 删除用户绑定角色表
     *      3.已部分角色添加新的角色    删除不存在的角色记录,存在的记录保留
     *      4.移除部分角色，添加新的角色   删除不存在的角色记录,存在的记录保留,添加新的角色
     *      全部删掉再添加
     */

}
