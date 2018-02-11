package com.pactera.controller.poi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PoiController {

	private static final Log log = LogFactory.getLog("ClientServerController");

	@ResponseBody
	@RequestMapping("/poi")
	public void preService(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {

		log.info("poi start...");

	}

	@RequestMapping("/export")
	@ResponseBody
	public void export(HttpServletRequest request,
			HttpServletResponse response, String yearMonth) {
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("yearMonth", yearMonth + "%");

			Map<Integer, String> map = new HashMap<Integer, String>();
			map.put(1, "晴");
			map.put(2, "多云");
			map.put(3, "阴");
			map.put(4, "小雨");
			map.put(5, "中雨");
			map.put(6, "大雨");
			map.put(7, "大到暴雨");
			map.put(8, "雾");
			map.put(9, "霾");
			// 构造导出数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<Integer> listStr = new ArrayList<>();
			listStr.add(1);
			listStr.add(2);
			listStr.add(3);
			listStr.add(4);
			listStr.add(5);
			listStr.add(6);
			listStr.add(7);
			listStr.add(8);
			listStr.add(9);

			for (int i = 0; i < listStr.size(); i++) {
				int model = listStr.get(i);
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("date", "2018-01-24");
				if (model == 1) {
					tmpMap.put("weather", map.get(1));
				} else if (model == 2) {
					tmpMap.put("weather", map.get(2));
				} else if (model == 3) {
					tmpMap.put("weather", map.get(3));
				} else if (model == 4) {
					tmpMap.put("weather", map.get(4));
				} else if (model == 5) {
					tmpMap.put("weather", map.get(5));
				} else if (model == 6) {
					tmpMap.put("weather", map.get(6));
				} else if (model == 7) {
					tmpMap.put("weather", map.get(7));
				} else if (model == 8) {
					tmpMap.put("weather", map.get(8));
				} else if (model == 9) {
					tmpMap.put("weather", map.get(9));
				}
				tmpMap.put("natureTem", "12");
				tmpMap.put("natureHum", "22");
				tmpMap.put("adjustTem", "33");
				tmpMap.put("adjustHum", "44");
				tmpMap.put("remark", "55");
				tmpMap.put("creator", "66");
				dataList.add(tmpMap);
			}
			String sheetName = "温湿度日记录";
			String date = yearMonth;
			String[] head0 = new String[] { "日期", "天气", "自然", "自然", "调整", "调整",
					"备注", "记录人" };// 在excel中的第3行每列的参数
			String[] head1 = new String[] { "温度℃", "湿度%", "温度℃", "湿度%" };// 在excel中的第4行每列（合并列）的参数
			String[] headnum0 = new String[] { "2,3,0,0", "2,3,1,1", "2,2,2,3",
					"2,2,4,5", "2,3,6,6", "2,3,7,7"};// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
//			String[] headnum0 = new String[] { "2,3,0,0", "2,3,1,1", "2,2,2,3",
//					"2,2,4,5", "2,3,6,6", "2,3,7,7", "2,3,8,8", "2,3,9,9" };// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] headnum1 = new String[] { "3,3,2,2", "3,3,3,3", "3,3,4,4","4,4,4,4","4,4,5,5",
					"3,3,5,5" };
			String[] colName = new String[] { "date", "weather", "natureTem",
					"natureHum", "adjustTem", "adjustHum", "remark", "creator" };// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key
			reportMergeXls(request, response, dataList, sheetName, head0,
					headnum0, head1, headnum1, colName, date); // utils类需要用到的参数
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 多行表头 dataList：导出的数据；sheetName：表头名称； head0：表头第一行列名；headnum0：第一行合并单元格的参数
	 * head1：表头第二行列名；headnum1：第二行合并单元格的参数；detail：导出的表体字段
	 *
	 */
	public void reportMergeXls(HttpServletRequest request,
			HttpServletResponse response, List<Map<String, Object>> dataList,
			String sheetName, String[] head0, String[] headnum0,
			String[] head1, String[] headnum1, String[] detail, String date)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);// 创建一个表
		// 表头标题样式
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		// 表头时间样式
		HSSFFont datefont = workbook.createFont();
		datefont.setFontName("宋体");
		datefont.setFontHeightInPoints((short) 12);// 字体大小
		HSSFCellStyle datestyle = workbook.createCellStyle();
		datestyle.setFont(datefont);
		datestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		datestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		datestyle.setLocked(true);
		// 列名样式
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);// 字体大小
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		style.setLocked(true);
		// 普通单元格样式（中文）
		HSSFFont font2 = workbook.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 12);
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style2.setFont(font2);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style2.setWrapText(true); // 换行
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		// 设置列宽 （第几列，宽度）
		sheet.setColumnWidth(0, 1600);
		sheet.setColumnWidth(1, 3600);
		sheet.setColumnWidth(2, 2800);
		sheet.setColumnWidth(3, 2800);
		sheet.setColumnWidth(4, 2800);
		sheet.setColumnWidth(5, 2800);
		sheet.setColumnWidth(6, 4500);
		sheet.setColumnWidth(7, 3600);
		sheet.setDefaultRowHeight((short) 360);// 设置行高
		// 第一行表头标题
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head0.length - 1));
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 0x349);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headstyle);
		CellUtil.setCellValue(cell, sheetName);
		// 第二行时间
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, head0.length - 1));
		HSSFRow row1 = sheet.createRow(1);
		row.setHeight((short) 0x349);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(datestyle);
		CellUtil.setCellValue(cell1, date);
		// 第三行表头列名
		row = sheet.createRow(2);
		for (int i = 0; i < 8; i++) {
			cell = row.createCell(i);
			cell.setCellValue(head0[i]);
			cell.setCellStyle(style);
		}
		// 动态合并单元格
		for (int i = 0; i < headnum0.length; i++) {
			String[] temp = headnum0[i].split(",");
			Integer startrow = Integer.parseInt(temp[0]);
			Integer overrow = Integer.parseInt(temp[1]);
			Integer startcol = Integer.parseInt(temp[2]);
			Integer overcol = Integer.parseInt(temp[3]);
			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
					startcol, overcol));
		}
		// 设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		row = sheet.createRow(3);// 因为下标从0开始，所以这里表示的是excel中的第四行
		for (int i = 0; i < head0.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);// 设置excel中第四行的1、2、7、8列的边框
			if (i > 1 && i < 6) {
				for (int j = 0; j < head1.length; j++) {
					cell = row.createCell(j + 2);
					cell.setCellValue(head1[j]);// 给excel中第四行的3、4、5、6列赋值（"温度℃",
												// "湿度%", "温度℃", "湿度%"）
					cell.setCellStyle(style);// 设置excel中第四行的3、4、5、6列的边框
				}
			}
		}
		// 动态合并单元格
		for (int i = 0; i < headnum1.length; i++) {
			String[] temp = headnum1[i].split(",");
			Integer startrow = Integer.parseInt(temp[0]);
			Integer overrow = Integer.parseInt(temp[1]);
			Integer startcol = Integer.parseInt(temp[2]);
			Integer overcol = Integer.parseInt(temp[3]);
			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
					startcol, overcol));
		}

		// 设置列值-内容
		for (int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 4);// 标题、时间、表头字段共占了4行，所以在填充数据的时候要加4，也就是数据要从第5行开始填充
			for (int j = 0; j < detail.length; j++) {
				Map tempmap = (HashMap) dataList.get(i);
				Object data = tempmap.get(detail[j]);
				cell = row.createCell(j);
				cell.setCellStyle(style2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				CellUtil.setCellValue(cell, data);
			}
		}
		String fileName = new String(sheetName.getBytes("gb2312"), "ISO8859-1");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		workbook.write(baos);
		response.setContentType("application/x-download;charset=utf-8");
		response.addHeader("Content-Disposition", "attachment;filename="
				+ fileName + ".xls");
		OutputStream os = response.getOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		byte[] b = new byte[1024];
		while ((bais.read(b)) > 0) {
			os.write(b);
		}
		bais.close();
		os.flush();
		os.close();
	}

}
