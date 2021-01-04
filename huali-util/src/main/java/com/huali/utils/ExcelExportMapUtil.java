package com.huali.utils;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.huali.utils.model.ExcelStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导出 excel 的工具类
 * </p>
 * 该类相交之前的类有逻辑上的转变，通过
 * 表达式取值的方法获得实体对象中的值,但是需要使用这自己书写表达式的值
 * <p>
 * 所需 pom.xml
 * <dependency>
 * <groupId>com.googlecode.aviator</groupId>
 * <artifactId>aviator</artifactId>
 * <version>4.1.2</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.68</version>
 * </dependency>
 *
 * @author Yang_my
 * @since 2020/12/18
 */
@Slf4j
public class ExcelExportMapUtil extends ExcelExportBase {

    private ExcelExportMapUtil() {
    }

    /**
     * <p>
     * ExcelExportMapUtil 单个列的参数对象
     *
     * @author Yang_my
     * </p>
     * @see ExcelExportMapUtil
     * @since 2020/12/18
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Deprecated
    public static class ExcelColumnParams {
        /**
         * 列名
         */
        private String columnName;

        /**
         * 列宽
         */
        private String columnWidthNum;

        /**
         * 单个单元格的样式对象
         */
        private ExcelStyle style;
    }

    /**
     * 将查询结果封装成 excel表 没有标题
     *
     * @param data   sql查询的结果
     * @param params params 调用者的参数信息
     *               key：单个参数的路径(即如果想获得 data 某个元素中的 name 属性，key = name, 元素是一个对象，想要获得对象中的 age 则 key=【对象的元素名.age】)
     *               value：该单元格的参数信息 ExcelColumnParams
     * @return 封装好的excel
     * @see ExcelColumnParams
     */
    public static XSSFWorkbook toExcelFile(List<?> data, LinkedHashMap<String, String> params) {
        return toExcelFile(data, params, null);
    }


    /**
     * 将查询结果封装成 excel表 没有标题,没有任何样式
     *
     * @param data   数据
     * @param params 数据解析规则
     * @param title  标题
     * @return 封装好的excel
     */
    public static XSSFWorkbook toExcelFile(List<?> data, LinkedHashMap<String, String> params, String title) {
        return toExcelFile(data, params, title, new ExcelStyle());
    }

    /**
     * 将查询结果封装成 excel表 没有标题,没有任何样式
     *
     * @param data       数据
     * @param params     数据解析规则
     * @param title      标题
     * @param excelStyle 样式对象
     * @return 封装好的excel
     */
    public static XSSFWorkbook toExcelFile(List<?> data, LinkedHashMap<String, String> params, String title, ExcelStyle excelStyle) {
        return toExcelFile(data, params, title,
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
     * @param data                  数据
     * @param params                数据解析规则
     * @param title                 标题数据 可以选择不填写
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
     */
    private static XSSFWorkbook toExcelFile(List<?> data, LinkedHashMap<String, String> params, String title,
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
                                            IndexedColors titleBackgroundColor, IndexedColors headBackgroundColor, IndexedColors dataBackgroundColor) {
        checkDataIsNotEmpty(data);

        XSSFWorkbook workBook = createWorkBook();
        XSSFSheet defaultSheet = createDefaultSheet(workBook);
        // 设置所有列的默认列宽为 20字符
        defaultSheet.setDefaultColumnWidth(20);

        // 表头数据所在的行数
        int headRowNum;
        // 判断有没有标题数据
        if (null != title) {
            // 获得标题的样式
            XSSFCellStyle style = getExcelStyle(workBook, titleFontName, titleBold, titleSize, titleLevelPosition,
                    titleVerticalPosition, backgroundType, titleBackgroundColor, borderType, borderColor);
            // 如果选择有标题数据则添加标题 和设置表头
            addExcelTitle(defaultSheet, title, params.size(), style);
            headRowNum = 1;
        } else {
            headRowNum = 0;
        }

        // 设置表头数据
        XSSFRow headRow = createSheetRow(defaultSheet, headRowNum);
        // 获得表格表头的样式
        XSSFCellStyle headStyle = getExcelStyle(workBook, headFontName, headBold, headSize, headLevelPosition,
                headVerticalPosition, backgroundType, headBackgroundColor, borderType, borderColor);
        int cellNum = 0;
        for (Map.Entry<String, String> headEntry : params.entrySet()) {
            XSSFCell headCell = createSheetCell(headRow, cellNum);
            // 设置表头值
            headCell.setCellValue(headEntry.getValue());
            // 设置表头样式
            headCell.setCellStyle(headStyle);
            cellNum++;
        }

        // 获得表格数据的样式
        XSSFCellStyle style = getExcelStyle(workBook, dataFontName, dataBold, dataSize, dataLevelPosition,
                dataVerticalPosition, backgroundType, dataBackgroundColor, borderType, borderColor);
        // 表明这个是第几行数据
        int rowNum = headRowNum + 1;
        // 遍历数据
        for (Object dataObject : data) {
            XSSFRow row = createSheetRow(defaultSheet, rowNum);
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dataObject));
            int dataCellNum = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // 获得 数据中的值
                Object execute;
                try {
                    execute = AviatorEvaluator.execute(entry.getKey(), jsonObject);
                } catch (ExpressionRuntimeException e) {
                    log.warn("该值所在的对象不存在，key:{},data:{}", entry.getKey(), jsonObject);
                    execute = null;
                }
                // 创建每一个数据的列
                XSSFCell cell = createSheetCell(row, dataCellNum);
                cell.setCellValue(execute != null ? execute.toString() : "");
                cell.setCellStyle(style);
                dataCellNum++;
            }
            rowNum++;
        }

        return workBook;
    }


}
