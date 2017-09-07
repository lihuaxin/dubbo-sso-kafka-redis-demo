package tech.lihx.demo.core.web.highcharts;

import java.math.BigDecimal;


/**
 * highcharts Serie
 * <p>
 * 
 * @author LHX
 * @date 2015-12-20
 * @version 1.0.0
 */
public class Serie {

	private String name;

	private BigDecimal[] data;


	public Serie( String name, BigDecimal[] data ) {
		this.name = name;
		this.data = data;
	}


	public String getName() {
		return name;
	}


	public void setName( String name ) {
		this.name = name;
	}


	public BigDecimal[] getData() {
		return data;
	}


	public void setData( BigDecimal[] data ) {
		this.data = data;
	}
}
