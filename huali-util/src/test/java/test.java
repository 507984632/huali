import com.lkx.util.ExcelUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/21
 */
public class test {
    public static void main(String[] args) throws Exception {

//        List<Object> objects = ExcelUtil.readXls("C:\\Users\\Administrator\\Desktop\\files\\a.xlsx", Student.class);
        List<Student> objects = ExcelUtil.readXls(getContent("C:\\Users\\Administrator\\Desktop\\files\\a.xlsx"), Student.class, new int[]{1});
        for (Object object : objects) {
            System.out.println(object.toString());
        }
    }

    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

}
