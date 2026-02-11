package dev.tamago0314.mossuse.mixin;

import dev.tamago0314.mossuse.ToggleKey;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MossAimMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void mossAimTick(CallbackInfo ci) {
        if (!ToggleKey.ENABLED) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.world == null) return;
        if (client.interactionManager == null) return;
        if (!client.player.isHolding(Items.BONE_MEAL)) return;
        if (!(client.crosshairTarget instanceof BlockHitResult hit)) return;

        ClientPlayerEntity player = client.player;
        World world = client.world;
        ClientPlayerInteractionManager im = client.interactionManager;

        BlockPos pos = hit.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();
        Direction dir = hit.getSide();

        if (block == Blocks.MOSS_CARPET
                || block == Blocks.AZALEA
                || block == Blocks.FLOWERING_AZALEA
                || block == Blocks.GRASS
                || block == Blocks.TALL_GRASS) {

            im.attackBlock(pos, dir);
            player.swingHand(Hand.MAIN_HAND);
            return;
        }

        if (block == Blocks.MOSS_BLOCK) {
            Vec3d hitVec = hit.getPos();
            BlockHitResult bhr = new BlockHitResult(hitVec, dir, pos, false);

            im.interactBlock(player, Hand.MAIN_HAND, bhr);
            player.swingHand(Hand.MAIN_HAND);
        }
    }
}