package me.huanmeng.bacterium.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class Entry {
    private static final JsonOps JSONOPS = JsonOps.COMPRESSED;
    public static final MapCodec<Entry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("block").forGetter(e -> e.state.getBlockHolder().getRegisteredName()),
            CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(e -> Optional.ofNullable(e.nbt)),
            //
            new Codec<Map<String, JsonElement>>() {

                @Override
                public <T> DataResult<T> encode(final Map<String, JsonElement> input, final DynamicOps<T> ops, final T prefix) {
                    final RecordBuilder<T> mappedBuilder = ops.mapBuilder();
                    input.forEach((k, v) -> mappedBuilder.add(k, JSONOPS.convertTo(ops, v)));
                    return mappedBuilder.build(prefix);
                }

                @Override
                public <T> DataResult<Pair<Map<String, JsonElement>, T>> decode(final DynamicOps<T> ops, final T input) {
                    Map<String, JsonElement> result = new HashMap<>();
                    return ops.getMap(input).flatMap(map -> {
                        map.entries().forEach(entry -> {
                            String key = ops.getStringValue(entry.getFirst()).getOrThrow();
                            final T second = entry.getSecond();
                            result.put(key, ops.convertTo(JSONOPS, second));
                        });
                        return DataResult.success(Pair.of(result, input));
                    });
                }
            }.fieldOf("props").forGetter(e -> e.props)
    ).apply(instance, Entry::load));

    public BlockState state;
    private CompoundTag nbt;
    private Map<String, JsonElement> props;


    @SuppressWarnings({"unchecked", "rawtypes"})
    public Entry(BlockState state, @Nullable Level level, @Nullable BlockEntity blockEntity) {
        this.state = state;
        this.props = new HashMap<>();
        for (final Property<?> property : state.getProperties()) {
            final Object value = state.getValue(property);
            final DataResult<JsonElement> object = property.valueCodec().encodeStart(JSONOPS, new Property.Value(property, (Comparable) value));
            this.props.put(property.getName(), object.getOrThrow());
        }
        if (level != null && blockEntity != null) {
            this.nbt = blockEntity.saveWithFullMetadata(level.registryAccess());
        }
    }

    public void place(Level level, BlockPos pos) {
        if (level == null) return;
        final BlockState blockState = createState();
        level.setBlockAndUpdate(pos, blockState);
        if (this.nbt != null && level.getBlockEntity(pos) != null) {
            final BlockEntity blockEntity = BlockEntity.loadStatic(pos, blockState, this.nbt, level.registryAccess());
            if (blockEntity != null) level.setBlockEntity(blockEntity);
        }
    }

    public BlockState createState() {
        BlockState state = this.state;
        for (final Property<?> property : state.getProperties()) {
            if (this.props.containsKey(property.getName())) {
                state = setPropertyValue(state, property);
            }
        }
        return state;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> BlockState setPropertyValue(BlockState state, Property<T> property) {
        JsonElement value = this.props.get(property.getName());
        if (property.getValueClass().isInstance(value)) {
            try {
                return state.setValue(property, property.valueCodec().decode(JSONOPS, value).getOrThrow().getFirst().value());
            } catch (Throwable e) {
                return state;
            }
        }
        return state;
    }

    @SuppressWarnings("unchecked")
    private static Entry load(String blockId, Optional<CompoundTag> nbt, Map<String, JsonElement> props) {
        final Optional<Block> block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(blockId));
        if (block.isEmpty()) return null;
        final Entry entry = new Entry(block.get().defaultBlockState(), null, null);
        entry.nbt = nbt.orElse(null);
        entry.props = props;
        return entry;
    }

    public void write(ValueOutput output) {
        output.store(CODEC, this);
    }

    @Override
    public String toString() {
        return "Entry{" + "state=" + state + ", nbt=" + nbt + ", props=" + props + '}';
    }
}