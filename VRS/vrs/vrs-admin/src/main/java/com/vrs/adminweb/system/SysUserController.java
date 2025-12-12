package com.vrs.adminweb.system;

import com.vrs.common.annotation.Log;
import com.vrs.common.constant.UserConstants;
import com.vrs.common.core.controller.BaseController;
import com.vrs.common.core.domain.AjaxResult;
import com.vrs.common.core.domain.entity.SysRole;
import com.vrs.common.core.domain.entity.SysUser;
import com.vrs.common.core.domain.model.LoginUser;
import com.vrs.common.core.page.TableDataInfo;
import com.vrs.common.enums.BusinessType;
import com.vrs.common.utils.SecurityUtils;
import com.vrs.common.utils.StringUtils;
import com.vrs.common.utils.poi.ExcelUtil;
import com.vrs.system.service.ISysPostService;
import com.vrs.system.service.ISysRoleService;
import com.vrs.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    /**
     * 获取用户列表
     */
   // @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if ("01".equals(loginUser.getUser().getUserType())){
            if (("02").equals(user.getUserType())){
                //承运商查询司机的时候，仅查询承运商自己的司机列表
                user.setParentId(loginUser.getUser().getUserId());
            }else  if (("01").equals(user.getUserType())){
                //承运商查询承运商列表，仅能查询自己
                user.setUserId(loginUser.getUser().getUserId());
            }
        }
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
   // @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
   // @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long carrierId = null;
        if ("01".equals(user.getUserType())){
            //承运商查询自己的数据
            carrierId=user.getUserId();
        }
        userService.checkUserDataScope(userId);

        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        ajax.put("carriers",  userService.selectCarrierAll(carrierId));
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
   // @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }else if (StringUtils.isNotEmpty(user.getIdcard())
                && UserConstants.NOT_UNIQUE.equals(userService.checkIdCardUnique(user))){
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，身份证号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
   // @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }else if (StringUtils.isNotEmpty(user.getIdcard())
                && UserConstants.NOT_UNIQUE.equals(userService.checkIdCardUniqueNotCurrentUser(user.getIdcard(),user.getUserId()))){
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，身份证号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
   // @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId()))
        {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    //@PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    //@PreAuthorize("@ss.hasPermi('system:user:edit')||@ss.hasPermi('system.user.removeBlack')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
   // @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 查询承运商
     */
    @GetMapping("/getCarrierList")
    public AjaxResult getCarrierList()
    {
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();

        AjaxResult ajax = AjaxResult.success();
        SysUser queryParam = new SysUser();
        queryParam.setUserType("01");
        queryParam.setIsBlacklist(0);
        if (("01").equals(loginUser.getUserType())){
            //承运商查询自己的数据
            queryParam.setUserId(loginUser.getUserId());
        }
        List<SysUser> user = userService.selectUserList(queryParam);
        ajax.put("carrier", user);
        return ajax;


    }

    /**
     * 根据上级id查询用户列表
     */
    @GetMapping("/getDriverList/{parentId}")
    public AjaxResult getDriverList(@PathVariable("parentId") Long parentId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser queryParam = new SysUser();
        queryParam.setParentId(parentId);
        queryParam.setUserType("02");
        //查询非黑名单用户
        queryParam.setIsBlacklist(0);
        List<SysUser> user = userService.selectUserList(queryParam);
        ajax.put("drivers", user);
        return ajax;
    }


    /**
     * 查询审核人   角色id固定144
     */
    @GetMapping("/getCheckerList")
    public AjaxResult getCheckerList()
    {
        Long userId = SecurityUtils.getUserId();
        AjaxResult ajax = AjaxResult.success();
        List<SysUser> user = userService.selectCheckerList(userId);
        ajax.put("checkers", user);
        return ajax;
    }

    /**
     * 查询审核人   角色id固定144
     */
    @GetMapping("/getDetailByUserId/{userId}")
    public AjaxResult getDetailByUserId(@PathVariable("userId") Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.getDetailByUserId(userId);
        ajax.put("user", user);
        return ajax;
    }



}
