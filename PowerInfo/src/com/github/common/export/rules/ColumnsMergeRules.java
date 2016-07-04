package com.github.common.export.rules;

import jxl.Cell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ColumnsMergeRules extends MergeRules{

	@Override
	public void extendMergeRules(int contextIndex, int[] colsIndex,
			WritableWorkbook workbook) {
		WritableSheet sheet = workbook.getSheet(0);
		int sartColumnsIndex = 0;
		int endColumnsIndex = colsIndex[colsIndex.length-1];
		
		int perColumnsIndex = endColumnsIndex -1;
		
		for (int i = contextIndex; i <= sheet.getRows(); i++) {
			if(i==contextIndex){
				sartColumnsIndex = 0;
			}else{
				sartColumnsIndex = colsIndex[0];
			}
			
			Cell cell = sheet.getCell(endColumnsIndex, i);
			
			String cellText = cell.getContents().trim();
			if(cellText==null||"".equals(cellText)){
				
				while(perColumnsIndex>=sartColumnsIndex){
					String perCellText = sheet.getCell(perColumnsIndex, i).getContents().trim();
					if(perCellText==null||"".equals(perCellText)){
						perColumnsIndex--;
					}else{
						try {
							sheet.mergeCells(perColumnsIndex, i, endColumnsIndex, i);
							perColumnsIndex = endColumnsIndex -1;
							break;
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
		
	

}
