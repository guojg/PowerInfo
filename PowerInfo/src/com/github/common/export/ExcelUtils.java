package com.github.common.export;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.github.common.export.rules.DefinedMergeRule;
import com.github.common.export.rules.MergeRules;

import jxl.Cell;
import jxl.Range;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


@SuppressWarnings("unused")
public class ExcelUtils {

	public static final String COLUMNS_SEPARATOR = String.valueOf((char)30);
	
	private static WritableWorkbook workbook = null;
	
	private ExcelParams excelParams;
	
	public ExcelUtils(ExcelParams excelParams) {
		this.excelParams = excelParams;
	}
	private WritableWorkbook createExcel(OutputStream os, List<MergeRules> rules, int[][] colsIndex, int[][] defineds) throws Exception{
    	DecimalFormat df = new DecimalFormat("#.#########");
		String fileName	= excelParams.getFileName();
		String[] excelName	= excelParams.getExcelTitle();
		Alignment[] excelAlign	= excelParams.getExcelAlign();
		String[] colTitle 	= excelParams.getColTitle();
		String[] colName   	= excelParams.getColName();
		String[] colWidth	= excelParams.getColWidth();
		Alignment[] colAlign	= excelParams.getColAlign();
		String colLock 		= excelParams.getColLock();
		String rowLock		= excelParams.getRowLock();
		List<Map<String, Object>> list = excelParams.getList();
		
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		fileName = fileName == null ? today : fileName + today;
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		int mus=65000; 
		int totalList = list.size();
		
		int avg = totalList/mus ;
		int favg = avg ;
		int sy =totalList%mus ;
		if(sy==0&&avg!=0){}
		else{
			avg+=1;
		}
		for( int si=0; si< avg ; ++si){
			String nameshow="" ;
			if((favg==1 && sy==0)||(favg==0)){
				 nameshow=fileName ;
			}else{
				nameshow= fileName+"-sheet"+(si+1) ;
			}
			WritableSheet sheet = workbook.createSheet(nameshow, si);
			
			SheetSettings sheetSet =  sheet.getSettings();
			
			int manyTitle = 1;				//表头默认一行
			
			/*------------设置默认条件-----开始位置--------*/
			
			if(colTitle != null && colTitle.length > 0) {
				for(String title : colTitle) {
					String[] colTitles = title.split(COLUMNS_SEPARATOR);
					manyTitle = Math.max(manyTitle, colTitles.length);
				}
			}
			
			if(colWidth == null || colWidth.length == 0) {
				colWidth = new String[colName.length];
				for(int i = 0; i < colName.length; i++) {
					colWidth[i] = "20";
				}
			}
			
			if(colAlign == null || colAlign.length == 0) {
				colAlign = new Alignment[colName.length];
				for(int i = 0; i < colName.length; i++) {
					colAlign[i] = Alignment.CENTRE;
				}
			}
			
			if(excelAlign == null || excelAlign.length == 0) {
				excelAlign = new Alignment[excelName.length];
				excelAlign[0] = Alignment.CENTRE;
				for(int i = 1; i < excelName.length; i++) {
					excelAlign[i] = Alignment.RIGHT;
				}
			}
			
			if(rowLock == null || "".equals(rowLock)) {
				rowLock = String.valueOf(excelName.length + manyTitle - 1);
			}
			
			/*------------设置默认条件-----结束位置--------*/
			
			int colLength 		= colName.length;								//导出excel的总列数
			int rowLength		= 0;	//导出excel的总行数

			if(si<favg){
				rowLength		= excelName.length + manyTitle + mus;	//导出excel的总行数

			}else{
				rowLength		= excelName.length + manyTitle + sy;	//导出excel的总行数
			}
			int coltitleIndex	= excelName.length;								//表头起始行
			int contextIndex	= excelName.length + manyTitle;					//数据起始行，即表头的下一行索引，从0开始

			
			colTitle = autocomplete(colTitle, manyTitle);
			
			//列宽
			for(int i = 0; i < colWidth.length; i++) {
				sheet.setColumnView(i, Integer.valueOf(colWidth[i]));
			}
			//页边距
			sheet.getSettings().setRightMargin(0.5);
			//单元格字体
			WritableFont tableContextFont	= new WritableFont(WritableFont.ARIAL, 10);
	        WritableFont tableHeadFont 		= new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
			
	        //标题内容设置
	        for(int i = 0; i < excelName.length; i++) {
	        	//标题格式
	            WritableFont tableTitleFont	= new WritableFont(WritableFont.ARIAL, 12,WritableFont.BOLD);
	            WritableCellFormat tabletitle = new WritableCellFormat(tableTitleFont);
	            //无边线
	            tabletitle.setBorder(Border.NONE, BorderLineStyle.THIN);
	            //垂直对齐
	            tabletitle.setVerticalAlignment(VerticalAlignment.CENTRE);
	            //背景色
	            tabletitle.setBackground(Colour.LIGHT_GREEN);
	        	sheet.mergeCells(0, i, colLength - 1, i);
	        	//水平对齐
	            tabletitle.setAlignment(excelAlign[i]);
	        	sheet.addCell(new Label(0, i, excelName[i], tabletitle));
	        	//行高
	        	sheet.setRowView(i, i == 0 ? 600 : 300);
	        }
	        
	        //固定行,索引从1开始
	        for(int i = 0; i <= Integer.valueOf(rowLock); i++) {
	        	sheetSet.setVerticalFreeze(i + 1);
	        }
	        //固定列,索引从1开始
	        if(colLock != null && !"".equals(colLock)) {
	        	for(int i = 0; i <= Integer.valueOf(colLock); i++) {
	        		sheetSet.setHorizontalFreeze(i);
	        	}
	        }
	        
	        WritableCellFormat tableHead = new WritableCellFormat(tableHeadFont);
	        tableHead.setBorder(Border.ALL, BorderLineStyle.THIN); 
	        tableHead.setVerticalAlignment(VerticalAlignment.CENTRE);
	        tableHead.setAlignment(Alignment.CENTRE);
	        tableHead.setBackground(Colour.SEA_GREEN);
	        tableHeadFont.setColour(Colour.WHITE);//设置字体颜色
	        
	        //表头数据填充
	        for(int i = 0; i < colTitle.length; i++) {
	        	for(int j = 0; j < manyTitle; j++) {
	        		sheet.addCell(new Label(i, j + coltitleIndex, colTitle[i].split(COLUMNS_SEPARATOR)[j], tableHead));
	        	}
	        }
	        
	        //表头数据合并
//	        titleMerge(sheet, coltitleIndex, colTitle, manyTitle, tableHead);
	        titleMerge(sheet, coltitleIndex, colTitle, manyTitle);
	        
	    	if(list != null && list.size() > 0 && !list.isEmpty()) {
				for(int i = contextIndex; i < rowLength; i++) {
					Map<String, Object> map = list.get((i+mus*(si) - contextIndex));
					if(map != null && map.size() > 0 && !map.isEmpty()) {
						sheet.setRowView(i, 360);
						for(int j = 0; j < colLength; j++) {
							WritableCellFormat tableContext = new WritableCellFormat(tableContextFont);
				    		tableContext.setBorder(Border.ALL, BorderLineStyle.THIN); 
							tableContext.setVerticalAlignment(VerticalAlignment.CENTRE);
							tableContext.setWrap(true);			//自动换行
							Object obj = map.get(colName[j]);
							
		    				tableContext.setAlignment(colAlign[j]);
		    				// 判断数据类型后插入
		    				if(isNum(obj) && obj !=null) {
		    					sheet.addCell(new Number(j, i, Double.parseDouble(df.format(((BigDecimal)obj).doubleValue())), tableContext));
		    				} else {
		    					sheet.addCell(new Label(j, i, obj == null ? "" : obj.toString(), tableContext));
		    				}
		    			}
	    			}
	    		}
	    	}
	    	
	    	WorkbookSettings wks=new WorkbookSettings();
			wks.setCellValidationDisabled(false);
			wks.setSuppressWarnings(false);
			
			if(defineds != null) {
				new DefinedMergeRule().definedMergeRules(workbook, defineds);
			}
			
			if(rules != null && rules.size() > 0 && !rules.isEmpty()) {
				for(int i = 0; i < rules.size(); i++) {
					MergeRules rule = rules.get(i);
					rule.extendMergeRules(contextIndex, colsIndex[i], workbook);
				}
			}
		}	
		return workbook;
	}
	
	public void exportExcel(HttpServletResponse response, List<MergeRules> rules, int[][] colsIndex, int[][] defineds){
		OutputStream os = null;
		String fileName	= excelParams.getFileName();
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		fileName = fileName == null ? today + ".xls" : fileName + today + ".xls";
		WritableWorkbook wk = null;
		try {
			os = response.getOutputStream();
			response.setHeader("content-disposition", "attachment ; filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1"));
			response.setContentType("application/x-msdownload");
			wk = createExcel(os, rules, colsIndex, defineds);
			wk.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(wk != null) {
					wk.close();
				}
				if(os != null) {
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
private WritableWorkbook createExcel1(OutputStream os, List<MergeRules> rules, int[][] colsIndex, int[][] defineds) throws Exception{
		
		String fileName	= excelParams.getFileName();
		String[] excelName	= excelParams.getExcelTitle();
		Alignment[] excelAlign	= excelParams.getExcelAlign();
		String[] colTitle 	= excelParams.getColTitle();
		String[] colName   	= excelParams.getColName();
		String[] colWidth	= excelParams.getColWidth();
		Alignment[] colAlign	= excelParams.getColAlign();
		String colLock 		= excelParams.getColLock();
		String rowLock		= excelParams.getRowLock();
		List<Map<String, Object>> list = excelParams.getList();
		
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		fileName = fileName == null ? today : fileName + today;
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		int mus=65000; 
		int totalList = list.size();
		
		int avg = totalList/mus ;
		int favg = avg ;
		int sy =totalList%mus ;
		if(sy==0&&avg!=0){}
		else{
			avg+=1;
		}
		for( int si=0; si< avg ; ++si){
			String nameshow="" ;
			if((favg==1 && sy==0)||(favg==0)){
				 nameshow=fileName ;
			}else{
				nameshow= fileName+"-sheet"+(si+1) ;
			}
			WritableSheet sheet = workbook.createSheet(nameshow, si);
			
			SheetSettings sheetSet =  sheet.getSettings();
			
			int manyTitle = 1;				//表头默认一行
			
			/*------------设置默认条件-----开始位置--------*/
			
			if(colTitle != null && colTitle.length > 0) {
				for(String title : colTitle) {
					String[] colTitles = title.split(COLUMNS_SEPARATOR);
					manyTitle = Math.max(manyTitle, colTitles.length);
				}
			}
			
			if(colWidth == null || colWidth.length == 0) {
				colWidth = new String[colName.length];
				for(int i = 0; i < colName.length; i++) {
					colWidth[i] = "20";
				}
			}
			
			if(colAlign == null || colAlign.length == 0) {
				colAlign = new Alignment[colName.length];
				for(int i = 0; i < colName.length; i++) {
					colAlign[i] = Alignment.CENTRE;
				}
			}
			
			if(excelAlign == null || excelAlign.length == 0) {
				excelAlign = new Alignment[excelName.length];
				excelAlign[0] = Alignment.CENTRE;
				for(int i = 1; i < excelName.length; i++) {
					excelAlign[i] = Alignment.RIGHT;
				}
			}
			
			if(rowLock == null || "".equals(rowLock)) {
				rowLock = String.valueOf(excelName.length + manyTitle - 1);
			}
			
			/*------------设置默认条件-----结束位置--------*/
			
			int colLength 		= colName.length;								//导出excel的总列数
			int rowLength		= 0;	//导出excel的总行数

			if(si<favg){
				rowLength		= excelName.length + manyTitle + mus;	//导出excel的总行数

			}else{
				rowLength		= excelName.length + manyTitle + sy;	//导出excel的总行数
			}
			int coltitleIndex	= excelName.length;								//表头起始行
			int contextIndex	= excelName.length + manyTitle;					//数据起始行，即表头的下一行索引，从0开始

			
			colTitle = autocomplete(colTitle, manyTitle);
			
			//列宽
			for(int i = 0; i < colWidth.length; i++) {
				sheet.setColumnView(i, Integer.valueOf(colWidth[i]));
			}
			//页边距
			sheet.getSettings().setRightMargin(0.5);
			//单元格字体
			WritableFont tableContextFont	= new WritableFont(WritableFont.ARIAL, 10);
	        WritableFont tableHeadFont 		= new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
			
	        //标题内容设置
	        for(int i = 0; i < excelName.length; i++) {
	        	//标题格式
	            WritableFont tableTitleFont	= new WritableFont(WritableFont.ARIAL, 12,WritableFont.BOLD);
	            WritableCellFormat tabletitle = new WritableCellFormat(tableTitleFont);
	            //无边线
	            tabletitle.setBorder(Border.NONE, BorderLineStyle.THIN);
	            //垂直对齐
	            tabletitle.setVerticalAlignment(VerticalAlignment.CENTRE);
	            //背景色
	            tabletitle.setBackground(Colour.LIGHT_GREEN);
	        	sheet.mergeCells(0, i, colLength - 1, i);
	        	//水平对齐
	            tabletitle.setAlignment(excelAlign[i]);
	        	sheet.addCell(new Label(0, i, excelName[i], tabletitle));
	        	//行高
	        	sheet.setRowView(i, i == 0 ? 600 : 300);
	        }
	        
	        //固定行,索引从1开始
	        for(int i = 0; i <= Integer.valueOf(rowLock); i++) {
	        	sheetSet.setVerticalFreeze(i + 1);
	        }
	        //固定列,索引从1开始
	        if(colLock != null && !"".equals(colLock)) {
	        	for(int i = 0; i <= Integer.valueOf(colLock); i++) {
	        		sheetSet.setHorizontalFreeze(i);
	        	}
	        }
	        
	        WritableCellFormat tableHead = new WritableCellFormat(tableHeadFont);
	        tableHead.setBorder(Border.ALL, BorderLineStyle.THIN); 
	        tableHead.setVerticalAlignment(VerticalAlignment.CENTRE);
	        tableHead.setAlignment(Alignment.CENTRE);
	        tableHead.setBackground(Colour.SEA_GREEN);
	        tableHeadFont.setColour(Colour.WHITE);//设置字体颜色
	        
	        //表头数据填充
	        for(int i = 0; i < colTitle.length; i++) {
	        	for(int j = 0; j < manyTitle; j++) {
	        		sheet.addCell(new Label(i, j + coltitleIndex, colTitle[i].split(COLUMNS_SEPARATOR)[j], tableHead));
	        	}
	        }
	        
	        //表头数据合并
//	        titleMerge(sheet, coltitleIndex, colTitle, manyTitle, tableHead);
	        titleMerge(sheet, coltitleIndex, colTitle, manyTitle);
	        
	    	if(list != null && list.size() > 0 && !list.isEmpty()) {
				for(int i = contextIndex; i < rowLength; i++) {
					Map<String, Object> map = list.get((i+mus*(si) - contextIndex));
					if(map != null && map.size() > 0 && !map.isEmpty()) {
						sheet.setRowView(i, 360);
						for(int j = 0; j < colLength; j++) {
							WritableCellFormat tableContext = new WritableCellFormat(tableContextFont);
				    		tableContext.setBorder(Border.ALL, BorderLineStyle.THIN); 
							tableContext.setVerticalAlignment(VerticalAlignment.CENTRE);
							tableContext.setWrap(true);			//自动换行
							Object obj = map.get(colName[j]);
							
		    				tableContext.setAlignment(colAlign[j]);
		    				// 判断数据类型后插入
//		    				if(isNum(obj)) {
//		    					sheet.addCell(new Number(j, i, obj == null ? 0 : ((BigDecimal)obj).doubleValue(), tableContext));
//		    				} else {
		    					sheet.addCell(new Label(j, i, obj == null ? "" : obj.toString(), tableContext));
//		    				}
		    			}
	    			}
	    		}
	    	}
	    	
	    	WorkbookSettings wks=new WorkbookSettings();
			wks.setCellValidationDisabled(false);
			wks.setSuppressWarnings(false);
			
			if(defineds != null) {
				new DefinedMergeRule().definedMergeRules(workbook, defineds);
			}
			
			if(rules != null && rules.size() > 0 && !rules.isEmpty()) {
				for(int i = 0; i < rules.size(); i++) {
					MergeRules rule = rules.get(i);
					rule.extendMergeRules(contextIndex, colsIndex[i], workbook);
				}
			}
		}	
		return workbook;
	}
	
	public void exportExcel1(HttpServletResponse response, List<MergeRules> rules, int[][] colsIndex, int[][] defineds){
		OutputStream os = null;
		String fileName	= excelParams.getFileName();
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		fileName = fileName == null ? today + ".xls" : fileName + today + ".xls";
		WritableWorkbook wk = null;
		try {
			os = response.getOutputStream();
			response.setHeader("content-disposition", "attachment ; filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1"));
			response.setContentType("application/x-msdownload");
			wk = createExcel1(os, rules, colsIndex, defineds);
			wk.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(wk != null) {
					wk.close();
				}
				if(os != null) {
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void exportExcelForSDW(String path, List<MergeRules> rules, int[][] colsIndex, int[][] defineds){
		OutputStream os = null;
//		String fileName	= excelParams.getFileName();
//		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		fileName = fileName == null ? today + ".xls" : fileName + today + ".xls";
		WritableWorkbook wk = null;
		try {
			

	            File file =new File(path);
	            os = new FileOutputStream(file);
//			os = response.getOutputStream();
//			response.setHeader("content-disposition", "attachment ; filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1"));
//			response.setContentType("application/x-msdownload");
			wk = createExcel(os, rules, colsIndex, defineds);
			wk.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(wk != null) {
					wk.close();
				}
				if(os != null) {
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void exportExcel(HttpServletResponse response, List<MergeRules> rules, int[][] colsIndex){
		this.exportExcel(response, rules, colsIndex, null);
	} 
	public void exportExcel1(HttpServletResponse response, List<MergeRules> rules, int[][] colsIndex){
		this.exportExcel1(response, rules, colsIndex, null);
	} 
	public void exportExcel(String path, List<MergeRules> rules, int[][] colsIndex){
		this.exportExcelForSDW(path, rules, colsIndex, null);
	} 
	public void exportExcel(HttpServletResponse response, int[][] defineds){
		this.exportExcel(response, null, null, defineds);
	} 
	
	public void exportExcel(HttpServletResponse response){
		this.exportExcel(response, null, null, null);
	} 
	
	private static boolean isNum(Object numStr) {
		if(numStr == null) return false;
		if(numStr instanceof BigDecimal) {
			return true;
		}
		return false;
	}
	
	
	private static String[] autocomplete(String[] cols, int rowNum) {
		for(int i = 0; i< cols.length; i++) {
			cols[i] = completeStr(cols[i], rowNum - cols[i].split(COLUMNS_SEPARATOR).length);
		}
		return cols;
	}
	
	private static String completeStr(String col, int count) {
		if (count == 0) {
			return col;
		}
		col = col.split(COLUMNS_SEPARATOR)[0] + COLUMNS_SEPARATOR + col;
		return completeStr(col, --count);
		/*
		if (count == 1) {
			return col;
		} else {
			return completeStr(col, --count);
		}
		*/
		
	}
	
	private static void titleMerge(WritableSheet sheet, int coltitleIndex, String[] colTitles, int num, WritableCellFormat tableHead) throws RowsExceededException, WriteException{
		for(int i = 0; i < colTitles.length; i++) {
			String[] cols = colTitles[i].split(COLUMNS_SEPARATOR);
			int rowTmp = 1;
			int colTmp = 1;
			
			for(int j = 0; j < cols.length - 1; j++) {
				if(!cols[j].equals(cols[j + 1])){
					break;
				}
				rowTmp += 1;
			}
			
			for(int k = i + 1; k < colTitles.length; k++) {
				String[] cols2 = colTitles[k].split(COLUMNS_SEPARATOR);
				if(!cols[0].equals(cols2[0])) {
					break;	
				}
				colTmp += 1;
			}
			
			sheet.mergeCells(i, coltitleIndex, i + colTmp - 1, coltitleIndex + rowTmp - 1);
			
			i += colTmp - 1;
		}
	}
	
	
	private void titleMerge(WritableSheet sheet, int coltitleIndex, String[] colTitles, int num) throws RowsExceededException, WriteException {
        for(int i = 0; i < num; i++) {
            String preVal = "";
            int columnIndex = 0;
            for(int j = 0; j < colTitles.length; j++) {
                columnIndex++;
                preVal = colTitles[j].split(COLUMNS_SEPARATOR)[i];
                String columnVal = "";
                try {
                    columnVal = colTitles[j + 1].split(COLUMNS_SEPARATOR)[i];
                } catch (Exception e1) {
                    List<Integer> list = new ArrayList<Integer>();
                    for(int m = j - columnIndex + 1; m <= j; m++) {
                        int bottom = 0;
                        for(int k = i + 1; k <= num; k++) {
                            String nextRowVal = "";
                            try {
                                nextRowVal = colTitles[m].split(COLUMNS_SEPARATOR)[k];
                            } catch (Exception e) {
                                list.add(bottom);
                            }
                            if(!"".equals(nextRowVal) && !preVal.equals(nextRowVal)) {
                                list.add(bottom);
                                break;
                            }
                            bottom++;
                        }
                    }
                    Collections.sort(list);    //从小到大
                    if((list.size() > 0 && list.get(0) != 0) || columnIndex != 1){
                        if(this.isCellMerged(sheet, coltitleIndex + i, j  - columnIndex + 1) == null ){
//                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//                            System.out.print(j  - columnIndex + 1);
//                            System.out.print(" : ");
//                            System.out.print(coltitleIndex + i);
//                            System.out.print(", ");
//                            System.out.print(j);
//                            System.out.print(" : ");
//                            System.out.print(coltitleIndex + i + list.get(0));
//                            System.out.println();
//                            System.out.println("-----------------------------");
                            sheet.mergeCells(j  - columnIndex + 1, coltitleIndex + i, j, coltitleIndex + i + list.get(0));
                        }
                    }
                    columnIndex = 0;
                }
                if(!"".equals(columnVal) && !preVal.equals(columnVal)) {
                    List<Integer> list = new ArrayList<Integer>();
                    for(int m = j - columnIndex + 1; m <= j; m++) {
                        int bottom = 0;
                        for(int k = i + 1; k <= num; k++) {
                            String nextRowVal = "";
                            try {
                                nextRowVal = colTitles[m].split(COLUMNS_SEPARATOR)[k];
                            } catch (Exception e) {
                                list.add(bottom);
                            }
                            if(!"".equals(nextRowVal) && !preVal.equals(nextRowVal)) {
                                list.add(bottom);
                                break;
                            }
                            bottom++;
                        }
                    }
                    Collections.sort(list);    //从小到大
                    if((list.size() > 0 && list.get(0) != 0) || columnIndex != 1){
                        if(this.isCellMerged(sheet, coltitleIndex + i, j  - columnIndex + 1) == null ){
//                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//                            System.out.print(j  - columnIndex + 1);
//                            System.out.print(" : ");
//                            System.out.print(coltitleIndex + i);
//                            System.out.print(", ");
//                            System.out.print(j);
//                            System.out.print(" : ");
//                            System.out.print(coltitleIndex + i + list.get(0));
//                            System.out.println();
//                            System.out.println("-----------------------------");
                            sheet.mergeCells(j  - columnIndex + 1, coltitleIndex + i, j, coltitleIndex + i + list.get(0));
                        }
                    }
                    columnIndex = 0;
                }
                preVal = columnVal;
            }
        }
    }
	
	private Range isCellMerged(WritableSheet sheet, int r, int c) {
        Range[] rs = sheet.getMergedCells();
        for(int i = 0; i < rs.length; i++) {
            Cell c1 = rs[i].getTopLeft();       //起始
            Cell c2 = rs[i].getBottomRight();   //终止
            //区间 row[c1.getRow(), c2.getRow()], column[c1.getColumn(), c2.getColumn()]
            if(r >= c1.getRow() && r <= c2.getRow() && c >= c1.getColumn() && c <= c2.getColumn()) {
                return rs[i];
            }
        }
        return null;
    }
	
	public static void main(String[] args) {
    
	    int i = 1;
	    Integer j = 2;
	    System.out.println(i + j);
	
    }
	
}
