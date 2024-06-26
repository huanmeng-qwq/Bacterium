package me.huanmeng.bacterium.util;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

import java.lang.reflect.Field;

/**
 * 2021/12/1<br>
 * Bacteriums_Fabric<br>
 *
 * @author huanmeng_qwq
 */
public enum PropertyType {
    INT,
    ENUM,
    DIR,
    BOOL,
    ;
    public static PropertyType type(Object obj) {
        final Class<?> clazz = obj.getClass();
        if (BooleanProperty.class.isAssignableFrom(clazz)) {
            return PropertyType.BOOL;
        }
        if (EnumProperty.class.isAssignableFrom(clazz)) {
            return PropertyType.ENUM;
        }
        if (IntProperty.class.isAssignableFrom(clazz)) {
            return PropertyType.INT;
        }
        if (DirectionProperty.class.isAssignableFrom(clazz)) {
            return PropertyType.DIR;
        }
        return null;
    }
    public static Object findProperty(Class<?> clazz, int index, Object instance) {
        try {
            final Field field = clazz.getDeclaredFields()[index];
            field.setAccessible(true);
            final Object o = field.get(instance);
            return o == null ? field.get(null) : o;
        } catch (Exception e) {
            return null;
        }
    }

}
