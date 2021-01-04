package com.huali.utils;

import com.huali.ExcelExportable;
import com.huali.utils.annotation.ExcelParams;
import com.huali.utils.enums.ExportVersion;
import com.huali.utils.model.ExcelStyle;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 导出excel 的工具类，
 * <p>
 * 使用该类需要一个对应的实体对象
 */
@Slf4j
public class ExcelExportUtil extends ExcelExportBase {
    private ExcelExportUtil() {
    }

    /**
     * 将查询结果封装成 excel表 没有标题,没有任何样式
     *
     * @param data sql查询的结果
     * @return 封装好的excel
     */
    public static XSSFWorkbook toExcelFile(List<? extends ExcelExportable> data, ExportVersion version) {
        return toExcelFile(data, null, version);
    }

    /**
     * 将查询结果封装成 excel表 有标题,没有任何样式
     *
     * @param data  sql查询的结果
     * @param title 标题内容
     * @return 封装好的excel
     */
    public static XSSFWorkbook toExcelFile(List<? extends ExcelExportable> data, String title, ExportVersion version) {
        return toExcelFile(data, title, new ExcelStyle(), version);
    }

    /**
     * 将查询结果封装成 excel表 有标题, 样式自己通过样式对象进行控制
     * (data 仅仅支持DTO 平铺的形式导出)
     *
     * @param data       sql查询的结果
     * @param title      标题内容
     * @param excelStyle excel样式对象
     * @return 封装好的excel
     */
    @SneakyThrows
    public static XSSFWorkbook toExcelFile(List<? extends ExcelExportable> data, String title, ExcelStyle excelStyle, ExportVersion version) {
        return toExcelFile(data, title, version,
                excelStyle.getBackgroundType(), excelStyle.getBorderType(), excelStyle.getBorderColor(),
                excelStyle.getTitleFontName(), excelStyle.getHeadFontName(), excelStyle.getDataFontName(),
                excelStyle.getTitleBold(), excelStyle.getHeadBold(), excelStyle.getDataBold(),
                excelStyle.getTitleSize(), excelStyle.getHeadSize(), excelStyle.getDataSize(),
                excelStyle.getTitleLevelPosition(), excelStyle.getHeadLevelPosition(), excelStyle.getDataLevelPosition(),
                excelStyle.getTitleVerticalPosition(), excelStyle.getTitleVerticalPosition(), excelStyle.getDataVerticalPosition(),
                excelStyle.getTitleBackgroundColor(), excelStyle.getHeadBackgroundColor(), excelStyle.getDataBackgroundColor());
    }


    /**
     * 将查询出来的结果封装进 excel 中 有标题的
     *
     * @param data                  sql查询的结果
     * @param title                 标题数据 可以选择不填写
     * @param version               导出的版本
     * @param backgroundType        表格背景颜色的类型(前景色)
     * @param borderType            表格边框的类型
     * @param borderColor           表格变宽的颜色
     * @param titleFontName         标题字体类型名称
     * @param headFontName          表头字体类型名称
     * @param dataFontName          数据字体类型名称
     * @param titleBold             标题字体是否粗体显示
     * @param headBold              表头字体是否粗体显示
     * @param dataBold              数据字体是否粗体显示
     * @param titleSize             标题字体大小
     * @param headSize              表头字体大小
     * @param dataSize              数据字体大小
     * @param titleLevelPosition    标题字体水平方向
     * @param headLevelPosition     表头字体水平方向
     * @param dataLevelPosition     数据字体水平方向
     * @param titleVerticalPosition 标题字体垂直方向
     * @param headVerticalPosition  表头字体垂直方向
     * @param dataVerticalPosition  数据字体垂直方向
     * @param titleBackgroundColor  标题的背景颜色
     * @param headBackgroundColor   表头的背景颜色
     * @param dataBackgroundColor   数据的背景颜色
     * @return 封装好的excel
     * @throws IllegalAccessException .
     */
    private static XSSFWorkbook toExcelFile(List<? extends ExcelExportable> data, String title, ExportVersion version,
                                            //同一的 背景颜色的类型  边框的类型 边框的颜色
                                            FillPatternType backgroundType, BorderStyle borderType, IndexedColors borderColor,
                                            //字体名称
                                            String titleFontName, String headFontName, String dataFontName,
                                            //字体是否为粗体
                                            Boolean titleBold, Boolean headBold, Boolean dataBold,
                                            //字体大小
                                            int titleSize, int headSize, int dataSize,
                                            //水平方向
                                            HorizontalAlignment titleLevelPosition, HorizontalAlignment headLevelPosition, HorizontalAlignment dataLevelPosition,
                                            //垂直方向
                                            VerticalAlignment titleVerticalPosition, VerticalAlignment headVerticalPosition, VerticalAlignment dataVerticalPosition,
                                            //背景颜色的
                                            IndexedColors titleBackgroundColor, IndexedColors headBackgroundColor, IndexedColors dataBackgroundColor) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        checkDataIsNotEmpty(data);

        //创建 excel 表 和工作簿
        XSSFWorkbook workbook = createWorkBook();
        XSSFSheet sheet = createDefaultSheet(workbook);
        // 设置所有列的默认列宽为 20字符
        sheet.setDefaultColumnWidth(20);

        Class<?> cls = data.get(data.size() - 1).getClass();
        List<Field> fields = getClassExcelParamsField(cls, version);

        // 表头数据所在的行数
        int headRowNum;
        // 判断有没有标题数据
        if (null != title) {
            // 获得标题的样式
            XSSFCellStyle style = getExcelStyle(workbook, titleFontName, titleBold, titleSize, titleLevelPosition,
                    titleVerticalPosition, backgroundType, titleBackgroundColor, borderType, borderColor);
            // 如果选择有标题数据则添加标题 和设置表头
            addExcelTitle(sheet, title, fields.size(), style);
            headRowNum = 1;
        } else {
            headRowNum = 0;
        }

        // 创建第一行表头数据
        XSSFRow headRow = createSheetRow(sheet, headRowNum);
        // 获得表格表头的样式
        XSSFCellStyle headStyle = getExcelStyle(workbook, headFontName, headBold, headSize, headLevelPosition,
                headVerticalPosition, backgroundType, headBackgroundColor, borderType, borderColor);
        // 创建表头数据
        for (Field field : fields) {
            ExcelParams annotation = field.getAnnotation(ExcelParams.class);
            XSSFCell cell = createSheetCell(headRow, annotation.columnNum());
            cell.setCellValue(annotation.columnName());
            // 为单个列设置列宽
            int i = annotation.columnWidthNum();
            if (i != 20) {
                // 设置某一列的列宽 width的单位不是字符，所以需要换算成字符的大小
                sheet.setColumnWidth(annotation.columnNum(), (Integer.parseInt(String.valueOf(annotation.columnWidthNum() + 0.62))) * CHARACTER_SIZE);
            }
            cell.setCellStyle(headStyle);
        }
        // 获得表格数据的样式
        XSSFCellStyle style = getExcelStyle(workbook, dataFontName, dataBold, dataSize, dataLevelPosition,
                dataVerticalPosition, backgroundType, dataBackgroundColor, borderType, borderColor);
        // 表明这个是第几行数据
        int rowNum = headRowNum + 1;
        // 创建每一行的数据
        for (Object dataNum : data) {
            XSSFRow row = createSheetRow(sheet, rowNum);
            List<Field> fieldList = getClassExcelParamsField(dataNum.getClass(), version);
            for (Field field : fieldList) {
                ExcelParams annotation = field.getAnnotation(ExcelParams.class);
                XSSFCell cell = createSheetCell(row, annotation.columnNum());
                // 强制访问该属性 除public之外的属性不需要，
                field.setAccessible(true);
                // 创建读取对象方法的对象
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), dataNum.getClass());
                //获得get方法
                Method getMethod = pd.getReadMethod();
                //执行get方法返回一个Object
                Object o = getMethod.invoke(dataNum);
                cell.setCellValue(o != null ? o.toString() : "");
                cell.setCellStyle(style);
            }
            rowNum++;
        }

        return workbook;
    }

    /**
     * 查询模板中所需要的列
     *
     * @param cls     试题对象的class
     * @param version 导出版本
     * @return 所有有 ExcelParams 的属性
     */
    private static List<Field> getClassExcelParamsField(Class<?> cls, ExportVersion version) {
        // 创建转载这个类中所有有这个注解的属性集合
        List<Field> fields = new ArrayList<>(30);

        // 获得所有的属性
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            ExcelParams annotation = field.getAnnotation(ExcelParams.class);
            if (null != annotation) {
                if (version.equals(annotation.exportVersion())) {
                    fields.add(field);
                }
            }
        }

        return fields;
    }

}
