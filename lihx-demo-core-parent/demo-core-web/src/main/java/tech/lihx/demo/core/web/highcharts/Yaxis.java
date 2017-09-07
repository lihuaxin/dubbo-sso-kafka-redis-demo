package tech.lihx.demo.core.web.highcharts;


/**
 * highcharts Yaxis
 * <p>
 * 
 * @author LHX
 * @date 2015-12-20
 * @version 1.0.0
 */
public class Yaxis {

	private String lineColor;

	private Integer lineWidth;

	private Title title;

	private Credits Credits;


	public String getLineColor() {
		return lineColor;
	}


	public void setLineColor( String lineColor ) {
		this.lineColor = lineColor;
	}


	public Integer getLineWidth() {
		return lineWidth;
	}


	public void setLineWidth( Integer lineWidth ) {
		this.lineWidth = lineWidth;
	}


	public Title getTitle() {
		return title;
	}


	public void setTitle( Title title ) {
		this.title = title;
	}


	public Credits getCredits() {
		return Credits;
	}


	public void setCredits( Credits credits ) {
		Credits = credits;
	}

}
