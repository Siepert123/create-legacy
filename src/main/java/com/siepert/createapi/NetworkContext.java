package com.siepert.createapi;

import com.siepert.createlegacy.CreateLegacyConfigHolder;

import java.util.ArrayList;
import java.util.List;

public class NetworkContext {

    public boolean infiniteSU = CreateLegacyConfigHolder.otherConfig.disableSU;

    public final List<KineticBlockInstance> blocksToActivate = new ArrayList<>();
    public int networkSpeed = 0;
    public int totalSU = 0;
    public int scheduledConsumedSU = 0;

}
