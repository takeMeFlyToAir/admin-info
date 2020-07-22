package com.ssd.admin.util.excel;

import com.ssd.admin.util.ErrorMessage;
import com.ssd.admin.util.ErrorType;
import com.ssd.admin.util.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ExcelDataUtil {
	/** excel导入配置文件 */
	private static final PropertiesLoader
	importExcelProperties = new PropertiesLoader(ExcelImportConstant.EXCEL_PROPERTIES_FILE);
	

	public static class ErrorLine {
		public int row;
		public List<ErrorMessage> error;
		@Override
		public String toString() {
			return row+","+error;
		}
	}
	
	public static class ExcelData {
		public List<ErrorLine> errorList;
		private List<?> datas;

		@SuppressWarnings("unchecked")
		public <T> List<T> getDatas(){
			return (List<T>)this.datas;
		}

		public List<ErrorLine> getErrorList(){
			return errorList;
		}

		@Override
		public String toString() {
			return datas+","+errorList;
		}
	}

	
	/**
	 * 读取Excel文件,按照03和07版本进行解析
	 * 
	 * @param excelFile
	 *            MultipartFile类型excel文件对象
	 * @param propPrefixName
	 *            配置文件前缀名称,例如:ExportGoods
	 * @return Map
	 */

	public static ExcelData readExcel(MultipartFile excelFile, String propPrefixName) {
		List<ErrorMessage> listError = new ArrayList<ErrorMessage>();
//		Map<Object, Object> datasMap = new HashMap<Object,Object>();
		List<Object> listDatas = new ArrayList<Object>();
		List<ErrorLine> floorList = new ArrayList<ErrorLine>();
		
		ExcelData excelData = new ExcelData();
		
		try {

			// 常规错误:excel文件不存在
			if (excelFile.isEmpty()) {
				listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_FILE_NOT_FOUND));
				exceptionDeal( floorList , listError, listDatas, excelData);
				log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_FILE_NOT_FOUND);
				return excelData;
			}

			String fileName = excelFile.getOriginalFilename();
			String extendName = fileName.substring(fileName.lastIndexOf('.'));

			// 处理excel2003文件
			if (ExcelImportConstant.VERSION_2003_EXCEL_XLS.equalsIgnoreCase(extendName)) {
				HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(excelFile.getInputStream());
				isExcelFileCorrect(hSSFWorkbook, propPrefixName, listError);
				if (listError.size() > 0) {
					if(listError.size()>1){
						for(ErrorMessage e:listError){
							List<ErrorMessage> list = new ArrayList<ErrorMessage>();
							list.add(e);
//							Map<Object, Object> h=new HashMap<Object,Object>();
//							h.put("row", 0);
//							h.put("error", list);
							ErrorLine errorLine = new ErrorLine();
							errorLine.error = listError;
							errorLine.row = 0;
							floorList.add(errorLine);
						}
//						datasMap.put("datas", listDatas);
//						datasMap.put("errorList", floorList);
						excelData.datas = listDatas;
						excelData.errorList = floorList;
					}else{
						exceptionDeal( floorList , listError, listDatas, excelData);
					}
					return excelData;
				} else {
					read2003SheetData(hSSFWorkbook, propPrefixName,listDatas,excelData);
				}
			} else if (ExcelImportConstant.VERSION_2007_EXCEL_XLSX.equalsIgnoreCase(extendName)) {// 处理excel2007文件
				XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(excelFile.getInputStream());
				isExcelFileCorrect(xSSFWorkbook, propPrefixName, listError);
				if (listError.size() > 0) {
					if(listError.size()>1){
						for(ErrorMessage e:listError){
							List<ErrorMessage> list = new ArrayList<ErrorMessage>();
							list.add(e);
							ErrorLine errorLine = new ErrorLine();
							errorLine.error = listError;
							errorLine.row = 0;
							floorList.add(errorLine);
						}
						excelData.datas = listDatas;
						excelData.errorList = floorList;
					}else{
						exceptionDeal( floorList , listError, listDatas, excelData);
					}
					return excelData;
				} else {
					read2007SheetData(xSSFWorkbook, propPrefixName,listDatas,excelData);
				}		
					

			} else {
				// 常规错误:excel文件不正确
				listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_NOT_CORRECT_EXCEL_FILE));
				exceptionDeal( floorList , listError, listDatas, excelData);
				log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_NOT_CORRECT_EXCEL_FILE);
				return excelData;
			}

		} catch (Exception e) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEPTION_STRONG));
			exceptionDeal( floorList , listError, listDatas, excelData);
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEPTION_STRONG+e.getMessage());
		}
		return excelData;
	}

	
	
	/**
	 * 解读复杂的execl文件
	 * @param excelFile
	 * @param propPrefixName
	 * @return
	 * 返回值为map对象
	 */
	public static ExcelData readComplexExcel(MultipartFile excelFile, String propPrefixName) {
		List<ErrorMessage> listError = new ArrayList<ErrorMessage>();
		List<Object> listDatas = new ArrayList<Object>();
		List<ErrorLine> floorList = new ArrayList<ErrorLine>();
		
		ExcelData excelData = new ExcelData();
		try {
			// 常规错误:excel文件不存在
			if (excelFile.isEmpty()) {
				listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_FILE_NOT_FOUND));
				exceptionDeal( floorList , listError, listDatas, excelData);
				log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_FILE_NOT_FOUND);
				return excelData;
			}

			String fileName = excelFile.getOriginalFilename();
			String extendName = fileName.substring(fileName.lastIndexOf('.'));

			// 处理excel2003文件
			if (ExcelImportConstant.VERSION_2003_EXCEL_XLS.equalsIgnoreCase(extendName)) {
				HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(excelFile.getInputStream());
				isComplexExcelFileCorrect(hSSFWorkbook, propPrefixName, listError);
				if (listError.size() > 0) {
					if(listError.size()>1){
						for(ErrorMessage e:listError){
							List<ErrorMessage> list = new ArrayList<ErrorMessage>();
							list.add(e);
							ErrorLine errorLine = new ErrorLine();
							errorLine.error = listError;
							errorLine.row = 0;
							floorList.add(errorLine);
						}
						excelData.datas = listDatas;
						excelData.errorList = floorList;
					}else{
						exceptionDeal( floorList , listError, listDatas, excelData);
					}
					return excelData;
				} else {
					read2003ComplexSheetData(hSSFWorkbook, propPrefixName,listDatas,excelData);
				}
			} else if (ExcelImportConstant.VERSION_2007_EXCEL_XLSX.equalsIgnoreCase(extendName)) {// 处理excel2007文件
				XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(excelFile.getInputStream());
				isComplexExcelFileCorrect(xSSFWorkbook, propPrefixName, listError);
				if (listError.size() > 0) {
					if(listError.size()>1){
						for(ErrorMessage e:listError){
							List<ErrorMessage> list = new ArrayList<ErrorMessage>();
							list.add(e);
							ErrorLine errorLine = new ErrorLine();
							errorLine.error = listError;
							errorLine.row = 0;
							floorList.add(errorLine);
						}
						excelData.datas = listDatas;
						excelData.errorList = floorList;
					}else{
						exceptionDeal( floorList , listError, listDatas, excelData);
					}
					return excelData;
				} else {
					read2007ComplexSheetData(xSSFWorkbook, propPrefixName,listDatas,excelData);
				}		
					

			} else {
				// 常规错误:excel文件不正确
				listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_NOT_CORRECT_EXCEL_FILE));
				exceptionDeal( floorList , listError, listDatas, excelData);
				log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_NOT_CORRECT_EXCEL_FILE);
				return excelData;
			}

		} catch (Exception e) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEPTION_STRONG));
			exceptionDeal( floorList , listError, listDatas, excelData);
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEPTION_STRONG+e.getMessage());
		}
		return excelData;
	}
	
	/**
	 * 验证导入的表头是否与模板的表头对应
	 * 
	 * @param workbook
	 *            Excel workbook对象
	 * @param propPrefixName
	 *            配置文件前缀名称,例如:ExportGoods
	 * @return boolean true：合法 数据| false：非法数据
	 */
	public static void isExcelFileCorrect(Workbook workbook, String propPrefixName, List<ErrorMessage> listError) {
		int activeSheetIndex = workbook.getActiveSheetIndex();
		int numberOfSheets = workbook.getNumberOfSheets();
		// sheet为空
		if (activeSheetIndex < 0 || numberOfSheets < 0) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_SHEET_NULL));
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_SHEET_NULL);
		}

		Sheet excelSheet = workbook.getSheetAt(0);
		// row为空
		Row row = excelSheet.getRow(0);
		if (row == null) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_ROW_NULL));
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_ROW_NULL);
		}

		// 读取配置文件title为空
		String title = getPropertyValue(propPrefixName + ExcelImportConstant.PROPRTIES_TITLE_SUFFIX);
		if (title == null || title.length() == 0) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_READ_PROPRTIES_TITLE));
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_READ_PROPRTIES_TITLE);
		}

		int excelTitleArrayLength = row.getPhysicalNumberOfCells();
		String[] propertiesTitleArray = title.split(",");
//		// 导入Excel列数与导入模板列数不对应
//		if (excelTitleArrayLength != propertiesTitleArray.length) {
//			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL));
//			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL);
//		}
		if(excelTitleArrayLength!=propertiesTitleArray.length){
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL,ExcelImportConstant.ERROR_EXCEL_WRONG_MODEL));
		}else{
			for (int i = 0; i < propertiesTitleArray.length; i++) {
				if (!(row.getCell(i).toString().equals(propertiesTitleArray[i]))) {
					log.debug(ExcelImportConstant.ERROR_NORMAL + "--" + ExcelImportConstant.ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL + "'"+row.getCell(i).toString()+"'与模板列'"+propertiesTitleArray[i]+"'不一致");
					listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, "'"+row.getCell(i).toString()+"'与模板列'"+propertiesTitleArray[i]+"'不一致"));
				}
			}
			if(listError.size()>0){
				listError.clear();
				listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL,ExcelImportConstant.ERROR_EXCEL_WRONG_MODEL));
			}
		}
		// 导入Excel列数与导入模板列数不对应
		
	}
	
	/**
	 * 验证复杂execl文件的配置文件是否正确
	 * @param workbook
	 * @param propPrefixName
	 * @param listError
	 */
	public static void isComplexExcelFileCorrect(Workbook workbook, String propPrefixName, List<ErrorMessage> listError) {
		int activeSheetIndex = workbook.getActiveSheetIndex();
		int numberOfSheets = workbook.getNumberOfSheets();
		// sheet为空
		if (activeSheetIndex < 0 || numberOfSheets < 0) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_SHEET_NULL));
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_SHEET_NULL);
		}

		Sheet excelSheet = workbook.getSheetAt(0);
		// row为空
		// 读取配置文件title为空
		//String title = getPropertyValue(propPrefixName + ExcelImportConstant.PROPRTIES_TITLE_SUFFIX);
		Integer startRow = Integer.parseInt(getPropertyValue(propPrefixName + ".xlsxListStartRow").trim());
		Row row = excelSheet.getRow(startRow);
		if (row == null) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_ROW_NULL));
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_ROW_NULL);
		}

		// 读取配置文件title为空
		String title = getPropertyValue(propPrefixName + ExcelImportConstant.PROPRTIES_TITLE_SUFFIX);
		if (title == null || title.length() == 0) {
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_READ_PROPRTIES_TITLE));
			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_READ_PROPRTIES_TITLE);
		}

		int excelTitleArrayLength = row.getPhysicalNumberOfCells();
		String[] propertiesTitleArray = title.split(",");
//		// 导入Excel列数与导入模板列数不对应
//		if (excelTitleArrayLength != propertiesTitleArray.length) {
//			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, ExcelImportConstant.ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL));
//			log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL);
//		}
		if(excelTitleArrayLength!=propertiesTitleArray.length){
			listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL,ExcelImportConstant.ERROR_EXCEL_WRONG_MODEL));
		}else{
			for (int i = 0; i < propertiesTitleArray.length; i++) {
				if (!(row.getCell(i).toString().equals(propertiesTitleArray[i]))) {
					log.debug(ExcelImportConstant.ERROR_NORMAL + ":" + ExcelImportConstant.ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL);
					listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL, "'"+row.getCell(i).toString()+"'与模板列'"+propertiesTitleArray[i]+"'不一致"));
				}
			}
			if(listError.size()>0){
				listError.clear();
				listError.add(createExceptionErrorMessage(ExcelImportConstant.ERROR_NORMAL,ExcelImportConstant.ERROR_EXCEL_WRONG_MODEL));
			}
		}
		// 导入Excel列数与导入模板列数不对应
		
	}
	
	
	public static void exceptionDeal(
			List<ErrorLine> floorList ,
			List<ErrorMessage> listError,
			List<Object> listDatas,
			ExcelData excelData) {
//		Map<Object, Object> h=new HashMap<Object,Object>();
//		h.put("row", 0);
//		h.put("error", listError);
		ErrorLine errorLine = new ErrorLine();
		errorLine.error = listError;
		errorLine.row = 0;
		floorList.add(errorLine);
//		datasMap.put("datas", listDatas);
//		datasMap.put("errorList", floorList);
		excelData.datas = listDatas;
		excelData.errorList = floorList;
		
	}
//	/** 验证导入的表头是否与模板的表头对应 **/
//	public static void validateIfModelSheet(Workbook iWb, String modelName, List<ErrorMessage> listError) {
//		Sheet excelSheet = iWb.getSheetAt(0);
//		String model = getPropertyValue(modelName + ".xlsxTitle");
//		String[] modelItems = model.split(",");
//		String str = "";
//		int modelSheetColumn = excelSheet.getRow(0).getPhysicalNumberOfCells();
//		if (modelSheetColumn != modelItems.length) {
//			str = "导入Excel列数与导入模板列数不对应，请检查";
//			listError.add(createExceptionErrorMessage("", str));
//		}
//		for (int i = 0; i < modelSheetColumn; i++) {
//			if (!(excelSheet.getRow(0).getCell(i).toString().equals(modelItems[i]))) {
//				str = "导入Excel列与导入模板列不对应，请检查";
//				listError.add(createExceptionErrorMessage("", str));
//			}
//		}
//	}

	/**
	 * 2007版本的Excel解析
	 * 
	 * @param iWb
	 * @param modelName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void read2007SheetData(
			XSSFWorkbook iWb, 
			String modelName,
			List<Object> listDatas,
			ExcelData excelData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String model = getPropertyValue(modelName + ".xlsxTitle").trim();
		String notNullColumns = getPropertyValue(modelName + ".xlsxNotNullColumn").trim();
		String dateColumns = getPropertyValue(modelName + ".xlsxDateColumn").trim();
		String digitalCols = getPropertyValue(modelName + ".xlsxDigitalColumn").trim();
		String booleanColumns = getPropertyValue(modelName + ".xlsxBooleanColumn").trim();
		String stringCols = getPropertyValue(modelName + ".xlsxStringColumn").trim();
		String classNamePath = getPropertyValue(modelName + ".className").trim();
		String columnName = getPropertyValue(modelName + ".columnName").trim();


		String[] modelItems = model.split(",");
		String[] columnNames = columnName.split(",");
		String[] digitalValues = digitalCols.split(",");
		String[] notNullValues = notNullColumns.split(",");
		String[] dateValues = dateColumns.split(",");
		String[] booleanValues = booleanColumns.split(",");
		String[] stringValues = stringCols.split(",");

		int excelRowNum = get2007ValidateExcelRowNum(iWb.getSheetAt(0), modelItems.length); // excel总列数
		if (excelRowNum > 0) {

			Class<?> className = Class.forName(classNamePath);
			List<ErrorLine> floorList = new ArrayList<ErrorLine>();

			for (int i = 1; i < excelRowNum + 1; i++) {// 行循环
//				Map<Object,Object> map = new HashMap<Object,Object>();
				ErrorLine errorLine = new ErrorLine();
//				map.put("row", i + 1);
				errorLine.row = i + 1;
				Object bean = className.newInstance();
				XSSFRow columnRow = iWb.getSheetAt(0).getRow(i);
				List<ErrorMessage> listError = new ArrayList<ErrorMessage>();
				if (columnRow != null) {
					for (int j = 0; j < modelItems.length; j++) { // 列循环
						XSSFCell cell = columnRow.getCell(j);
						Object value = getXSSFCellValue(cell);

						if ((value==null || "".equals(value)) && ArrayUtils.contains(notNullValues,String.valueOf(j))) {
							listError.add(createErrorMessage(modelItems[j], ErrorType.NULL));
						} else if (ArrayUtils.contains(dateValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDate(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(digitalValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDouble(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(booleanValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertBoolean(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(stringValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertString(value));

						}
					}
				} else {
					break;
				}
				if (listError.size() > 0) {
//					map.put("error", listError);
					errorLine.error = listError;
					floorList.add(errorLine);
					listDatas.add(null);
				} else {
					listDatas.add(bean);
				}
			}
//			datasMap.put("errorList", floorList);
//			datasMap.put("datas", listDatas);
			excelData.errorList = floorList;
			excelData.datas = listDatas;
		}

	}

	/**
	 * 解析复杂的execl文件07版的
	 * @param iWb
	 * @param modelName
	 * @param listDatas
	 * @param excelData
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */

	public static void read2007ComplexSheetData(
			XSSFWorkbook iWb, 
			String modelName,
			List<Object> listDatas,
			ExcelData excelData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//列表开始的行数
		Integer startRow = Integer.parseInt(getPropertyValue(modelName + ".xlsxListStartRow").trim());
		Integer endRow = Integer.parseInt(getPropertyValue(modelName + ".xlsxListEndRow").trim());
		
		String model = getPropertyValue(modelName + ".xlsxTitle").trim();
		String notNullColumns = getPropertyValue(modelName + ".xlsxNotNullColumn").trim();
		String dateColumns = getPropertyValue(modelName + ".xlsxDateColumn").trim();
		String digitalCols = getPropertyValue(modelName + ".xlsxDigitalColumn").trim();
		String booleanColumns = getPropertyValue(modelName + ".xlsxBooleanColumn").trim();
		String stringCols = getPropertyValue(modelName + ".xlsxStringColumn").trim();
		String classNamePath = getPropertyValue(modelName + ".className").trim();
		String columnName = getPropertyValue(modelName + ".columnName").trim();
		
//		String cellPositionAndName = getPropertyValue(modelName + ".xlsxCellPositionAndName").trim().trim();

		String[] modelItems = model.split(",");
		String[] columnNames = columnName.split(",");
		String[] digitalValues = digitalCols.split(",");
		String[] notNullValues = notNullColumns.split(",");
		String[] dateValues = dateColumns.split(",");
		String[] booleanValues = booleanColumns.split(",");
		String[] stringValues = stringCols.split(",");
//		String[] cellPostionAndNames = cellPositionAndName.split(",");
		int excelRowNum = get2007ValidateExcelRowNum(iWb.getSheetAt(0), modelItems.length); // excel总列数
		if (excelRowNum > 0) {

			Class<?> className = Class.forName(classNamePath);
			List<ErrorLine> floorList = new ArrayList<ErrorLine>();

//			//循环遍历非列表的集合对象 TODO Excel导入 待处理
//			for(int i = 0;i<cellPostionAndNames.length;i++){
//				String tempCellPostion = cellPostionAndNames[i];
//				String[] cellPostion = tempCellPostion.split("&");
//				if(cellPostion.length == 3){
//					Integer rowNum = Integer.parseInt(cellPostion[0]);
//					Integer columNum = Integer.parseInt(cellPostion[1]);
//					String  columTitle = cellPostion[2];
//					if(rowNum>=0){
//						Object val = getXSSFCellValue(iWb.getSheetAt(0).getRow(rowNum).getCell(columNum));
//						datasMap.put(columTitle, val);
//					}else{
//						Integer res = excelRowNum + rowNum +1;
//						Object val = getXSSFCellValue(iWb.getSheetAt(0).getRow(res).getCell(columNum));
//						datasMap.put(columTitle, val);
//					}
//				}
//			}
			for (int i = startRow+1; i < (excelRowNum - endRow + 1); i++) {// 行循环
//				Map<Object,Object> map = new HashMap<Object,Object>();
				ErrorLine errorLine = new ErrorLine();
//				map.put("row", i + 1);
				errorLine.row = i + 1;
				Object bean = className.newInstance();
				XSSFRow columnRow = iWb.getSheetAt(0).getRow(i);
				List<ErrorMessage> listError = new ArrayList<ErrorMessage>();
				if (columnRow != null) {
					for (int j = 0; j < modelItems.length; j++) { // 列循环
						XSSFCell cell = columnRow.getCell(j);
						Object value = getXSSFCellValue(cell);

						if ((value==null || "".equals(value)) && ArrayUtils.contains(notNullValues,String.valueOf(j))) {
							listError.add(createErrorMessage(modelItems[j], ErrorType.NULL));
						} else if (ArrayUtils.contains(dateValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDate(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(digitalValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDouble(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(booleanValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertBoolean(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(stringValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertString(value));

						}
					}
				} else {
					break;
				}
				if (listError.size() > 0) {
//					map.put("error", listError);
					errorLine.error = listError;
					floorList.add(errorLine);
					listDatas.add(null);
				} else {
					listDatas.add(bean);
				}
			}
//			datasMap.put("errorList", floorList);
//			datasMap.put("datas", listDatas);
			excelData.errorList = floorList;
			excelData.datas = listDatas;
		}

	}
	
	/**
	 * 2003版本的Excel解析
	 * 
	 * @param iWb
	 *            获取的excel文件
	 * @param modelName
	 *            模板名称
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * 
	 * **/

	public static void read2003SheetData(
			HSSFWorkbook iWb, 
			String modelName,
			List<Object> listDatas,
			ExcelData excelData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// PropertiesLoader propertiesLoader = loadProperties();
		String model = getPropertyValue(modelName + ".xlsxTitle").trim();
		String[] modelItems = model.split(",");
		String notNullColumns = getPropertyValue(modelName + ".xlsxNotNullColumn").trim();
		String dateColumns = getPropertyValue(modelName + ".xlsxDateColumn").trim();
		String digitalCols = getPropertyValue(modelName + ".xlsxDigitalColumn").trim();
		String booleanColumns = getPropertyValue(modelName + ".xlsxBooleanColumn").trim();
		String stringCols = getPropertyValue(modelName + ".xlsxStringColumn").trim();
		String classNamePath = getPropertyValue(modelName + ".className").trim();
		String columnName = getPropertyValue(modelName + ".columnName").trim().trim();	
		
		String[] columnNames = columnName.split(",");
		String[] digitalValues = digitalCols.split(",");
		String[] notNullValues = notNullColumns.split(",");
		String[] dateValues = dateColumns.split(",");
		String[] booleanValues = booleanColumns.split(",");
		String[] stringValues = stringCols.split(",");
		int excelRowNum = get2003ValidateExcelRowNum(iWb.getSheetAt(0), modelItems.length);  // excel总列数
		if (excelRowNum > 0) {
			Class<?> className = Class.forName(classNamePath);
			List<ErrorLine> floorList = new ArrayList<ErrorLine>();
			for (int i = 1; i < (excelRowNum + 1); i++) {// 行循环
//				Map<Object,Object> map = new HashMap<Object,Object>();
				ErrorLine errorLine = new ErrorLine();
//				map.put("row", i + 1);
				errorLine.row = i + 1;
				Object bean = className.newInstance();
				HSSFRow columnRow = iWb.getSheetAt(0).getRow(i);
				List<ErrorMessage> listError = new ArrayList<ErrorMessage>();
				if (columnRow != null) {
					for (int j = 0; j < modelItems.length; j++) { // 列循环
						HSSFCell cell = columnRow.getCell(j);
						Object value = getHSSFCellValue(cell);

						if ((value==null || "".equals(value)) && ArrayUtils.contains(notNullValues,String.valueOf(j))) {
							listError.add(createErrorMessage(modelItems[j], ErrorType.NULL));
						} else if (ArrayUtils.contains(dateValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDate(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(digitalValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDouble(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(booleanValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertBoolean(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(stringValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertString(value));
						}
						
					}
				} else {
					break;
				}
				if (listError.size() > 0) {
//					map.put("error", listError);
					errorLine.error = listError;
					floorList.add(errorLine);
					listDatas.add(null);
				} else {
					listDatas.add(bean);
				}
			}
//			datasMap.put("errorList", floorList);
//			datasMap.put("datas", listDatas);
			excelData.errorList = floorList;
			excelData.datas = listDatas;
		}

	}

	
	
	/**
	 * 2003版本的Excel解析
	 * 解读复杂的2003版的execl文件
	 * @param iWb
	 *            获取的excel文件
	 * @param modelName
	 *            模板名称
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * 
	 * **/

	public static void read2003ComplexSheetData(
			HSSFWorkbook iWb, 
			String modelName,
			List<Object> listDatas,
			ExcelData excelData) 
					throws ClassNotFoundException, 
					InstantiationException, 
					IllegalAccessException, 
					InvocationTargetException, 
					NoSuchMethodException {
		// PropertiesLoader propertiesLoader = loadProperties();
		//列表开始的行数
		Integer startRow = Integer.parseInt(getPropertyValue(modelName + ".xlsxListStartRow").trim());
		Integer endRow = Integer.parseInt(getPropertyValue(modelName + ".xlsxListEndRow").trim());
		//模板头文件
		String model = getPropertyValue(modelName + ".xlsxTitle").trim();
		//非空列
		String notNullColumns = getPropertyValue(modelName + ".xlsxNotNullColumn").trim();
		//日期列
		String dateColumns = getPropertyValue(modelName + ".xlsxDateColumn").trim();
		//数据列
		String digitalCols = getPropertyValue(modelName + ".xlsxDigitalColumn").trim();
		//布尔列
		String booleanColumns = getPropertyValue(modelName + ".xlsxBooleanColumn").trim();
		//字符类
		String stringCols = getPropertyValue(modelName + ".xlsxStringColumn").trim();
		//类路径
		String classNamePath = getPropertyValue(modelName + ".className").trim();
        //列表集合
		String columnName = getPropertyValue(modelName + ".columnName").trim().trim();
//		String cellPositionAndName = getPropertyValue(modelName + ".xlsxCellPositionAndName").trim().trim();
		//配置转换成数据
		String[] modelItems = model.split(",");
		String[] notNullValues = notNullColumns.split(",");
		String[] dateValues = dateColumns.split(",");
		String[] digitalValues = digitalCols.split(",");
		String[] booleanValues = booleanColumns.split(",");
		String[] stringValues = stringCols.split(",");
		String[] columnNames = columnName.split(",");
//        String[] cellPostionAndNames = cellPositionAndName.split(",");
        
		int excelRowNum = get2003ValidateExcelRowNum(iWb.getSheetAt(0), modelItems.length);  // excel总列数
		if (excelRowNum > 0) {
			Class<?> className = Class.forName(classNamePath);
			List<ErrorLine> floorList = new ArrayList<ErrorLine>();
//			//循环遍历非列表的集合对象 TODO Excel 导入 待处理
//			for(int i = 0;i<cellPostionAndNames.length;i++){
//				String tempCellPostion = cellPostionAndNames[i];
//				String[] cellPostion = tempCellPostion.split("&");
//				if(cellPostion.length == 3){
//					Integer rowNum = Integer.parseInt(cellPostion[0]);
//					Integer columNum = Integer.parseInt(cellPostion[1]);
//					String  columTitle = cellPostion[2];
//					if(rowNum>=0){
//						Object val = getHSSFCellValue(iWb.getSheetAt(0).getRow(rowNum).getCell(columNum));
//						datasMap.put(columTitle, val);
//					}else{
//						Integer res = excelRowNum + rowNum +1;
//						Object val = getHSSFCellValue(iWb.getSheetAt(0).getRow(res).getCell(columNum));
//						datasMap.put(columTitle, val);
//					}
//				}
//			}
			for (int i = startRow+1; i < (excelRowNum - endRow + 1); i++) {// 行循环
//				Map<Object,Object> map = new HashMap<Object,Object>();
				ErrorLine errorLine = new ErrorLine();
//				map.put("row", i + 1);
				errorLine.row = i + 1;
				Object bean = className.newInstance();
				//从第一行开始读execl
				HSSFRow columnRow = iWb.getSheetAt(0).getRow(i);
				List<ErrorMessage> listError = new ArrayList<ErrorMessage>();
				if (columnRow != null) {
					for (int j = 0; j < modelItems.length; j++) { // 列循环
						//获取单元格的值
						HSSFCell cell = columnRow.getCell(j);
						Object value = getHSSFCellValue(cell);
						if ((value==null || "".equals(value)) && ArrayUtils.contains(notNullValues,String.valueOf(j))) {
							listError.add(createErrorMessage(modelItems[j], ErrorType.NULL));
						} else if (ArrayUtils.contains(dateValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDate(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(digitalValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertDouble(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(booleanValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertBoolean(value, modelItems[j], listError));
						} else if (ArrayUtils.contains(stringValues, String.valueOf(j))) {
							PropertyUtils.setProperty(bean, columnNames[j], convertString(value));
						}
					}
				} else {
					break;
				}
				if (listError.size() > 0) {
//					map.put("error", listError);
					errorLine.error = listError;
					floorList.add(errorLine);
					listDatas.add(null);
				} else {
					listDatas.add(bean);
				}
			}
			
//			datasMap.put("errorList", floorList);
//			datasMap.put("datas", listDatas);
			excelData.errorList = floorList;
			excelData.datas = listDatas;
		}

	}
	
	private static Object getHSSFCellValue(HSSFCell cell) {
		Object result = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			// XSSFCell可以达到相同的效果
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期类型
					result = cell.getDateCellValue();
				} else {
					 cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					result = cell.getStringCellValue();
				}
				break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				result = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				try {
					result = cell.getNumericCellValue();
				} catch (IllegalStateException e) {
					try {
						result = cell.getStringCellValue();
					} catch (IllegalStateException e1) {

					}
				}
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				result = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 非法字符

				break;
			default:// 未知类型

				break;
			}
		}
		return result;
	}

	private static Object getXSSFCellValue(XSSFCell cell) {
		Object result = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			// XSSFCell可以达到相同的效果
			case XSSFCell.CELL_TYPE_NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期类型
					result = cell.getDateCellValue();
				} else {
					 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					result = cell.getStringCellValue();
				}
				break;
			case XSSFCell.CELL_TYPE_STRING: // 字符串
				result = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				result = cell.getBooleanCellValue();
				break;
			case XSSFCell.CELL_TYPE_FORMULA: // 公式
				try {
					result = cell.getNumericCellValue();
				} catch (IllegalStateException e) {
					try {
						result = cell.getStringCellValue();
					} catch (IllegalStateException e1) {

					}
				}
				break;
			case XSSFCell.CELL_TYPE_BLANK: // 空值
				result = "";
				break;
			case XSSFCell.CELL_TYPE_ERROR: // 非法字符

				break;
			default:// 未知类型

				break;
			}
		}
		return result;
	}

	private static String convertString(Object value) {
		return value == null ? null : value.toString();
	}

	private static Double convertDouble(Object value, String title, List<ErrorMessage> errors) {
		if(value==null || "".equals(value)) return null;
		if (value instanceof Double)
			return (Double) value;
		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (Exception e) {

			}
		}
		errors.add(createErrorMessage(title, ErrorType.DIGITAL));
		return null;
	}

	private static Date convertDate(Object value, String title, List<ErrorMessage> errors) {
		if(value==null || "".equals(value)) return null;
		if (value instanceof Date)
			return (Date) value;
		if (value instanceof String) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
			} catch (ParseException e) {
				try {
					return new SimpleDateFormat("yyyy/MM/dd").parse(value.toString());
				} catch (ParseException e1) {

				}
			}
		}
		errors.add(createErrorMessage(title, ErrorType.DATE));
		return null;
	}

	private static Boolean convertBoolean(Object value, String title, List<ErrorMessage> errors) {
		if(value==null || "".equals(value)) return null;
		if (value instanceof Boolean)
			return (Boolean) value;
		if (value instanceof String) {
			return "Y".equalsIgnoreCase((String) value) || "TRUE".equalsIgnoreCase((String) value) || "是".equalsIgnoreCase((String) value) ? Boolean.TRUE : Boolean.FALSE;
		}
		errors.add(createErrorMessage(title, ErrorType.BOOLEAN));
		return null;
	}

	private static ErrorMessage createErrorMessage(String title, ErrorType errorType) {
		ErrorMessage en = new ErrorMessage();
		en.setTitle(title);
		en.setErrorMessage(errorType.getDisplay());
		return en;
	}

	private static ErrorMessage createExceptionErrorMessage(String title, String errorMessage) {
		ErrorMessage en = new ErrorMessage();
		en.setTitle(title);
		en.setErrorMessage(errorMessage);
		return en;
	}

	/**
	 * 根据配置文件中key获取value
	 * 
	 * @param keyName
	 *            key名称
	 * @return value
	 */
	private static String getPropertyValue(String keyName) {
		String keyValue = importExcelProperties.getProperty(keyName);
		if (keyValue == null) {
			log.debug("根据key获取配置文件的value为空,返回null；key==" + keyName);
			return null;
		} else {
			try {
				return new String(keyValue.getBytes("ISO8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.debug("根据key(" + keyName + ")获取配置文件的value转码UTF8抛异常,返回''；keyValue==" + keyValue);
				return "";
			}
		}
	}

	/**
	 * 获取2007版本真实有数据的行数
	 * **/

	public static int get2007ValidateExcelRowNum(XSSFSheet sheet,int rows) {
		int columns=sheet.getLastRowNum();
		int realColumn=0;
		for(int i=columns;i>=0;i--){
			if(realColumn>0){
				break;
			}
			XSSFRow excelheadRow = sheet.getRow(i);
			if(excelheadRow!=null){
				for(int j=0;j<2;j++){
					XSSFCell cell=excelheadRow.getCell(j);
					if(cell!=null){
						if(cell.getCellType()==XSSFCell.CELL_TYPE_BLANK){
							continue;
						}else{
							realColumn=i;
						}
						
					}else{
						continue;
					}
					
				}
			}else{
				continue;
			}
			
		}
		return realColumn;
	}
	/**
	 * 获取2003版本真实有数据的行数
	 * **/
	public static int get2003ValidateExcelRowNum(HSSFSheet sheet,int rows) {
		int columns=sheet.getLastRowNum();
		int realColumn=0;
		for(int i=columns;i>=0;i--){
			if(realColumn>0){
				break;
			}
			HSSFRow excelheadRow = sheet.getRow(i);
			if(excelheadRow!=null){
				for(int j=0;j<2;j++){
					HSSFCell cell=excelheadRow.getCell(j);
					if(cell!=null){
						if(cell.getCellType()==XSSFCell.CELL_TYPE_BLANK){
							continue;
						}else{
							realColumn=i;
						}
						
					}else{
						continue;
					}
					
				}
			}else{
				continue;
			}
			
		}
		return realColumn;
	}
}
