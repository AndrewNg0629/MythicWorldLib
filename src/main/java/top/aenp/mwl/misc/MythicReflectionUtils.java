package top.aenp.mwl.misc;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponsePayload;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestPayload;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public class MythicReflectionUtils {
    private static final Method LoginQueryRequestS2CPacket$readPayload0;
    private static final Method LoginQueryResponseC2SPacket$readPayload0;

    static {
        try {
            LoginQueryRequestS2CPacket$readPayload0 = LoginQueryRequestS2CPacket.class.getDeclaredMethod(EnvironmentDetector.isYarn ? "readPayload" : "method_52287", Identifier.class, PacketByteBuf.class);
            LoginQueryRequestS2CPacket$readPayload0.setAccessible(true);
            LoginQueryResponseC2SPacket$readPayload0 = LoginQueryResponseC2SPacket.class.getDeclaredMethod(EnvironmentDetector.isYarn ? "readPayload" : "method_52290", Integer.TYPE, PacketByteBuf.class);
            LoginQueryResponseC2SPacket$readPayload0.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to get method object.", e);
        }
    }

    public static LoginQueryRequestPayload LoginQueryRequestS2CPacket$readPayload(Identifier id, PacketByteBuf buf) {
        try {
            return (LoginQueryRequestPayload) LoginQueryRequestS2CPacket$readPayload0.invoke(null, id, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LoginQueryResponsePayload LoginQueryResponseC2SPacket$readPayload(int id, PacketByteBuf buf) {
        try {
            return (LoginQueryResponsePayload) LoginQueryResponseC2SPacket$readPayload0.invoke(null, id, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class ReflectedField<C, F> {
        private static final ImmutableMap<Class<?>, Class<?>> PRIMITIVE_TO_BOXED = new ImmutableMap.Builder<Class<?>, Class<?>>()
                .put(byte.class, Byte.class)
                .put(short.class, Short.class)
                .put(int.class, Integer.class)
                .put(long.class, Long.class)
                .put(float.class, Float.class)
                .put(double.class, Double.class)
                .put(boolean.class, Boolean.class)
                .put(char.class, Character.class)
                .build();
        private final Field containedField;
        private final Class<F> fieldType;
        private final boolean isStaticConstant;
        private final F cachedConstant;

        public ReflectedField(@NotNull Class<C> fieldClass, @NotNull Class<F> fieldType, @NotNull String prettyName, @Nullable String intermediaryName) {
            String fieldName = EnvironmentDetector.isYarn || intermediaryName == null ? prettyName : intermediaryName;
            try {
                containedField = fieldClass.getDeclaredField(fieldName);
                this.fieldType = fieldType;
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(String.format("Unable to find field with name: %s, %s", prettyName, intermediaryName), e);
            }
            containedField.setAccessible(true);
            if (!getBoxedType(containedField).isAssignableFrom(fieldType)) {
                throw new IllegalArgumentException("Wrong field type! Check the generics type!");
            }
            int mod = containedField.getModifiers();
            if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
                isStaticConstant = true;
                try {
                    cachedConstant = this.fieldType.cast(containedField.get(null));
                } catch (IllegalAccessException | ClassCastException e) {
                    throw new RuntimeException(String.format("Failed to get value of field: %s", containedField), e);
                }
            } else {
                isStaticConstant = false;
                cachedConstant = null;
            }
        }

        private static Class<?> getBoxedType(Field field) {
            Class<?> fieldType = field.getType();
            Class<?> mappedType = PRIMITIVE_TO_BOXED.get(fieldType);
            return mappedType == null ? fieldType : mappedType;
        }

        public void setFieldValue(C instance, F targetValue) {
            if (isStaticConstant) {
                throw new UnsupportedOperationException(String.format("Field %s is constant, modification not supported.", cachedConstant));
            }
            try {
                containedField.set(instance, targetValue);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException(String.format("Failed to set value of field: %s", containedField), e);
            }
        }

        public F getFieldValue(C instance) {
            if (isStaticConstant) {
                return cachedConstant;
            }
            try {
                Object value = containedField.get(instance);
                Class<F> fieldType = this.fieldType;
                return fieldType.cast(value);
            } catch (IllegalAccessException | ClassCastException e) {
                throw new RuntimeException(String.format("Failed to get value of field: %s", containedField), e);
            }
        }
    }
}
