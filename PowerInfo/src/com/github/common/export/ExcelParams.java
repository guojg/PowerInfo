package com.github.common.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.format.Alignment;

public class ExcelParams {
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 导出的文件的标题
	 */
	private String[] excelTitle;
	
	/**
	 * 导出的文件的标题的对齐方式
	 */
	private Alignment[] excelAlign;
	
	/**
	 * 导出表头名
	 */
	private String[] colTitle;
	
	/**
	 * 导出列名
	 */
	private String[] colName;
	
	/**
	 * 导出数据单元格对齐方式
	 */
	private Alignment[] colAlign;
	
	/**
	 * 导出数据单元格宽度
	 */
	private String[] colWidth;
	
	/**
	 * 固定行的行索引
	 */
	private String rowLock;
	
	/**
	 * 固定列的列索引
	 */
	private String colLock; 
	
	
	/**
	 * 导出的文件数据
	 */
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	public ExcelParams(String fileName, String[] excelTitle,
			Alignment[] excelAlign, String[] colTitle, String[] colName,
			List<Map<String, Object>> list) {
		super();
		this.fileName = fileName;
		this.excelTitle = excelTitle;
		this.excelAlign = excelAlign;
		this.colTitle = colTitle;
		this.colName = colName;
		this.list = list;
	}
	
	public ExcelParams(String fileName, String[] excelTitle,
			Alignment[] excelAlign, String[] colTitle, String[] colName,
			Alignment[] colAlign, String[] colWidth, String rowLock,
			String colLock, List<Map<String, Object>> list) {
		super();
		this.fileName = fileName;
		this.excelTitle = excelTitle;
		this.excelAlign = excelAlign;
		this.colTitle = colTitle;
		this.colName = colName;
		this.colAlign = colAlign;
		this.colWidth = colWidth;
		this.rowLock = rowLock;
		this.colLock = colLock;
		this.list = list;
	}

	public ExcelParams() {
		
	}

	public String[] getExcelTitle() {
		return excelTitle;
	}

	public void setExcelTitle(String[] excelTitle) {
		this.excelTitle = excelTitle;
	}

	public Alignment[] getExcelAlign() {
		return excelAlign;
	}

	public void setExcelAlign(Alignment[] excelAlign) {
		this.excelAlign = excelAlign;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getColTitle() {
		return colTitle;
	}

	public void setColTitle(String[] colTitle) {
		this.colTitle = colTitle;
	}

	public String[] getColName() {
		return colName;
	}

	public void setColName(String[] colName) {
		this.colName = colName;
	}

	public Alignment[] getColAlign() {
		return colAlign;
	}

	public void setColAlign(Alignment[] colAlign) {
		this.colAlign = colAlign;
	}

	public String[] getColWidth() {
		return colWidth;
	}

	public void setColWidth(String[] colWidth) {
		this.colWidth = colWidth;
	}

	public String getRowLock() {
		return rowLock;
	}

	public void setRowLock(String rowLock) {
		this.rowLock = rowLock;
	}

	public String getColLock() {
		return colLock;
	}

	public void setColLock(String colLock) {
		this.colLock = colLock;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	
}
