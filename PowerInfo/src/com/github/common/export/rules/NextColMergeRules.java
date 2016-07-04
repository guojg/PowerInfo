package com.github.common.export.rules;

import java.util.ArrayList;
import java.util.List;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 按列合并，合并规则：按前一列的合并样式进行合并
 * 
 */
public class NextColMergeRules extends MergeRules {

    @Override
    public void extendMergeRules(int contextIndex, int[] colsIndex, WritableWorkbook workbook) {
        WritableSheet sheet = workbook.getSheet(0);
        boolean isNext = false;
        List<List<Integer>> prevMerges = new ArrayList<List<Integer>>();
        for (int j = 0; j < colsIndex.length; j++) {
            String perText = "";
            int tmp = contextIndex;
            List<Integer> prevMerge = new ArrayList<Integer>();
            if (j - 1 >= 0 && colsIndex[j] - colsIndex[j - 1] == 1){
                isNext = true;
            }
            for (int i = contextIndex; i <= sheet.getRows(); i++) {
                String cellText = "";
                if (i == sheet.getRows() + contextIndex) {
                    cellText = "";
                } else {
                    cellText = sheet.getCell(colsIndex[j], i).getContents().trim();
                }
                if (perText.equals(cellText)) {
                    if (isNext) {
                        boolean isMerge = false;
                        for (int k = 0; k < prevMerges.get(j - 1).size(); k++) {
                            if (i - tmp + contextIndex - 1 <= prevMerges.get(j - 1).get(k) - 1 && i > prevMerges.get(j - 1).get(k) - 1) {
//                                if (i - tmp + contextIndex - 1 < prevMerges.get(j - 1).get(k) - 1 && i - 1 >= prevMerges.get(j - 1).get(k) - 1) {
                                isMerge = true;
                                break;
                            }
                        }
                        if (isMerge) {
//                            if (isMerge && i != contextIndex) {
                            try {
                                sheet.mergeCells(colsIndex[j], i - tmp - 1 + contextIndex, colsIndex[j], i - 1);
                            } catch (RowsExceededException e) {
                                e.printStackTrace();
                            } catch (WriteException e) {
                                e.printStackTrace();
                            }
                            prevMerge.add(i - tmp - 1 + contextIndex);
                            tmp = contextIndex - 1;;
                        }
                    }
                    tmp += 1;
                } else {
//                    if(i != contextIndex) {
                        try {
                            sheet.mergeCells(colsIndex[j], i - tmp - 1 + contextIndex, colsIndex[j], i - 1);
                        } catch (RowsExceededException e) {
                            e.printStackTrace();
                        } catch (WriteException e) {
                            e.printStackTrace();
                        }
                        prevMerge.add(i - tmp - 1 + contextIndex);
                        tmp = contextIndex;
//                    }
                }
                perText = cellText;
            }
            prevMerges.add(prevMerge);
        }
    }
}