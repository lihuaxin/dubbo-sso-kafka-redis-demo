package tech.lihx.demo.core.web.highcharts;


/**
 * highcharts Tooltip
 * <p>
 * 
 * @author LHX
 * @date 2015年2月4日
 * @version 1.0.0
 */
public class Tooltip {

	private String valueSuffix;


	public Tooltip( String valueSuffix ) {
		this.valueSuffix = valueSuffix;
	}


	public String getValueSuffix() {
		return valueSuffix;
	}


	public void setValueSuffix( String valueSuffix ) {
		this.valueSuffix = valueSuffix;
	}
}
