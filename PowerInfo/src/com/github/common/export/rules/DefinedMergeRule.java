package com.github.common.export.rules;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DefinedMergeRule {

	public void definedMergeRules(WritableWorkbook workbook, int[][] ints) throws Exception {
		WritableSheet sheet = workbook.getSheet(0);
		for(int i = 0; i < ints.length; i++) {
			int[] is = ints[i];
			sheet.mergeCells(is[0], is[1], is[2], is[3]);
		}
	}
	
}
