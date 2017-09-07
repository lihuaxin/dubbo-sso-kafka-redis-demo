package tech.lihx.demo.core.web.highcharts;

import java.util.List;


/**
 * highcharts ChartTxt
 * <p>
 * 
 * @author LHX
 * @date 2016-12-20
 * @version 1.0.0
 */
public class ChartTxt {

	private Chart chart;

	private Title title;

	private String[] colors;

	private Tooltip tooltip;

	private Xaxis XAxis;

	private Yaxis YAxis;

	private List<Serie> series;


	public Chart getChart() {
		return chart;
	}


	public void setChart( Chart chart ) {
		this.chart = chart;
	}


	public Title getTitle() {
		return title;
	}


	public void setTitle( Title title ) {
		this.title = title;
	}


	public String[] getColors() {
		return colors;
	}


	public void setColors( String[] colors ) {
		this.colors = colors;
	}


	public Tooltip getTooltip() {
		return tooltip;
	}


	public void setTooltip( Tooltip tooltip ) {
		this.tooltip = tooltip;
	}


	public Xaxis getXAxis() {
		return XAxis;
	}


	public void setXAxis( Xaxis xAxis ) {
		XAxis = xAxis;
	}


	public Yaxis getYAxis() {
		return YAxis;
	}


	public void setYAxis( Yaxis yAxis ) {
		YAxis = yAxis;
	}


	public List<Serie> getSeries() {
		return series;
	}


	public void setSeries( List<Serie> series ) {
		this.series = series;
	}
}
