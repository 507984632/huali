import com.huali.utils.annotation.ExcelImportParams;
import com.huali.utils.enums.ImportVersion;
import com.lkx.util.Excel;
import com.lkx.util.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @ExcelImportParams(title = "学生姓名", version = ImportVersion.STUDENT_DTO)
    private String name;
    @ExcelImportParams(title = "学生性别", version = ImportVersion.STUDENT_DTO)
    private Boolean sex;

    @ExcelImportParams(version = ImportVersion.STUDENT_DTO, isObject = true)
    Teacher teacher;

    @ExcelImportParams(title = "老师名称", version = ImportVersion.STUDENT_DTO)
    private String tName;
    @ExcelImportParams(title = "老师年龄", version = ImportVersion.STUDENT_DTO)
    private Integer tAge;
    @ExcelImportParams(title = "学校名称", version = ImportVersion.STUDENT_DTO)
    private String uName;
    @ExcelImportParams(title = "学校地址", version = ImportVersion.STUDENT_DTO)
    private String uAddress;
}
