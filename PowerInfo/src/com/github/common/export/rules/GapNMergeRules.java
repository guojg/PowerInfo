package com.github.common.export.rules;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * 按列合并，合并规则：合并固定的行数
 *
 */
public class GapNMergeRules extends MergeRules {
	
	private int gapNum = 0;
	
	public GapNMergeRules(int gapNum) {
		this.gapNum = gapNum;
	}

	@Override
	public void extendMergeRules(int contextIndex, int[] colsIndex,
			WritableWorkbook workbook) {
		
		WritableSheet sheet = workbook.getSheet(0);
		for(int j = 0; j < colsIndex.length; j++) {
			for(int i = contextIndex + 1; i <= sheet.getRows(); i++) {
				
				if((i - contextIndex) % gapNum == 0) {
					try {
						sheet.mergeCells(colsIndex[j], i, colsIndex[j], i - 1);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				} else if(i == sheet.getRows()) {
					try {
						sheet.mergeCells(colsIndex[j], i - ((i - contextIndex) % gapNum), colsIndex[j], i - 1);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
