package mist475.endermite_overhaul.rendering;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemEndermiteEggRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {

        switch (type) {
            case ENTITY:
                GL11.glScalef(1.25f, 1.25f, 1.25f);
                renderEndermiteEgg(-0.5f, -0.45f, -0.5f);
                break;
            case EQUIPPED, INVENTORY:
                GL11.glScalef(1.25f, 1.25f, 1.25f);
                renderEndermiteEgg(0, 0, 0);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glScalef(1.25f, 1.25f, 1.25f);
                renderEndermiteEgg(0, 0, -0.10F);
                break;
            default:
                break;
        }
    }

    private void renderEndermiteEgg(float x, float y, float z) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        RenderTileEntityEndermiteEgg.INSTANCE.renderBase(0, 0, 0);
        GL11.glPopMatrix();
    }
}
