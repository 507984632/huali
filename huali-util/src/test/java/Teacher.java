import com.huali.utils.annotation.ExcelImportParams;
import com.huali.utils.enums.ImportVersion;
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
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @ExcelImportParams(title = "老师名称", version = ImportVersion.STUDENT_DTO)
    private String tName;
    @ExcelImportParams(title = "老师年龄", version = ImportVersion.STUDENT_DTO)
    private Integer tAge;
    @ExcelImportParams(version = ImportVersion.STUDENT_DTO, isObject = true)
    private University university;
}
