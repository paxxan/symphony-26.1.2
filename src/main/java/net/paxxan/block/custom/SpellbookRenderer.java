package net.paxxan.block.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;

import java.util.ArrayList;
import java.util.List;



public class SpellbookRenderer implements BlockEntityRenderer<SpellbookStackBlockEntity, SpellbookRenderer.State> {
    private static final float BOOK_HEIGHT = 4f / 16f;
    private static final float[] YAW = {0f, 9f, -7f, 13f};


    private final ItemModelResolver itemModelResolver;

    public SpellbookRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemModelResolver = ctx.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState {
        public final List<ItemStackRenderState> books = new ArrayList<>();
        public final List<Float> yaws = new ArrayList<>();
    }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(SpellbookStackBlockEntity blockEntity, State state, float partialTick,
                                   Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, crumbling);
        state.books.clear();
        state.yaws.clear();
        for (SpellbookStackBlockEntity.Book book : blockEntity.getBooks()) {
            ItemStack stack = new ItemStack(ModItems.SPELLBOOK);
            stack.set(ModComponents.SPELL_ID, book.spell().getPath());
            ItemStackRenderState renderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(renderState, stack, ItemDisplayContext.FIXED,
                    blockEntity.getLevel(), null, 0);
            state.books.add(renderState);
            state.yaws.add(book.yaw());
        }
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        for (int i = 0; i < state.books.size() && i < 4; i++) {
            pose.pushPose();
            pose.translate(0.5f, BOOK_HEIGHT * i + BOOK_HEIGHT / 2f, 0.5f);
            pose.mulPose(Axis.YP.rotationDegrees(state.yaws.get(i)));
            pose.scale(1f, 4f / 3f, 1f);
            state.books.get(i).submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
        }
    }
}
