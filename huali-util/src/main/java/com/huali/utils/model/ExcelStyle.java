package com.huali.utils.model;


import com.huali.ExcelExportable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;

/**
 * <p>
 * 导出excel 的样式对象
 * <p>
 * 该对象分为4部分
 * 第一部分是全局作用的为 excel全局样式
 * 第二部分是作用于标题的 标题样式
 * 第三部分是作用于标题的 表头样式
 * 第四部分是作用于标题的 数据样式
 * 关于单元格字体水平 垂直默认是居中的状态
 * <p>
 * 注意点：
 * 1.如需要设置任意部分的背景颜色，需要设置全局的背景类型 和边框类型
 * </p>
 *
 * @author Yang_my
 * @since 2020/9/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelStyle implements ExcelExportable {
    /*************************                          所需常量设置                       *******************************/
    private static final String All_SECTION = "all";
    private static final String TITLE_SECTION = "title";
    private static final String HEAD_SECTION = "head";
    private static final String DATA_SECTION = "data";


    /*************************                       设置excel全局样式                     *******************************/
    /**
     * 全局背景颜色的类型
     */
    private FillPatternType backgroundType;

    /**
     * excel 全局边框的类型
     */
    private BorderStyle borderType;
    /**
     * excel 全局边框的颜色
     */
    private IndexedColors borderColor;

    /*************************                         设置标题样式                        *******************************/
    /**
     * 标题背景颜色
     */
    private IndexedColors titleBackgroundColor;

    /**
     * 标题字体类型的名称
     */
    private String titleFontName;

    /**
     * 标题字体是否粗体展示
     */
    private Boolean titleBold;

    /**
     * 标题字体大小
     */
    private int titleSize;

    /**
     * 标题字体水平方向的位置
     */
    private HorizontalAlignment titleLevelPosition;

    /**
     * 标题字体垂直方向的位置
     */
    private VerticalAlignment titleVerticalPosition;


    /*************************                         设置表头样式                        *******************************/
    /**
     * 表头背景颜色
     */
    private IndexedColors headBackgroundColor;

    /**
     * 表头字体类型的名称
     */
    private String headFontName;

    /**
     * 表头字体是否粗体展示
     */
    private Boolean headBold;

    /**
     * 表头字体大小
     */
    private int headSize;

    /**
     * 表头字体水平方向的位置
     */
    private HorizontalAlignment headLevelPosition;

    /**
     * 表头字体垂直方向的位置
     */
    private VerticalAlignment headVerticalPosition;


    /*************************                         设置数据样式                        *******************************/
    /**
     * 数据背景颜色
     */
    private IndexedColors dataBackgroundColor;
    /**
     * 数据字体类型的名称
     */
    private String dataFontName;

    /**
     * 数据字体是否全部粗体展示
     */
    private Boolean dataBold;

    /**
     * 数据字体大小
     */
    private int dataSize;

    /**
     * 数据字体水平方向的位置
     */
    private HorizontalAlignment dataLevelPosition;

    /**
     * 数据字体垂直方向的位置
     */
    private VerticalAlignment dataVerticalPosition;

}