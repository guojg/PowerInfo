package com.github.common.export.rules;

import java.io.IOException;

import jxl.Cell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 按列合并，合并规则：相邻数据相同
 *
 */
public class CellEqualMergeRules extends MergeRules {

	@Override
	public void extendMergeRules(int contextIndex, int[] colsIndex, WritableWorkbook workbook) {

		WritableSheet sheet = workbook.getSheet(0);
		
		for (int j = 0; j < colsIndex.length; j++) {

			String perText = ""; 	// 合并行的数据值
			int tmp = contextIndex; // 起始行号

			for (int i = contextIndex; i <= sheet.getRows(); i++) {
				String cellText = "";
				if (i != sheet.getRows()) {
					Cell cell = sheet.getCell(colsIndex[j], i);
					cellText = cell.getContents().trim();
				}
				if (perText.equals(cellText)) {
					tmp += 1;
				} else if (i > contextIndex) {
					try {
						if(tmp>contextIndex){
							sheet.mergeCells(colsIndex[j], i - tmp - 1 + contextIndex, colsIndex[j], i - 1);
						}
						tmp = contextIndex;
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
				perText = cellText;
			}
		}
	}

}
