package me.huanmeng.bacterium.util;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Entry {

    public BlockState state;
    public CompoundTag nbt;
    public Map<Property<? extends Comparable<?>>, Comparable<?>> props;

    public Entry(BlockState state) {
        this.state = state;
        this.props = new HashMap<>();
    }

    public <E extends Enum<E> & StringRepresentable> Entry enums(E e, EnumProperty<E> property) {
        props.put(property, e);
        return this;
    }

    public BlockState state() {
        BlockState state = this.state;
        for (Map.Entry<Property<? extends Comparable<?>>, Comparable<?>> entry : props.entrySet()) {
            state = state.setValue(((Property) entry.getKey()), ((Comparable) entry.getValue()));
        }
        return state;
    }

    public Entry enums(String enumName, String enumClass, Object instance) {
        try {
            final Class<?> enumClazz = Class.forName(enumClass);
            if (!enumClazz.isEnum()) {
                return this;
            }
            props.put(((Property<?>) instance), ((Comparable<?>) enumClazz.getDeclaredMethod("valueOf", String.class).invoke(null, enumName)));
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

    public Entry ints(int i, IntegerProperty property) {
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

    public static Entry readFromNbt(CompoundTag entry) {
        final Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(entry.getString("id")));
        final Entry e = new Entry(block.defaultBlockState());
        if (entry.contains("nbt")) {
            e.nbt = entry.getCompound("nbt");
        }
        if (entry.contains("properties")) {
            final CompoundTag properties = entry.getCompound("properties");
            for (String name : properties.getAllKeys()) {
                final CompoundTag a = properties.getCompound(name);
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
                                e.dir(Direction.BY_ID.apply(a.getInt("value")), propertyInstance);
                            }
                            case INT -> {
                                e.ints(a.getInt("value"), propertyInstance);
                            }
                            case ENUM -> {
                                e.enums(a.getString("value"), a.getString("class"), properties);
                            }
                        }
                }
            }
        }
        return e;
    }

    public CompoundTag toNbt() {
        final CompoundTag entry = new CompoundTag();
        final Block block = state.getBlock();
        entry.putString("id", BuiltInRegistries.BLOCK.getKey(block).toString());
        if (nbt != null) {
            entry.put("nbt", nbt);
        }
        if (!props.isEmpty()) {
            final CompoundTag properties = new CompoundTag();
            for (Map.Entry<Property<? extends Comparable<?>>, Comparable<?>> comparableEntry : props.entrySet()) {
                final CompoundTag pro = new CompoundTag();
                final PropertyType type = PropertyType.type(comparableEntry.getKey());
                if (type != null)
                    switch (type) {
                        case DIR -> {
                            pro.putInt("value", ((Direction) comparableEntry.getValue()).get3DDataValue());
                        }
                        case ENUM -> {
                            pro.putString("value", ((Enum<?>) comparableEntry.getValue()).name());
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
