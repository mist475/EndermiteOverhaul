package mist475.endermite_overhaul.rendering;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mist475.endermite_overhaul.EndermiteOverhaulUtils;

public class BlockBreakParticleHandler implements IMessageHandler<BlockBreakParticleMessage, IMessage> {

    @Override
    public IMessage onMessage(BlockBreakParticleMessage message, MessageContext ctx) {
        handleMessage(message);
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void handleMessage(BlockBreakParticleMessage message) {
        var world = Minecraft.getMinecraft().theWorld;
        EndermiteOverhaulUtils.blockDestroyParticles(world, message.x, message.y, message.z);
    }
}
