package net.paxxan.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.paxxan.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpellbookStackBlockEntity extends BlockEntity {
    private List<Book> books = List.of();

    public SpellbookStackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SPELLBOOK_STACK_BE, pos, state);
    }

    public record Book(Identifier spell, float yaw) {
        public static final Codec<Book> CODEC = RecordCodecBuilder.create(i -> i.group(
                Identifier.CODEC.fieldOf("spell").forGetter(Book::spell),
                Codec.FLOAT.fieldOf("yaw").forGetter(Book::yaw)
        ).apply(i, Book::new));
    }

    public void addBook(Identifier spell, float yaw) {
        List<Book> updated = new ArrayList<>(books);
        updated.add(new Book(spell, yaw));
        books = List.copyOf(updated);
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public @Nullable Book removeTopBook() {
        if (books.isEmpty()) return null;
        List<Book> updated = new ArrayList<>(books);
        Book top = updated.removeLast();
        books = List.copyOf(updated);
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
        return top;
    }

    public List<Book> getBooks() { return books; }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("books", Book.CODEC.listOf(), books);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        books = input.read("books", Book.CODEC.listOf()).orElse(List.of());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }


}
