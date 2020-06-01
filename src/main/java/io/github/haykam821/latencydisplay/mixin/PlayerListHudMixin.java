package io.github.haykam821.latencydisplay.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin extends DrawableHelper {
	@Shadow
	private MinecraftClient client;

	@Unique
	private Text getFormattedLatencyText(int latency) {
		Text text = new LiteralText(Integer.toString(latency));

		if (latency < 0) {
			return text.formatted(Formatting.DARK_GRAY);
		} else if (latency < 150) {
			return text.formatted(Formatting.GREEN);
		} else if (latency < 300) {
			return text.formatted(Formatting.YELLOW);
		} else if (latency < 600) {
			return text.formatted(Formatting.GOLD);
		} else if (latency < 1000) {
			return text.formatted(Formatting.RED);
		} else {
			return text.formatted(Formatting.DARK_RED);
		}
	}

	@Inject(method = "renderLatencyIcon", at = @At("TAIL"))
	public void renderLatencyText(int width, int x, int y, PlayerListEntry playerEntry, CallbackInfo ci) {
		Text latencyText = this.getFormattedLatencyText(playerEntry.getLatency());
		this.drawRightAlignedString(client.textRenderer, latencyText.asFormattedString(), x + width - 12, y, 0);
	}
}