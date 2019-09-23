/**
 * 
 */
package com.ssd.admin.util.excel;

/**
 * Excel导入信息常量类
 *
 * 2015年9月7日下午1:21:27
 * @author 吕佳诚
 */
public class ExcelImportConstant {
	
	/**EXCE配置文件*/
	public static final String EXCEL_PROPERTIES_FILE = "/props/importExcel.properties";
	
	/** excel2003版,标识:xls*/
	public static final String VERSION_2003_EXCEL_XLS = ".xls"; 
	/** excel2007版,标识:xlsx*/
	public static final String VERSION_2007_EXCEL_XLSX = ".xlsx"; 
	

	/***常规错误,例如：文件不存在;模板不正确;不是excel文件等*/
	public static final String ERROR_NORMAL = "normalError";
	/**导入数据出现异常,请检查后重试**/
	public static final String ERROR_EXCEPTION_STRONG = "导入数据出现异常,请检查后重试";
	
	/***excel文件不存在*/
	public static final String ERROR_EXCEL_FILE_NOT_FOUND = "excel文件不存在";
	
	/***必须以.xls或.xlsx结尾的excel文件*/
	public static final String ERROR_NOT_CORRECT_EXCEL_FILE = "必须以.xls或.xlsx结尾的excel文件";
	
	/**EXCEL模板配置文件列标题后缀名：.xlsxTitle*/
	public static final String PROPRTIES_TITLE_SUFFIX = ".xlsxTitle";
	
	/**excel sheet为空*/
	public static final String ERROR_EXCEL_SHEET_NULL = "错误的excel文件,没有sheet" ;
	/**错误的excel文件,空行*/
	public static final String ERROR_EXCEL_ROW_NULL = "错误的excel文件,空行" ;
	/**导入Excel列数与导入模板列数不对应*/
	public static final String ERROR_EXCEL_CELL_NOT_MATCH_PROPERTIES_CELL = "导入Excel列数与导入模板列数不对应" ;
	/**导入Excel列与导入模版某一列不对应*/
	public static final String ERROR_EXCEL_WRONG_MODEL = "导入数据模板错误，请更换模板" ;
	
	/**读取EXCEL模板配置文件中模板标题出错*/
	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
	
	
	
	/**EXCEL模板配置文件列标题后缀名：.xlsxNotNullColumn*/
	public static final String PROPRTIES_DATA_COLUMN_SUFFIX = ".xlsxNotNullColumn";
	/**读取EXCEL模板配置文件中模板非空列配置出错*/
	public static final String ERROR_READ_PROPRTIES_DATA_COLUMN = "读取配置文件中模板标题出错";
	/**读取EXCEL模板配置文件中模板标题出错*/
//	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
//	/**读取EXCEL模板配置文件中模板标题出错*/
//	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
//	/**读取EXCEL模板配置文件中模板标题出错*/
//	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
//	/**读取EXCEL模板配置文件中模板标题出错*/
//	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
//	/**读取EXCEL模板配置文件中模板标题出错*/
//	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
//	/**读取EXCEL模板配置文件中模板标题出错*/
//	public static final String ERROR_READ_PROPRTIES_TITLE = "读取配置文件中模板标题出错";
	
	
	
//	String model = getPropertyValue(modelName + ".xlsxTitle");
//	String[] modelItems = model.split(",");
//	String notNullColumns = getPropertyValue(modelName + ".xlsxNotNullColumn");
//	String dateColumns = getPropertyValue(modelName + ".xlsxDateColumn");
//	String digitalCols = getPropertyValue(modelName + ".xlsxDigitalColumn");
//	String booleanColumns = getPropertyValue(modelName + ".xlsxBooleanColumn");
//	String stringCols = getPropertyValue(modelName + ".xlsxStringColumn");
//	String formulaCols = getPropertyValue(modelName + ".xlsxStringColumn");
//	
//	String classNamePath = getPropertyValue(modelName + ".className");
//	
//	String columnName = getPropertyValue(modelName + ".columnName");
}
