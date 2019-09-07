package com.supadata.utils;


import com.supadata.pojo.Click;
import com.supadata.pojo.StudentCard;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 类说明: 
 * @ClassName: ExcelUtil
 * @author: pengxiuxiao
 * @date: 2018年8月14日 上午10:37:01
*/
public class ExcelUtil {

	public static void main(String[] args) throws Exception {
 
		String sheetName = "付费用户";
		String fileName = "智联信息表";
		int columnNumber = 12;
//		String[][] dataList = { { "001", "2015-01-01", "IT" },
//				{ "002", "2015-01-02", "市场部" }, { "003", "2015-01-03", "测试" } };
		int[] columnWidth =   { 8, 15, 50, 15, 15, 15, 15, 5, 5, 40, 10, 10 };
		String[] columnName = { "用户名", "支付标识", "订单id", "订单价格", "实付金额", "视频名称", "讲师姓名", "下单时间" };
//		new ExcelUtil().ExportNoResponse(sheetName, fileName,
//				columnNumber, columnWidth, columnName, dataList);
//		new ExcelUtil().ExportWithResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, response);
	}

//	public static void ExportWithResponse(String sheetName, String fileName, int columnNumber, int[] columnWidth,
//			String[] columnName, List<Order> orders, HttpServletResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
	public static void ExportWithResponse(String sheetName, String fileName, int columnNumber, int[] columnWidth,
										  String[] columnName, List<StudentCard> dataList, HttpServletResponse response) throws Exception {
		
		
		if (columnNumber == columnWidth.length&& columnWidth.length == columnName.length) {
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(sheetName);
			// sheet.setDefaultColumnWidth(15); //统一设置列宽
			for (int i = 0; i < columnNumber; i++) 
			{
				for (int j = 0; j <= i; j++) 
				{
					if (i == j) 
					{
						sheet.setColumnWidth(i, columnWidth[j] * 256); // 单独设置每列的宽
					}
				}
			} 
			// 创建第1行 也就是表头
			HSSFRow row = sheet.createRow((int) 0);
			row.setHeightInPoints(37);// 设置表头高度
 
			// 第四步，创建表头单元格样式 以及表头的字体样式
			HSSFCellStyle style = wb.createCellStyle();
			style.setWrapText(true);// 设置自动换行
//			style.setAlignment(HSSFCellStyle);
//			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式
 
			style.setBottomBorderColor(HSSFColor.BLACK.index);
//			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
 
			HSSFFont headerFont = (HSSFFont) wb.createFont(); // 创建字体样式
//			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			headerFont.setFontName("黑体"); // 设置字体类型
			headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
			style.setFont(headerFont); // 为标题样式设置字体样式
 
			// 第四.一步，创建表头的列
			for (int i = 0; i < columnNumber; i++) 
			{
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(style);
			}
 
			// 第五步，创建单元格，并设置值

			// 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
			HSSFCellStyle zidonghuanhang2 = wb.createCellStyle();
			zidonghuanhang2.setWrapText(true);// 设置自动换行
//			zidonghuanhang2
//					.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个上下居中格式
//				zidonghuanhang2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中

			// 设置边框
			zidonghuanhang2.setBottomBorderColor(HSSFColor.BLACK.index);
//			zidonghuanhang2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			zidonghuanhang2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			zidonghuanhang2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			zidonghuanhang2.setBorderTop(HSSFCellStyle.BORDER_THIN);


			//日期格式设置

			HSSFCellStyle cellStyle2 = wb.createCellStyle();
			HSSFDataFormat format = wb.createDataFormat();
			cellStyle2.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
			//上下居中
//			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			// 设置边框
			cellStyle2.setBottomBorderColor(HSSFColor.BLACK.index);
//			cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);

			for (int i = 0; i < dataList.size(); i++) 
			{
				row = sheet.createRow((int) i + 1);
				row.setHeightInPoints(20);// 设置表头高度

				//第一列不设置值
				HSSFCell datacell = null;
				int flag = 0;
				for (int j = 0; j < columnNumber; j++) {
					datacell = row.createCell(j);
					switch (j) {
					case 0://姓名
						datacell.setCellValue(dataList.get(i).getStudentName());
						break;
					case 1://卡号
						datacell.setCellValue(dataList.get(i).getCardNumber());
						break;
//					case 2://暗码
//						datacell.setCellValue(dataList.get(i).getSecretNumber());
//						break;
					default:
						break;
					}
				}
			}
 
			// 第六步，将文件存到浏览器设置的下载位置
			String filename = fileName + ".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					.concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			OutputStream out = response.getOutputStream();
			try {
				wb.write(out);// 将数据写出去
				String str = "导出" + fileName + "成功！";
				System.out.println(str);
			} catch (Exception e) {
				e.printStackTrace();
				String str1 = "导出" + fileName + "失败！";
				System.out.println(str1);
			} finally {
				out.close();
			}
 
		} else {
			System.out.println("列数目长度名称三个数组长度要一致");
		}
 
	}


	public static void ExportClicksWithResponse(String sheetName, String fileName, int columnNumber, int[] columnWidth,
										  String[] columnName, List<Click> dataList, HttpServletResponse response) throws Exception {


		if (columnNumber == columnWidth.length&& columnWidth.length == columnName.length) {
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(sheetName);
			// sheet.setDefaultColumnWidth(15); //统一设置列宽
			for (int i = 0; i < columnNumber; i++)
			{
				for (int j = 0; j <= i; j++)
				{
					if (i == j)
					{
						sheet.setColumnWidth(i, columnWidth[j] * 256); // 单独设置每列的宽
					}
				}
			}
			// 创建第1行 也就是表头
			HSSFRow row = sheet.createRow((int) 0);
			row.setHeightInPoints(37);// 设置表头高度

			// 第四步，创建表头单元格样式 以及表头的字体样式
			HSSFCellStyle style = wb.createCellStyle();
			style.setWrapText(true);// 设置自动换行
			style.setBottomBorderColor(HSSFColor.BLACK.index);

			HSSFFont headerFont = (HSSFFont) wb.createFont(); // 创建字体样式
			headerFont.setFontName("黑体"); // 设置字体类型
			headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
			style.setFont(headerFont); // 为标题样式设置字体样式

			// 第四.一步，创建表头的列
			for (int i = 0; i < columnNumber; i++)
			{
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(style);
			}

			// 第五步，创建单元格，并设置值

			// 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
			HSSFCellStyle zidonghuanhang2 = wb.createCellStyle();
			zidonghuanhang2.setWrapText(true);// 设置自动换行

			// 设置边框
			zidonghuanhang2.setBottomBorderColor(HSSFColor.BLACK.index);

			//日期格式设置
			HSSFCellStyle cellStyle2 = wb.createCellStyle();
			HSSFDataFormat format = wb.createDataFormat();
			cellStyle2.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));

			// 设置边框
			cellStyle2.setBottomBorderColor(HSSFColor.BLACK.index);


			for (int i = 0; i < dataList.size(); i++)
			{
				row = sheet.createRow((int) i + 1);
				row.setHeightInPoints(20);// 设置表头高度

				//第一列不设置值
				HSSFCell datacell = null;
				int flag = 0;
				for (int j = 0; j < columnNumber; j++) {
					datacell = row.createCell(j);
					switch (j) {
					case 0://id
						datacell.setCellValue(dataList.get(i).getId());
						break;
					case 1://课程名
						datacell.setCellValue(dataList.get(i).getCourseName());
						break;
					case 2://学员姓名
						datacell.setCellValue(dataList.get(i).getStudentName());
						break;
					case 3://卡号
						datacell.setCellValue(dataList.get(i).getCardNumber());
						break;
					case 4://签到区间
						datacell.setCellValue(dataList.get(i).getcType());
						break;
					case 5://打卡时间
						datacell.setCellValue(DateUtil.fromDateToStr(dataList.get(i).getClickTime()));
						break;
					default:
						break;
					}
				}
			}

			// 第六步，将文件存到浏览器设置的下载位置
			String filename = fileName + ".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					.concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			OutputStream out = response.getOutputStream();
			try {
				wb.write(out);// 将数据写出去
				String str = "导出" + fileName + "成功！";
				System.out.println(str);
			} catch (Exception e) {
				e.printStackTrace();
				String str1 = "导出" + fileName + "失败！";
				System.out.println(str1);
			} finally {
				out.close();
			}

		} else {
			System.out.println("列数目长度名称三个数组长度要一致");
		}

	}

	


}
