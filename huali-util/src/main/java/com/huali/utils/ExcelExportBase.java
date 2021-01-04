package com.huali.utils;

import com.huali.ExcelExportable;
import com.huali.utils.annotation.ExcelParams;
import com.huali.utils.enums.ExportVersion;
import com.huali.utils.model.ExcelStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导出excel 的工具类的公共方法类
 * <p>
 * 关于 data
 * data 是一个list集合 集合中的对象必须实现 ExcelExportable 接口
 *
 * @author Yang_my
 * @see ExcelExportable 导出 excel 的实体类必须实现的接口
 * 且对象中的属性需要通过 ExcelParams 注解
 * @see ExcelParams 表明这个是excel 所需要的列 其中配置 列名 / 列所在 excel 列数 / 列宽
 * @see ExportVersion 表明本次导出的数据版本
 *
 * <p>
 * 如果只需 要导出内容 没有标题和样式的需求，可以使用 单个参数的 toExcel(data)
 * 如果需要 excel 表格中有关于表述的大标题 可以使用 两个参数的 toExcel(data,title)
 * 如果需要 excel 表有标题，有样式 可以使用 三个参数的 toExcel(data,title,style) style 样式对象
 * @see ExcelStyle 样式对象
 * <p>
 * 注意点：
 * 1. data 集合中，会取最后一个对象做为表头数据的来源，所以，需要使用者在传入 data 集合之前在集合中添加一个空的对象(该对象必须完全为空)
 * 2. 如需要设置任意部分的背景颜色，需要设置全局的背景类型 和边框类型 如遇到问题 可以自行修改
 * 3. 列数 从0开始 1 - 6 ... 按照顺序填写 该类不会判断是否存在重复的列数，只会讲之前的列数覆盖
 * 4. 参数 data 中必须保证 data 不是一个空的 list 集合
 * 5. ExportVersion 必须保导出字段上版本必须一致
 * </p>
 * @since 2020/9/15
 * <p>
 * 所需 pom.xml
 * <dependency>
 * <groupId>org.apache.poi</groupId>
 * <artifactId>poi-ooxml</artifactId>
 * <version>4.1.2</version>
 * </dependency>
 * </p>
 */
public abstract class ExcelExportBase extends ExcelUtilBase {

    /**
     * 存储 sheet 的画图管理器
     */
    private static Map<Integer, XSSFDrawing> drawingMap;

    /**
     * 每一列列宽的乘数 因为每一列的列宽设置的跟excel中的单位不匹配,所以需要这个乘数
     * (Integer.parseInt(String.valueOf(annotation.columnWidthNum() + 0.62))) * CHARACTER_SIZE 才为 最接近的 excel 的列宽
     * 行高不必，可以直接写，没有换算
     */
    public static final int CHARACTER_SIZE = 256;

    /**
     * 获得一个 workBook
     *
     * @return 一个新的 workBook
     */
    public static XSSFWorkbook createWorkBook() {
        return new XSSFWorkbook();
    }

    /**
     * 获得默认的 第一个 sheet页
     *
     * @param workbook excel对象
     * @return 默认的第一个sheet
     */
    public static XSSFSheet createDefaultSheet(XSSFWorkbook workbook) {
        return workbook.createSheet("Sheet1");
    }

    /**
     * 获得一个 sheet
     *
     * @param workbook  excel 对象
     * @param sheetName excel 单页的名称
     * @return 返回 excel 单页
     */
    public static XSSFSheet createSheet(XSSFWorkbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * 获得 sheet 中的一行
     *
     * @param sheet  excel 单页
     * @param rowNum 行号
     * @return 返回 excel 中的行对象
     */
    public static XSSFRow createSheetRow(XSSFSheet sheet, int rowNum) {
        return sheet.createRow(rowNum);
    }

    /**
     * 获得 某个行对象中的一个列对象
     *
     * @param row     excel 行对象
     * @param cellNum 列号
     * @return excel 列对象(某个行对象中的 列数为列号的列对象)
     */
    public static XSSFCell createSheetCell(XSSFRow row, int cellNum) {
        return row.createCell(cellNum);
    }

    /**
     * 检验数据不能为空
     *
     * @param data 数据
     */
    public static void checkDataIsNotEmpty(List<?> data) {
        // 找到 data 中最后一个实体，获得它的class ，然后在获得每个实体属性上的注解value 属性 根据 columnNum列确定该属性是在第几个列上
        if (data.isEmpty()) {
            throw new RuntimeException("数据不能为空");
        }
    }

    /**
     * 创建 excel 标题数据
     *
     * @param sheet   excel 工作表
     * @param title   excel 的标题部分
     * @param cellNum 表头数据的列数
     * @param style   样式对象 如果为null 则不设置样式
     */
    public static void addExcelTitle(XSSFSheet sheet, String title, Integer cellNum, XSSFCellStyle style) {
        /*
          合并单元格
            firstRow: 从哪一行开始
            lastRow:  到哪一行结束
            firstCol: 从哪一列开始
            lastCol:  到哪一列结束
        */
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellNum - 1));

        XSSFRow titleRow = sheet.createRow(0);
        // 设置行高
        titleRow.setHeightInPoints(30);
        XSSFCell cell = titleRow.createCell(0);
        cell.setCellValue(title);

        if (null != style) {
            cell.setCellStyle(style);
            // 如果设置了合并,还设置了边框 需要将合并的边框全部创建出来并且赋上样式对象,不然会有问题
            for (int i = 1; i < cellNum; i++) {
                XSSFCell borderCell = titleRow.createCell(i);
                borderCell.setCellStyle(style);
            }
        }
    }

    /**
     * 设置单元格样式
     *
     * @param workbook         excel 工作簿
     * @param fontName         字体名称 "微软雅黑" / "宋体"
     * @param fontBold         字体是否加粗 true + / false -
     * @param fontSize         字体大小
     * @param levelPosition    水平位置 null(默认居中)
     * @param verticalPosition 垂直位置 null(默认居中)
     * @param backgroundType   背景的前景色类型
     * @param backgroundColor  背景颜色 [传进来的参数到 颜色即可]
     *                         颜色 选择:(https://blog.csdn.net/aoxiangzhe/article/details/92974828)
     *                         -- IndexedColors:enum类 / RED:颜色 / .index:获得RED 颜色的编号
     * @param borderType       边框类型 虚线 / 实线 ...
     * @param borderColor      边框颜色 参考背景颜色取值
     * @return excel样式对象
     */
    public static XSSFCellStyle getExcelStyle(XSSFWorkbook workbook,
                                              String fontName,
                                              Boolean fontBold,
                                              int fontSize,
                                              HorizontalAlignment levelPosition,
                                              VerticalAlignment verticalPosition,
                                              FillPatternType backgroundType,
                                              IndexedColors backgroundColor,
                                              BorderStyle borderType,
                                              IndexedColors borderColor) {
        // 设置字体样式
        XSSFFont font = workbook.createFont();
        font.setFontName(fontName != null ? fontName : "微软雅黑");
        // 字体加粗
        font.setBold(fontBold != null ? fontBold : false);
        // 字体大小
        font.setFontHeightInPoints(fontSize >= 11 ? (short) fontSize : 11);

        // 设置单元格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        // 将字体样式设置到表格样式中
        cellStyle.setFont(font);
        // 水平居中 如果传入的是 null 值 ，则默认水平居中
        cellStyle.setAlignment(levelPosition != null ? levelPosition : HorizontalAlignment.CENTER);
        // 垂直居中 如果传入的是 null 值 ，则默认垂直居中
        cellStyle.setVerticalAlignment(verticalPosition != null ? verticalPosition : VerticalAlignment.CENTER);
        // 设置前景色
        cellStyle.setFillPattern(backgroundType != null ? backgroundType : FillPatternType.NO_FILL);
        // 设置背景色 颜色用 IndexedColors.RED.index 确定颜色
        cellStyle.setFillForegroundColor(backgroundColor != null ? backgroundColor.index : IndexedColors.WHITE1.index);

        // 底部边框 和 底部边框颜色
        cellStyle.setBorderBottom(borderType != null ? borderType : BorderStyle.NONE);
        cellStyle.setBottomBorderColor(borderColor != null ? borderColor.index : IndexedColors.BLACK.index);
        // 左部边框 和 左部边框颜色
        cellStyle.setBorderLeft(borderType != null ? borderType : BorderStyle.NONE);
        cellStyle.setLeftBorderColor(borderColor != null ? borderColor.index : IndexedColors.BLACK.index);
        // 上部边框 和 上部边框颜色
        cellStyle.setBorderTop(borderType != null ? borderType : BorderStyle.NONE);
        cellStyle.setTopBorderColor(borderColor != null ? borderColor.index : IndexedColors.BLACK.index);
        // 右部边框 和 右部边框颜色
        cellStyle.setBorderRight(borderType != null ? borderType : BorderStyle.NONE);
        cellStyle.setRightBorderColor(borderColor != null ? borderColor.index : IndexedColors.BLACK.index);

        return cellStyle;
    }

    /**
     * 插入图片
     * 坐标点：单个单元格的左上角的点。
     * 想要在第 2 行的 第 3 个格子上，插入一个 长 2 个单元格，高 3 个单元格的图片，
     * 则 startCellNum 为 3， startRowNum 为 2， endCellNum 为 5，endRowNum 为 5
     *
     * @param workbook   workbook
     * @param sheet      sheet 页
     * @param imageArray 图片字节数组
     */
    public static void insertImage(XSSFWorkbook workbook, XSSFSheet sheet, Images imageArray) {
        XSSFDrawing dp = getDrawingBySheetHashCode(sheet);

        insertImage(workbook, imageArray, dp);
    }

    /**
     * 批量插入图片
     * 坐标点：单个单元格的左上角的点。
     * 想要在第 2 行的 第 3 个格子上，插入一个 长 2 个单元格，高 3 个单元格的图片，
     * 则 startCellNum 为 3， startRowNum 为 2， endCellNum 为 5，endRowNum 为 5
     *
     * @param workbook workbook
     * @param sheet    sheet 页
     * @param images   批量插入的图片对象
     */
    public static void insertImage(XSSFWorkbook workbook, XSSFSheet sheet, List<Images> images) {
        XSSFDrawing dp = getDrawingBySheetHashCode(sheet);
        for (Images image : images) {
            insertImage(workbook, image, dp);
        }
    }

    /**
     * 插入图片
     *
     * @param workbook   一个workbook
     * @param imageArray 照片对象
     * @param dp         dp画图管理器
     */
    private static void insertImage(XSSFWorkbook workbook, Images imageArray, XSSFDrawing dp) {
        Integer startRowNum = imageArray.getStartRowNum();
        Integer startCellNum = imageArray.getStartCellNum();
        Integer endRowNum = imageArray.getEndRowNum();
        Integer endCellNum = imageArray.getEndCellNum();
        checkCoordinate(startRowNum, startCellNum, endRowNum, endCellNum);
        /**
         * 该构造函数有8个参数
         * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
         * 后四个参数为 起始列，起始行，结束列，结束行
         * 坐标点： 单元格的左上角的点为坐标点
         * 后四个参数的寻找, 先在一个 excel 中 选中图片的区域，
         * 确定下左上角的坐标点，为 起始列，起始行，
         * 确定下右下角的坐标点，为 结束列，结束行
         * excel中的cellNum和rowNum的index都是从0开始的
         */
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,
                Short.parseShort(String.valueOf(startCellNum)),
                startRowNum,
                Short.parseShort(String.valueOf(endCellNum)),
                endRowNum);

        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);

        // 插入图片 todo 按道理讲 应该根据图片的后缀来选择插入图片的类型
        dp.createPicture(anchor, workbook.addPicture(imageArray.getImageByte(), XSSFWorkbook.PICTURE_TYPE_PNG));
        //dp.createPicture(anchor, workbook.addPicture(imageArray, XSSFWorkbook.PICTURE_TYPE_JPEG));
    }


    /**
     * 获得单个 sheet 的画图管理器
     *
     * @param sheet sheet 页
     * @return sheet页的画图管理器
     */
    private static XSSFDrawing getDrawingBySheetHashCode(XSSFSheet sheet) {
        if (sheet == null) {
            throw new RuntimeException("Sheet is Not Null");
        }
        XSSFDrawing dp = drawingMap.get(sheet.hashCode());
        if (dp == null) {
            dp = sheet.createDrawingPatriarch();
        }
        return dp;
    }

    /**
     * 检验插入图片的数据是否符合规则
     *
     * @param startRowNum  起始行坐标点
     * @param startCellNum 起始列坐标点
     * @param endRowNum    结束行坐标点
     * @param endCellNum   结束列坐标点
     */
    private static void checkCoordinate(Integer startRowNum, Integer startCellNum, Integer endRowNum, Integer endCellNum) {
        boolean check = startRowNum != null && startCellNum != null && endRowNum != null && endCellNum != null;

        if (check || (startRowNum > endRowNum) || (startCellNum > endCellNum)) {
            throw new RuntimeException("传入的图片坐标点有误，请检查: \n" +
                    "起始行坐标点：{" + startRowNum + "},\n" +
                    "起始列坐标点：{" + startCellNum + "},\n" +
                    "结束行坐标点：{" + endRowNum + "},\n" +
                    "结束列坐标点：{" + endCellNum + "} ");
        }
    }

    /**
     * 测试插入图片的方法
     *
     * @throws IOException
     */
    private static void testImage() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
             FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\files\\testImage.xlsx"))) {

            BufferedImage bufferImg = ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\files\\a.png"));
            ImageIO.write(bufferImg, "png", byteArrayOut);

            // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            XSSFDrawing dp = sheet.createDrawingPatriarch();
            /**
             * 该构造函数有8个参数
             * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
             * 后四个参数为 起始列，起始行，结束列，结束行
             * 坐标点： 单元格的左上角的点为坐标点
             * 后四个参数的寻找, 先在一个 excel 中 选中图片的区域，
             * 确定下左上角的坐标点，为 起始列，起始行，
             * 确定下右下角的坐标点，为 结束列，结束行
             * excel中的cellNum和rowNum的index都是从0开始的
             */
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 1, 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);

            // 插入图片
            dp.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
            //dp.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));

            workbook.write(fos);
        }
    }
}
