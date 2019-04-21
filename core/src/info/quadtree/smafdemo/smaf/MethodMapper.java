package info.quadtree.smafdemo.smaf;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodMapper {
    private static String generateMethodName(String prefix, String paramName){
        return prefix + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
    }

    enum MethodType {
        Getter,
        Setter
    }

    private static Map<MethodInfo, Method> methodMap = new HashMap<>();

    private static class MethodInfo {
        private MethodInfo(String fieldName, MethodType type, Class<?> clazz) {
            this.fieldName = fieldName;
            this.type = type;
            this.clazz = clazz;
        }

        String fieldName;
        MethodType type;
        Class<?> clazz;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodInfo that = (MethodInfo) o;
            return Objects.equals(fieldName, that.fieldName) &&
                    type == that.type &&
                    Objects.equals(clazz, that.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldName, type, clazz);
        }
    }

    public static Method getGetterMethod(Class<?> clazz, String fieldName) throws ReflectionException {
        MethodInfo mi = new MethodInfo(fieldName, MethodType.Getter, clazz);
        if (!methodMap.containsKey(mi)){
            String name = generateMethodName("get", fieldName);
            methodMap.put(mi, ClassReflection.getMethod(clazz, name));
        }

        return methodMap.get(mi);
    }

    public static Method getSetterMethod(Class<?> clazz, String fieldName) throws ReflectionException {
        MethodInfo mi = new MethodInfo(fieldName, MethodType.Setter, clazz);
        if (!methodMap.containsKey(mi)){
            String name = generateMethodName("set", fieldName);
            for (Method m : ClassReflection.getMethods(clazz)){
                if (m.getName().equals(name)){
                    methodMap.put(mi, m);
                    break;
                }
            }
        }

        return methodMap.get(mi);
    }
}
