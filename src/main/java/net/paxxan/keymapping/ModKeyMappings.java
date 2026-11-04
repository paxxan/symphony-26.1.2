package net.paxxan.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.paxxan.Symphony;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Symphony.id("flute_notes"));

    public static final KeyMapping FLUTE_UP_NOTE = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.symphony.flute_up_note",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_UP, CATEGORY));

    public static final KeyMapping FLUTE_LEFT_NOTE = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.symphony.flute_left_note",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT, CATEGORY));

    public static final KeyMapping FLUTE_RIGHT_NOTE = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.symphony.flute_right_note",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_RIGHT, CATEGORY));

    public static final KeyMapping FLUTE_DOWN_NOTE = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.symphony.flute_down_note",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_DOWN, CATEGORY));

    public static void register() {
        Symphony.LOGGER.info("Registering keybinds for " + Symphony.MOD_ID);
    }
}
