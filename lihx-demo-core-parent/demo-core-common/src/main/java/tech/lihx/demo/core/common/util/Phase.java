package tech.lihx.demo.core.common.util;

import java.io.Serializable;


/**
 * 学段对象
 * <p>
 * 
 * @author LHX
 * @date 2015-2-5
 * @version 1.0.0
 */
public class Phase implements Serializable {

	private Long phaseId;

	private String phaseName;


	private Phase( Long phaseId, String phaseName ) {

		this.phaseId = phaseId;
		this.phaseName = phaseName;

	}


	public static Phase getChuZhongInstance() {
		return new Phase(52L, "初中");
	}


	public static Phase getGaoZhongInstance() {
		return new Phase(51L, "高中");
	}


	public static Phase getXiaoXueInstance() {
		return new Phase(53L, "小学");
	}


	public Long getPhaseId() {
		return phaseId;
	}


	public void setPhaseId( Long phaseId ) {
		this.phaseId = phaseId;
	}


	public String getPhaseName() {
		return phaseName;
	}


	public void setPhaseName( String phaseName ) {
		this.phaseName = phaseName;
	}


}
