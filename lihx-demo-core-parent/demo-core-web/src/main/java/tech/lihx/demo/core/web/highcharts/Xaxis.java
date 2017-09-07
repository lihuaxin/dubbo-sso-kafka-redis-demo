package tech.lihx.demo.core.web.highcharts;


/**
 * highcharts Xaxis
 * <p>
 * 
 * @author LHX
 * @date 2015-12-20
 * @version 1.0.0
 */
public class Xaxis {

	private String[] categories;

	private String lineColor;

	private Integer lineWidth;

	private String gridLineWidth;

	private String gridLineDashStyle;

	private Labels labels;


	public String[] getCategories() {
		return categories;
	}


	public void setCategories( String[] categories ) {
		this.categories = categories;
	}


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


	public String getGridLineWidth() {
		return gridLineWidth;
	}


	public void setGridLineWidth( String gridLineWidth ) {
		this.gridLineWidth = gridLineWidth;
	}


	public String getGridLineDashStyle() {
		return gridLineDashStyle;
	}


	public void setGridLineDashStyle( String gridLineDashStyle ) {
		this.gridLineDashStyle = gridLineDashStyle;
	}


	public Labels getLabels() {
		return labels;
	}


	public void setLabels( Labels labels ) {
		this.labels = labels;
	}

}
