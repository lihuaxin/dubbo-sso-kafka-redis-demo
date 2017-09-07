package tech.lihx.demo.core.support.privilege.shiro;

import java.util.List;

/**
 * 获取权限接口
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:15:46
 */
public interface IFetchPermission {

	// 获取字符串权限控制
	public List<String> fetchPermissions( Long userId );


	// 获取角色权限控制
	public List<String> fetchRoles( Long userId );
}
