package tech.lihx.demo.core.common.util;


/**
 * <p>
 * 
 * @author LHX
 * @date 2016-2-5
 * @version 1.0.0
 */
public class PhaseUtil {

	/**
	 * 用年级ID 拿到学段对象
	 * <p>
	 * 
	 * @param gradeId
	 * @return
	 */
	public static Phase getPhaseByGradeId( Long gradeId ) {
		if ( (gradeId < 3 && gradeId >= 0) || gradeId == 15 ) {
			return Phase.getChuZhongInstance();
		} else if ( gradeId > 2 && gradeId < 6 ) {
			return Phase.getGaoZhongInstance();
		} else {
			return Phase.getXiaoXueInstance();
		}
	}

}
