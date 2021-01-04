import com.huali.utils.annotation.ExcelImportParams;
import com.huali.utils.enums.ImportVersion;
import com.lkx.util.Excel;
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
public class University {
    @ExcelImportParams(title = "学校名称", version = ImportVersion.STUDENT_DTO)
    private String uName;
    @ExcelImportParams(title = "学校地址", version = ImportVersion.STUDENT_DTO)
    private String uAddress;
}
