package com.github.common.export.rules;

import jxl.write.WritableWorkbook;

/**
 * 导出规则的基类
 *
 */
public abstract class MergeRules {
	
	/**
	 * 可扩展的合并单元格格式
	 * @param contextIndex
	 * 			表格数据开始行号，从0开始，默认Excel除去标题行和表头行
	 * @param colsIndex
	 * 			合并列的索引，索引从0开始，小于workbook的列长度
	 * @param workbook
	 * 			导出的Excel对象
	 */
	public abstract void extendMergeRules(int contextIndex, int[] colsIndex, WritableWorkbook workbook);

}
