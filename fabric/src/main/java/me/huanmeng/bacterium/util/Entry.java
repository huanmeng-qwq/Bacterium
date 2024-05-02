package me.huanmeng.bacterium.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Entry {

    public BlockState state;
    public NbtCompound nbt;
    public Map<Property<? extends Comparable<?>>, Comparable<?>> props;

    public Entry(BlockState state) {
        this.state = state;
        this.props = new HashMap<>();
    }

    public <E extends Enum<E> & StringIdentifiable> Entry enums(E e, EnumProperty<E> property) {
        props.put(property, e);
        return this;
    }

    public BlockState state() {
        BlockState state = this.state;
        for (Map.Entry<Property<? extends Comparable<?>>, Comparable<?>> entry : props.entrySet()) {
            state = state.with(((Property) entry.getKey()), ((Comparable) entry.getValue()));
        }
        return state;
    }

    public Entry enums(String enumName, String enumClass, Integer ordinal, Object instance) {
        try {
            final Class<?> enumClazz = Class.forName(enumClass);
            if (!enumClazz.isEnum()) {
                return this;
            }
            if (ordinal != null) {
                props.put(((Property<?>) instance), ((Comparable<?>) enumClazz.getEnumConstants()[ordinal]));
            } else {
                props.put(((Property<?>) instance), ((Comparable<?>) enumClazz.getDeclaredMethod("valueOf", String.class).invoke(null, enumName)));
            }
        } catch (Exception ignored) {
        }
        return this;
    }

    public Entry bool(boolean b, BooleanProperty property) {
        props.put(property, b);
        return this;
    }

    public Entry bool(boolean b, Object instance) {
        props.put(((Property<?>) instance), b);
        return this;
    }

    public Entry ints(int i, IntProperty property) {
        props.put(property, i);
        return this;
    }

    public Entry ints(int i, Object instance) {
        props.put(((Property<?>) instance), i);
        return this;
    }

    public Entry dir(Direction dir, DirectionProperty property) {
        props.put(property, dir);
        return this;
    }

    public Entry dir(Direction dir, Object instance) {
        props.put(((Property<?>) instance), dir);
        return this;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "state=" + state +
                ", nbt=" + nbt +
                ", props=" + props +
                '}';
    }

    public static Entry readFromNbt(NbtCompound entry) {
        final Block block = Registries.BLOCK.get(new Identifier(entry.getString("id")));
        final Entry e = new Entry(block.getDefaultState());
        if (entry.contains("nbt")) {
            e.nbt = entry.getCompound("nbt");
        }
        if (entry.contains("properties")) {
            final NbtCompound properties = entry.getCompound("properties");
            for (String name : properties.getKeys()) {
                final NbtCompound a = properties.getCompound(name);
                final int index = a.getInt("index");
                final Object propertyInstance = PropertyType.findProperty(block.getClass(), index, block);
                if (propertyInstance != null) {
                    final PropertyType type = PropertyType.type(properties);
                    if (type != null)
                        switch (type) {
                            case BOOL -> {
                                e.bool(a.getBoolean("value"), propertyInstance);
                            }
                            case DIR -> {
                                e.dir(Direction.byId(a.getInt("value")), propertyInstance);
                            }
                            case INT -> {
                                e.ints(a.getInt("value"), propertyInstance);
                            }
                            case ENUM -> {
                                int ordinal = -1;
                                if (a.contains("ordinal")) {
                                    ordinal = a.getInt("ordinal");
                                }
                                e.enums(a.getString("value"), a.getString("class"), ordinal == -1 ? null : ordinal, properties);
                            }
                        }
                }
            }
        }
        return e;
    }

    public NbtCompound toNbt() {
        final NbtCompound entry = new NbtCompound();
        final Block block = state.getBlock();
        entry.putString("id", Registries.BLOCK.getId(block).toString());
        if (nbt != null) {
            entry.put("nbt", nbt);
        }
        if (!props.isEmpty()) {
            final NbtCompound properties = new NbtCompound();
            for (Map.Entry<Property<? extends Comparable<?>>, Comparable<?>> comparableEntry : props.entrySet()) {
                final NbtCompound pro = new NbtCompound();
                final PropertyType type = PropertyType.type(comparableEntry.getKey());
                if (type != null)
                    switch (type) {
                        case DIR -> {
                            pro.putInt("value", ((Direction) comparableEntry.getValue()).getId());
                        }
                        case ENUM -> {
                            pro.putString("value", ((Enum<?>) comparableEntry.getValue()).name());
                            pro.putInt("ordinal", ((Enum<?>) comparableEntry.getValue()).ordinal());
                            pro.putString("class", comparableEntry.getValue().getClass().getName());
                        }
                        case INT -> {
                            pro.putInt("value", ((Integer) comparableEntry.getValue()));
                        }
                        case BOOL -> {
                            pro.putBoolean("value", ((Boolean) comparableEntry.getValue()));
                        }
                    }

                int index = -1;
                try {
                    final Class<? extends Block> clazz = block.getClass();
                    final Field[] declaredFields = clazz.getDeclaredFields();
                    for (int i = 0; i < declaredFields.length; i++) {
                        final Field field = declaredFields[i];
                        field.setAccessible(true);
                        if (field.get(block) == comparableEntry.getKey()) {
                            index = i;
                            break;
                        }
                    }
                    pro.putInt("index", index);
                } catch (Exception ignored) {
                }
                if (index == -1) {
                    continue;
                }
                properties.put(comparableEntry.getKey().getName(), pro);
            }
            entry.put("properties", properties);
        }
        return entry;
    }
}
