package com.huali.utils;

import com.huali.Charsets;
import com.huali.utils.enums.CheckTemplateType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 * <p>
 * 对模板进行校验
 * </p>
 *
 * @author Yang_my
 * @since 2020/8/21
 */
public final class CheckTemplateUtil {

    /**
     * oss 路径
     */
    private static final String TEMPLATE_URL = "https://chengjiaogj-public.oss-cn-beijing.aliyuncs.com/template/";

    /**
     * 比较用户传入的模板是否匹配
     *
     * @param name  标准模板名称
     * @param sheet 用户模板页
     * @throws IOException .
     */
    public static void compared(CheckTemplateType name, XSSFSheet sheet) throws IOException {
        URL url = new URL(TEMPLATE_URL + URLEncoder.encode(name.getName(), Charsets.UTF_8_NAME) + ".xlsx");
        try (InputStream inputStream = url.openStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet templateSheet = workbook.getSheetAt(0);

            XSSFRow row = templateSheet.getRow(0);
            XSSFRow templateRow = sheet.getRow(0);

            if (row.getPhysicalNumberOfCells() != templateRow.getPhysicalNumberOfCells()) {
                throw new RuntimeException("模板有问题");
            }

            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                XSSFCell cell = row.getCell(i);
                XSSFCell templateCell = templateRow.getCell(i);
                if (!cell.getStringCellValue().equals(templateCell.getStringCellValue())) {
                    throw new RuntimeException("第1行第" + (i + 1) + "列与模板列不相同，请仔细核对。");
                }
            }
        }
    }

}
