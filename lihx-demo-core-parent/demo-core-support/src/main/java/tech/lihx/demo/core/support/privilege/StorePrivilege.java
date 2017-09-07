package tech.lihx.demo.core.support.privilege;

import java.util.List;

/**
 * 扫描权限后保存或更新调用的接口
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:17:19
 */
public interface StorePrivilege {

	// 只对传入所有带Privilege注解的
	public void insertOrUpdate( List<PrivilegeBean> beans );
}
