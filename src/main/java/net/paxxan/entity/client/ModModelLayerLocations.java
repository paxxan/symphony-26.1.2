package net.paxxan.entity.client;

import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.Identifier;
import net.paxxan.Symphony;

import java.util.Set;

public class ModModelLayerLocations {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ModelLayerLocation FLUTE_SPIRIT = register("flute_spirit");

    private static ModelLayerLocation register(final String model) {
        return register(model, "main");
    }

    private static ModelLayerLocation register(final String model, final String layer) {
        ModelLayerLocation result = createLocation(model, layer);
        if (!ALL_MODELS.add(result)) {
            throw new IllegalStateException("Duplicate registration for " + result);
        } else {
            return result;
        }
    }

    private static ModelLayerLocation createLocation(final String model, final String layer) {
        return new ModelLayerLocation(Symphony.id(model), layer);
    }
}
