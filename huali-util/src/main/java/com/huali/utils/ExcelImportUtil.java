package com.huali.utils;

import com.huali.utils.annotation.ExcelImportParams;
import com.huali.utils.enums.ImportVersion;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/21
 */
public class ExcelImportUtil extends ExcelExportBase {

    /**
     * 导入excel
     *
     * @param o        实体类
     * @param cls      实体类的class 文件
     * @param filePath 导入文件的 地址
     * @return .
     */
    @SneakyThrows
    public static List<?> getList(Object o, Class<?> cls, String filePath, ImportVersion version) {
        return getList(o, cls, FileUtil.getContent(filePath), version);
    }

    /**
     * 导入excel
     *
     * @param o     实体类
     * @param cls   实体类的class 文件
     * @param bytes 导入文件的byte 数组
     * @return .
     */
    @SneakyThrows
    public static List<?> getList(Object o, Class<?> cls, byte[] bytes, ImportVersion version) {
        try (Workbook workbook = XSSFWorkbookFactory.create(new ByteArrayInputStream(bytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);

            List<MyField<Object>> fieldList = getImportFields(o, cls, version);
            for (MyField<?> myField : fieldList) {
                Field field = myField.getField();
                ExcelImportParams annotation = field.getAnnotation(ExcelImportParams.class);
                System.out.println(annotation.title());
            }

        }
        // todo
        return null;
    }

    private static List<MyField<Object>> getImportFields(Object object, Class<?> cls, ImportVersion version) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<MyField<Object>> fields = new ArrayList<>(30);

        // 找到所有的字段
        for (Field field : cls.getDeclaredFields()) {
            ExcelImportParams annotation = field.getAnnotation(ExcelImportParams.class);
            if (annotation != null) {
                if (version.equals(annotation.version())) {
                    if (!annotation.isObject()) {
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                        Method getMethod = pd.getReadMethod();
                        Object o = getMethod.invoke(object);
                        fields.add(new MyField<>(o, field, o != null ? o.toString() : ""));
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
                        List<MyField<Object>> childList = getImportFields(o, o.getClass(), version);
                        fields.addAll(childList);
                    }
                }
            }
        }

        return fields;
    }

}
