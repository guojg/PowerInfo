package com.github.common.export.controller;



import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value ="/export")
public class ExportController {
	@RequestMapping(value ="/index")
	public @ResponseBody void index(HttpServletRequest request, HttpServletResponse response){
		 try {
	            request.setCharacterEncoding("UTF-8");
	        } catch (UnsupportedEncodingException e1) {
	            e1.printStackTrace();
	        }
	        String reportTitle = request.getParameter("tabTitle");//前台报表封装数据
	        String tabName = request.getParameter("tabName");//接受报表的名称
	        String remarks = request.getParameter("remarks");//接受报表的备注
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//格式时间申明，导出报表文件名需要。文件名格式为 报表名称+yyyymmdd
	        // 创建一个新的文件
	        WritableWorkbook book =null;
	        try {
	            //前台加过编码格式，后台进行解码
	            reportTitle = URLDecoder.decode(URLDecoder.decode(reportTitle, "UTF-8"),"UTF-8");
	            //前台加过编码，后台进行解码
	            tabName = URLDecoder.decode(URLDecoder.decode(tabName, "UTF-8"),"UTF-8");
	            if(remarks != null){
	                remarks = URLDecoder.decode(URLDecoder.decode(remarks, "UTF-8"),"UTF-8");
	            }
	            //格式化时间
	            String data = sdf.format(new Date());
	            //将前台报表数组转成JSONArray类型
	            JSONArray jsonArray = JSONArray.fromObject(reportTitle);
	            //申明response 报文协议
	            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	            response.setCharacterEncoding("UTF-8");
	            String fileName =  new String( tabName.getBytes("gb2312"), "ISO8859-1" ) +data+".xls";
	            fileName = fileName.replace("+", "%20");
	            response.setHeader("Content-disposition", "attachment;filename="+fileName);
	            OutputStream os= response.getOutputStream();
	           
	            // 创建一个新的文件
	            book  =  Workbook.createWorkbook(os);// 写入的制定输出流
	            WritableSheet wsheet  =  book.createSheet( "Sheet1" ,  0 );
	           
	            
	            // 处理表头 第三行
	            WritableFont font = new WritableFont(WritableFont.createFont("宋体"),11); 
	            WritableCellFormat headerFormat =  new WritableCellFormat();
	            headerFormat.setFont(font);
	            headerFormat.setWrap(true);//自动换行
	            headerFormat.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
	            headerFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
	            headerFormat.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
	            
	            WritableCellFormat headerBGFormat =  new WritableCellFormat();
	            headerBGFormat.setFont(font);
	            headerBGFormat.setWrap(true);//自动换行
	            headerBGFormat.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
	            headerBGFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
	            headerBGFormat.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
	            headerBGFormat.setBackground(Colour.GRAY_25);
	            
	            JSONArray rowInfos = null;//申明行数组
	            Map<String, Object> json = null;//申明单元格对象
	            int cellIndex = 0;//一行单元格最后的一个索引，用来合并表头
	            int cellNumber = 0;//单元格索引
	            int hiddenRowNumber = 0;//要隐藏的行号
	            //循环前台封装的数组
	            for(int row = 0; row < jsonArray.size(); row++){
	                rowInfos = (JSONArray)jsonArray.get(row);//获取到单行的数组信息
	                //循环每个单元格
	                for(int cell = 0; cell < rowInfos.size(); cell++){
	                    json = (Map<String, Object>)rowInfos.get(cell);//获取到单元格信息
	                    String textJson="";
                    	if(json.get("text") != null){
                    		textJson = json.get("text").toString() ;
                    	}
	                        //jxl合并单元格方法
	                        wsheet.mergeCells(Integer.parseInt(json.get("index").toString())-1, row+1, (Integer.parseInt(json.get("index").toString())+Integer.parseInt(json.get("colspan").toString())-2), row+(Integer.parseInt(json.get("rowspan").toString())));
	                        if(json.get("bg") == null){//如果没有有bg这个信息那么就不是维度信息
	                            //创建单元格信息
	                        	String val = textJson;
	                        	if(val.contains("m2")) val = val.replace("m2", "m"+(char)(178));//带m2的转换为m方
	                            wsheet.addCell(new Label(Integer.parseInt(json.get("index").toString())-1, row+1,val,headerFormat));
	                        }else{//如果是维度信息
	                        	
	                            if(textJson.equals("维度")){
	                                hiddenRowNumber = row;//将维度信息行号赋值给hiddenRowNumber，后面隐藏行号需要
	                            }
	                            //创建单元格信息
	                            wsheet.addCell(new Label(Integer.parseInt(json.get("index").toString())-1, row+1,textJson,headerBGFormat));
	                        }
	                        //获取单元格内容索引
	                        cellNumber = Integer.parseInt(json.get("index").toString())-1;
	                   if(textJson.length()>3){
	                     //设置单元格宽度
	                       wsheet.setColumnView(cellNumber, 20);
	                   }
	                }
	                //获取最后的一个单元格
	                cellIndex = cellIndex < (Integer.parseInt(json.get("index").toString())+Integer.parseInt(json.get("colspan").toString())-2) ? (Integer.parseInt(json.get("index").toString())+Integer.parseInt(json.get("colspan").toString())-2) : cellIndex;
	            }
	            //标题处理  
	            wsheet.mergeCells(0, 0, cellIndex, 0);// 标题合并单元格
	            wsheet.setRowView(0, 600);// 设置行高为200
	            wsheet.setRowView(1, 300);
	            wsheet.setRowView(hiddenRowNumber+1, 0);//隐藏行维度
	            wsheet.setColumnView(0, 0);//隐藏列维度
	            
	            WritableCellFormat titleformat =  new WritableCellFormat();
	            Label titleLabel = new  Label(0, 0,tabName,titleformat) ;
	            titleformat.setWrap(true);//自动换行
	            titleformat.setAlignment(jxl.format.Alignment.CENTRE);//水平居左
	            titleformat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
	            titleformat.setFont(new WritableFont(WritableFont.createFont("宋体"),14,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.GREEN));//粗体
	            titleformat.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
	            wsheet.addCell(titleLabel);
	            
	            if(remarks != null){
	                //备注处理  
	            	int row_count = 5;
	            	for(int i=0;i<remarks.length();i++){
	            		if(remarks.charAt(i)=='\r'){
	            			row_count++;
	            		}
	            	}
	                wsheet.mergeCells(0, jsonArray.size()+2, cellIndex, jsonArray.size()+row_count);// 标题合并单元格
	                WritableCellFormat remarksFormat =  new WritableCellFormat();
	                Label remarksLabel = new  Label( 0 , jsonArray.size()+2 ,remarks,remarksFormat) ;
	                remarksFormat.setWrap(true);//自动换行
	                remarksFormat.setAlignment(jxl.format.Alignment.LEFT);//水平居左
	                remarksFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
	                remarksFormat.setFont(new WritableFont(WritableFont.createFont("宋体"),11));//粗体
	                
	                wsheet.addCell(remarksLabel);
	            }
	            SheetSettings ss=wsheet.getSettings();
	            ss.setHorizontalCentre(true);// 水平居中
	            ss.setOrientation(PageOrientation.LANDSCAPE);// 横向打印
	            ss.setFitWidth(1);//所有列打印到一页
	            //ss.setPaperSize(PaperSize.A4);// 设置默认A2纸张打印
	            ss.setPrintTitlesRow(2, 2);// 设置每页打印表头
	            
	            
	            book.write();// 写入工作薄
	            book.close();// 关闭工作薄
	            
	            os.close();
//	            file.close();
	            
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (RowsExceededException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (WriteException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }finally{
	        }
	}
}
