import com.huali.redis.model.User;

/**
 * @author myUser
 * @date 2021-01-26 16:56
 **/
public class testA {
    public static void main(String[] args) {
//        ParameterizedType p = (ParameterizedType) new User().getClass().getGenericSuperclass();
//        Type model = p.getActualTypeArguments()[0];
//        System.out.println(model.getTypeName() + ":" + 2);

        String name = new User().getClass().getName();
        System.out.println(name);
    }
}
