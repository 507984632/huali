package com.huali.utils;

import com.huali.ExcelExportable;
import com.huali.utils.annotation.ExcelParams;
import com.huali.utils.enums.ExportVersion;
import com.huali.utils.model.ExcelStyle;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * 导出excel 的工具类的实现类之一
 * 相较于第一版本的 ExcelExportUtil 类来说，简化了写DTO的烦恼
 * </p>
 */
public class ExcelExportAnywhereUtil extends ExcelExportBase {

    private ExcelExportAnywhereUtil() {
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
     * @param version               导出版本
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
        List<MyField<Object>> fields = getClassExcelParamsField(data.get(data.size() - 1), cls, version);

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
        // 创建列的字段的集合
        Set<Integer> columnNums = new HashSet<>();
        // 创建表头数据
        for (MyField<Object> myField : fields) {
            Field field = myField.getField();
            ExcelParams annotation = field.getAnnotation(ExcelParams.class);
            // 判断是否存在这个列的列号
            if (!columnNums.add(annotation.columnNum())) {
                /**
                 * 存在跳过，则能保证，不会将覆盖之前的数据
                 *  TODO, 可以将所有第二次出现的列号记录下来，当遍历完所有未重复的列号之后，再将所有重复的列号拼到最后即可
                 *   实现思路： 列号所有不重复的列号，可以通过一个变量记录下来，
                 *   然后重复字段的列号 可以通过 ((字段原列号 + 1) + 不重复的列号（应该是用户输入的最大，也可以理解为表头行中最后一列的列号）)
                 */
                continue;
            }
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
            List<MyField<Object>> fieldList = getClassExcelParamsField(dataNum, dataNum.getClass(), version);
            columnNums = new HashSet<>();
            for (MyField<Object> myField : fieldList) {
                Field field = myField.getField();
                ExcelParams annotation = field.getAnnotation(ExcelParams.class);
                // 判断是否存在这个列的列号
                if (!columnNums.add(annotation.columnNum())) {
                    /**
                     * 存在跳过，则能保证，不会将覆盖之前的数据
                     *  TODO, 可以将所有第二次出现的列号记录下来，当遍历完所有未重复的列号之后，再将所有重复的列号拼到最后即可
                     *   实现思路： 列号所有不重复的列号，可以通过一个变量记录下来，
                     *   然后重复字段的列号 可以通过 ((字段原列号 + 1) + 不重复的列号（应该是用户输入的最大，也可以理解为表头行中最后一列的列号）)
                     */
                    continue;
                }
                XSSFCell cell = createSheetCell(row, annotation.columnNum());
                // 强制访问该属性 除public之外的属性不需要，
                field.setAccessible(true);
                cell.setCellValue(myField.getStr());
                cell.setCellStyle(style);
            }
            rowNum++;
        }

        return workbook;
    }

    /**
     * 查询模板中所需要的列
     *
     * @param object  导出最顶层的对象
     * @param cls     导出最顶层对象的 class文件
     * @param version 导出数据的版本
     * @return 所有有 ExcelParams 的属性
     */
    private static List<MyField<Object>> getClassExcelParamsField(Object object, Class<?> cls, ExportVersion version) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        // 创建转载这个类中所有有这个注解的属性集合
        List<MyField<Object>> fields = new ArrayList<>(30);

        // 获得所有的属性
        for (Field field : cls.getDeclaredFields()) {
            ExcelParams annotation = field.getAnnotation(ExcelParams.class);
            if (null != annotation) {
                if (annotation.exportVersion().equals(version)) {
                    if (!annotation.isObject()) {
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                        Method getMethod = pd.getReadMethod();
                        //执行get方法返回一个Object
                        Object o = getMethod.invoke(object);
                        fields.add(new MyField<>(field, o != null ? o.toString() : ""));
                    } else {
                        // 强制访问该属性 除public之外的属性不需要，
                        field.setAccessible(true);
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                        //获得get方法
                        Method getMethod = pd.getReadMethod();
                        Object o = getMethod.invoke(object);
                        if (Objects.isNull(o)) {
                            continue;
                        }
                        List<MyField<Object>> childList = getClassExcelParamsField(o, o.getClass(), version);
                        fields.addAll(childList);
                    }
                }
            }
        }

        return fields;
    }

}
