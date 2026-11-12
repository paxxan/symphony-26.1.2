package net.paxxan.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.paxxan.Symphony;
import net.paxxan.block.ModBlocks;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.particle.ModParticles;
import org.jspecify.annotations.Nullable;

public class SpellbookStackBlock extends BaseEntityBlock {
    public static final MapCodec<SpellbookStackBlock> CODEC = simpleCodec(SpellbookStackBlock::new);
    public static final IntegerProperty BOOKS = IntegerProperty.create("books", 1, 4);

    public SpellbookStackBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(BOOKS, 1));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BOOKS);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpellbookStackBlockEntity(pos, state);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        return !ctx.isSecondaryUseActive()
                && ctx.getItemInHand().is(this.asItem())
                && state.getValue(BOOKS) < 4;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState existing = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (existing.is(this)) {
            return existing.setValue(BOOKS, Math.min(4, existing.getValue(BOOKS) + 1));
        }
        return super.getStateForPlacement(ctx);
    }

    private boolean full(BlockState state) {
        return !(state.getValue(BOOKS) < 4);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof SpellbookStackBlockEntity blockEntity) {
            Identifier id = Symphony.id(stack.get(ModComponents.SPELL_ID));
            if (id != null) {
                float yaw = ((Math.round(((placer != null) ? -placer.getYRot() : 0f) / 45f) * 45f) - 90f) + (level.getRandom().nextInt(3) - 1) * 15f;
                blockEntity.addBook(id, yaw);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        return belowState.is(ModBlocks.SPELLBOOK_STACK) ? full(belowState) : !belowState.getCollisionShape(level, below).isEmpty();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                   @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide() && !state.canSurvive(level, pos)) {
            dropBooks(level, pos);
            paperBurst(level, pos, state.getValue(BOOKS) * 0.25, 6);
            level.destroyBlock(pos, false);
        }
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }

    private static void dropBooks(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof SpellbookStackBlockEntity be) {
            for (SpellbookStackBlockEntity.Book b : be.getBooks()) {
                ItemStack stack = new ItemStack(ModItems.SPELLBOOK);
                stack.set(ModComponents.SPELL_ID, b.spell().getPath());
                Block.popResource(level, pos, stack);
            }
        }
    }



    private static void paperBurst(Level level, BlockPos pos, double height, int count) {
        if (level instanceof ServerLevel serverLevel) {
            level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS,
                    0.8f, 0.9f + level.getRandom().nextFloat() * 0.3f);
            serverLevel.sendParticles(ModParticles.PAPER_PARTICLE,
                    pos.getX() + 0.5, pos.getY() + height, pos.getZ() + 0.5,
                    count,
                    0.25, 0.2, 0.25,
                    0.07);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            paperBurst(level, pos, state.getValue(BOOKS) * 0.25,6 * state.getValue(BOOKS));
        }
        if (!level.isClientSide() && !player.isCreative()
                && level.getBlockEntity(pos) instanceof SpellbookStackBlockEntity blockEntity) {
            dropBooks(level, pos);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Block.box(4, 0, 4, 12, 4 * state.getValue(BOOKS), 12);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(ModItems.SPELLBOOK)) return InteractionResult.PASS;
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof SpellbookStackBlockEntity blockEntity) {
            SpellbookStackBlockEntity.Book top = blockEntity.removeTopBook();
            if (top == null) return InteractionResult.PASS;

            ItemStack book = new ItemStack(ModItems.SPELLBOOK);
            book.set(ModComponents.SPELL_ID, top.spell().getPath());
            if (!player.getInventory().add(book)) {
                player.drop(book, false);
            }

            int remaining = blockEntity.getBooks().size();
            if (remaining <= 0) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(BOOKS, remaining), Block.UPDATE_ALL);
            }
            paperBurst(level, pos, state.getValue(BOOKS) * 0.25, 6);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
