import com.huali.utils.ExcelImportUtil;
import com.huali.utils.enums.ImportVersion;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/21
 */
public class tsetMain {
    public static void main(String[] args) {
        List<?> list = ExcelImportUtil.getList(new Student(), Student.class, "C:\\Users\\Administrator\\Desktop\\files\\a.xlsx", ImportVersion.STUDENT_DTO);
    }
}
