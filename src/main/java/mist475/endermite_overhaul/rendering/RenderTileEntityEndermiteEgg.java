package mist475.endermite_overhaul.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import mist475.endermite_overhaul.EndermiteOverhaul;
import mist475.endermite_overhaul.TileEntityEndermiteEgg;

public class RenderTileEntityEndermiteEgg extends TileEntitySpecialRenderer {

    public static RenderTileEntityEndermiteEgg INSTANCE;
    private final ResourceLocation texture;
    private final IModelCustom model;

    public RenderTileEntityEndermiteEgg() {
        texture = new ResourceLocation(EndermiteOverhaul.MODID, "textures/blocks/endermite_egg.png");
        model = AdvancedModelLoader
            .loadModel(new ResourceLocation(EndermiteOverhaul.MODID, "models/endermite_egg.obj"));
    }

    public void renderBase(double posX, double posY, double posZ) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glPushMatrix();
        GL11.glTranslated(posX + 0.5, posY, posZ + 0.5);
        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTick) {
        if (!(te instanceof TileEntityEndermiteEgg)) return;
        renderBase(posX, posY, posZ);
    }
}
