package tf2.Advancements;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class AdvancementTF implements ICriterionTrigger<AdvancementTF.Instance> {

    public final ResourceLocation ID;
    private final Map<PlayerAdvancements, AdvancementTF.Listeners> listeners = Maps.newHashMap();

    public AdvancementTF(ResourceLocation id)
    {
        this.ID = id;
    }

	@Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<AdvancementTF.Instance> listener) {
        AdvancementTF.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<AdvancementTF.Instance> listener) {
        AdvancementTF.Listeners listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public AdvancementTF.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new AdvancementTF.Instance(this.ID);
    }

    public void trigger(EntityPlayerMP player) {
        AdvancementTF.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance(ResourceLocation criterionIn) {
            super(criterionIn);
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<AdvancementTF.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<AdvancementTF.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<AdvancementTF.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            for (ICriterionTrigger.Listener<AdvancementTF.Instance> listener : Lists.newArrayList(this.listeners)) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }
}