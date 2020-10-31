import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Cloner {
    private static Constructor classConstructor;

    public static <T> T cloneObj(T obj) throws InvocationTargetException,
            InstantiationException, CannotClonedException, IllegalAccessException {
        Class clazz = obj.getClass();
        T clone = newInstance(clazz);
        for (Field field : getDeclaredFilds(clazz)) {
            cloneField(obj, clone, field);
        }
        return clone;
    }

    private static <T> void cloneField(T obj, T clone, Field field) throws IllegalAccessException {
        if (field.getType().isPrimitive()||field.getType()==String.class){
            field.set(clone,field.get(obj));
        }
        if (field.getType().isArray()){
           //TODO cloneObjectArray and clonePrimitiveArray
        }
    }

    private static <T> T newInstance(Class clazz) throws IllegalAccessException,
            InvocationTargetException, InstantiationException, CannotClonedException {
        Constructor constructor = getConstructor(clazz);
        if (constructor == null) {
            throw new CannotClonedException(clazz + "hasn't default constructor");
        }
        return (T) constructor.newInstance();
    }

    private static Constructor getConstructor(Class clazz) {
        for (Constructor cons : clazz.getConstructors()) {
            if (cons.getParameterTypes().length == 0) {
                cons.setAccessible(true);
                return cons;
            }
        }
        return null;
    }

    private static List<Field> getDeclaredFilds(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fieldList.add(field);
        }
        return fieldList;
    }
}
