package mist475.endermite_overhaul.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import mist475.endermite_overhaul.EndermiteOverhaul;

public class ItemEndermiteEggRender implements IItemRenderer {

    private final ResourceLocation texture;
    private final IModelCustom model;

    public ItemEndermiteEggRender() {
        texture = new ResourceLocation(EndermiteOverhaul.MODID, "textures/blocks/endermite_egg.png");
        model = AdvancedModelLoader
            .loadModel(new ResourceLocation(EndermiteOverhaul.MODID, "models/endermite_egg.obj"));
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
        GL11.glPushMatrix();
        switch (type) {
            case EQUIPPED_FIRST_PERSON -> {
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslatef(-1F, 0.5F, -0.5F);
            }
            case INVENTORY -> GL11.glTranslatef(-0.5F, -0.5F, -0.1F);
            default -> {}
        }
        this.model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
}
