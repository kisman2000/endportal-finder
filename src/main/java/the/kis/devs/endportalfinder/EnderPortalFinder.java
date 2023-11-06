package the.kis.devs.endportalfinder;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("DataFlowIssue")
public class EnderPortalFinder implements ModInitializer {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private static Vector2d POS1 = null;
    private static Vector2d POS2 = null;
    private static Vector2d POS3 = null;

    private static float YAW1 = Float.NaN;
    private static float YAW2 = Float.NaN;

    private static float Y = Float.NaN;

    private static KeyBinding REGISTER_POS;

    private static KeyBinding RESET_POS;

    private float wrap(float yaw) {
        if(yaw < 0) {
            return -yaw;
        } else {
            return -yaw + 360f;
        }
    }

    private void info(String message) {
        mc.inGameHud.getChatHud().addMessage(Text.literal(message));
    }

    public static void onRenderWorld(MatrixStack matrices) {
        if(POS3 != null) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            Vec3d camera = mc.getEntityRenderDispatcher().camera.getPos().negate();
            Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotateX((float) -Math.toRadians(mc.gameRenderer.getCamera().getPitch())).rotateY((float) -Math.toRadians(mc.gameRenderer.getCamera().getYaw()));
            Vec3d pos = new Vec3d(POS3.x, Y, POS3.y).add(camera);

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.lineWidth(2f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();

            buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            buffer.vertex(matrix4f, (float) eyes.x, (float) eyes.y, (float) eyes.z).color(-1).next();
            buffer.vertex(matrix4f, (float) pos.x, (float) pos.y, (float) pos.z).color(-1).next();

            tessellator.draw();

            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
        }
    }

    @Override
    public void onInitialize() {
        REGISTER_POS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.endportalfinder.register_pos",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.endportalfinder"
        ));

        RESET_POS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.endportalfinder.reset_pos",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.endportalfinder"
        ));

        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            while(RESET_POS.wasPressed()) {
                POS1 = null;
                POS2 = null;
                POS3 = null;
                YAW1 = Float.NaN;
                YAW2 = Float.NaN;
                Y = Float.NaN;

                info("Cleared directions");
            }

            while(REGISTER_POS.wasPressed()) {
                if(POS1 == null) {
                    POS3 = null;
                    POS1 = new Vector2d(mc.player.getX(), mc.player.getZ());
                    YAW1 = wrap(MathHelper.wrapDegrees(mc.player.getYaw(mc.getTickDelta())));
                    Y = (float) mc.player.getY();

                    info("Saved first direction");
                } else if(POS2 == null) {
                    POS2 = new Vector2d(mc.player.getX(), mc.player.getZ());
                    YAW2 = wrap(MathHelper.wrapDegrees(mc.player.getYaw(mc.getTickDelta())));

                    info("Saved second direction");
                } else {
                    double x0 = POS1.x;
                    double z0 = POS1.y;

                    double x1 = POS2.x - x0;
                    double z1 = POS2.y - z0;

                    double a = Math.tan((float) Math.toRadians(YAW1));
                    double b = Math.tan((float) Math.toRadians(YAW2));
                    double c = x1 - b * z1;

                    double z = c / (a - b);
                    double x = a * z;

                    x += x0;
                    z += z0;

                    POS1 = null;
                    POS2 = null;
                    POS3 = new Vector2d(x, z);
                    YAW1 = Float.NaN;
                    YAW2 = Float.NaN;

                    info("Intersection is " + (int) x + ", " + (int) Y + ", " + (int) z);
                }
            }
        });
    }
}
