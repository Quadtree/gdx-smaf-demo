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

    private static Map<String, Method> methodMap = new HashMap<>();

    private static String methodInfo(String fieldName, MethodType methodNode, Class<?> clazz){
        StringBuilder sb = new StringBuilder();
        sb.append(fieldName);
        sb.append("_");
        sb.append(methodNode);
        sb.append("_");
        sb.append(clazz.getName());
        return sb.toString();
    }

    public static Method getGetterMethod(Class<?> clazz, String fieldName) throws ReflectionException {
        String mi = methodInfo(fieldName, MethodType.Getter, clazz);
        if (!methodMap.containsKey(mi)){
            String name = generateMethodName("get", fieldName);
            methodMap.put(mi, ClassReflection.getMethod(clazz, name));
        }

        return methodMap.get(mi);
    }

    public static Method getSetterMethod(Class<?> clazz, String fieldName) throws ReflectionException {
        String mi = methodInfo(fieldName, MethodType.Setter, clazz);
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
